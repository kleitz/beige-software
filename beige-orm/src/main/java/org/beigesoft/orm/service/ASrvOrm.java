package org.beigesoft.orm.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.converter.IConverter;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.log.ILogger;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.persistable.DatabaseInfo;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.model.PropertiesBase;
import org.beigesoft.orm.model.ETypeField;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.FieldSql;

/**
 * <p>Base ORM service without INSERT implementation.
 * It evaluate tables descriptors
 * from properties files. It generates DDL and DML queries.
 * It perform SQL queries through database service.
 * It has no transaction logic.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public abstract class ASrvOrm<RS> implements ISrvOrm<RS> {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Settings service.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Properties base.</p>
   **/
  private PropertiesBase propertiesBase;

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private LinkedHashMap<String, TableSql> tablesMap =
    new LinkedHashMap<String, TableSql>();

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Helper to create Insert Update statement
   * by Android way.</p>
   **/
  private HlpInsertUpdate hlpInsertUpdate;

  /**
   * <p>Factory of converters entity to ColumnsValues.</p>
   **/
  private IFactoryAppBeansByClass<IConverter<?, ColumnsValues>>
    factoryCnvEntityToColumnsValues;

  /**
   * <p>Entitie's factories factory.</p>
   **/
  private IFactoryAppBeansByClass<IFactorySimple<?>> entitiesFactoriesFatory;

  /**
   * <p>Filler entities from IRecordSet<RS>.</p>
   **/
  private IFillerObjectsFrom<IRecordSet<RS>> fillerEntitiesFromRs;

  /**
   * <p>Factory of fillers object fields.</p>
   **/
  private IFactoryAppBeansByClass<IFillerObjectFields<?>>
    fctFillersObjectFields;

  /**
   * <p>ID for New Database.</p>
   **/
  private int newDatabaseId = 1;

  /**
   * <p>Getter for new database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID for new database
   **/
  @Override
  public final int getNewDatabaseId() {
    return this.newDatabaseId;
  }

  /**
   * <p>Setter for new database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @param pNewDatabaseId ID for new database
   **/
  @Override
  public final void setNewDatabaseId(final int pNewDatabaseId) {
    this.newDatabaseId = pNewDatabaseId;
  }

  /**
   * <p>Refresh entity from DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntity(final Map<String, Object> pAddParam,
    final T pEntity) throws Exception {
    String query = evalSqlSelect(pAddParam, pEntity.getClass());
    pAddParam.put("isOnlyId", Boolean.TRUE);
    ColumnsValues columnsValues = evalColumnsValues(pAddParam, pEntity);
    pAddParam.remove("isOnlyId");
    String whereStr = evalWhereId(pEntity, columnsValues);
    @SuppressWarnings("unchecked")
    Class<T> entityClass = (Class<T>) pEntity.getClass();
    return retrieveEntity(pAddParam, entityClass,
      query + " where " + whereStr);
  }

  /**
   * <p>Refresh entity from DB by its ID.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pItsId entity ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntityById(final Map<String, Object> pAddParam,
    final Class<T> pEntityClass, final Object pItsId) throws Exception {
    String query = evalSqlSelect(pAddParam, pEntityClass);
    @SuppressWarnings("unchecked")
    IFactorySimple<T> facEn = (IFactorySimple<T>) this.entitiesFactoriesFatory
      .lazyGet(pAddParam, pEntityClass);
    T entity = facEn.create(pAddParam);
    @SuppressWarnings("unchecked")
    IFillerObjectFields<T> filler = (IFillerObjectFields<T>)
      this.fctFillersObjectFields.lazyGet(pAddParam, pEntityClass);
    String idFldName = this.tablesMap
      .get(pEntityClass.getSimpleName()).getIdFieldName();
    filler.fill(pAddParam, entity, pItsId, idFldName);
    pAddParam.put("isOnlyId", Boolean.TRUE);
    ColumnsValues columnsValues = evalColumnsValues(pAddParam, entity);
    pAddParam.remove("isOnlyId");
    String whereId = evalWhereId(entity, columnsValues);
    return retrieveEntity(pAddParam, pEntityClass,
      query + " where " + whereId + ";\n");
  }

  /**
   * <p>Retrieve entity from DB by given query conditions.
   * The first record in record-set will be returned.</p>
   * @param <T> entity type
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * or "" that means without filter/order
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntityWithConditions(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass,
        final String pQueryConditions) throws Exception {
    if (pQueryConditions == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    String query = evalSqlSelect(pAddParam, pEntityClass);
    return retrieveEntity(pAddParam, pEntityClass,
      query + " " + pQueryConditions + ";\n");
  }

  /**
   * <p>Update entity in DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void updateEntity(
    final Map<String, Object> pAddParam,
      final T pEntity) throws Exception {
    ColumnsValues columnsValues = evalColumnsValues(pAddParam, pEntity);
    String whereStr = evalWhereForUpdate(pEntity, columnsValues);
    prepareColumnValuesForUpdate(columnsValues, pEntity);
    int result = getSrvDatabase().executeUpdate(pEntity.getClass()
      .getSimpleName().toUpperCase(), columnsValues, whereStr);
    if (result != 1) {
      if (result == 0 && columnsValues.ifContains(ISrvOrm.VERSION_NAME)) {
        throw new ExceptionWithCode(ISrvDatabase.DIRTY_READ, "dirty_read");
      } else {
        String query = hlpInsertUpdate.evalSqlUpdate(pEntity.getClass()
          .getSimpleName().toUpperCase(), columnsValues,
            whereStr);
        throw new ExceptionWithCode(ISrvDatabase.ERROR_INSERT_UPDATE,
          "It should be 1 row updated but it was "
            + result + ", query:\n" + query);
      }
    }
  }

  /**
   * <p>Delete entity with condition, e.g. complex ID.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pWhere Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void deleteEntityWhere(final Map<String, Object> pAddParam,
    final Class<T> pEntityClass, final String pWhere) throws Exception {
    if (pWhere == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    getSrvDatabase().executeDelete(pEntityClass.getSimpleName()
      .toUpperCase(), pWhere);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param <K> ID type
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void deleteEntity(
    final Map<String, Object> pAddParam,
      final T pEntity) throws Exception {
    pAddParam.put("isOnlyId", Boolean.TRUE);
    ColumnsValues columnsValues = evalColumnsValues(pAddParam, pEntity);
    pAddParam.remove("isOnlyId");
    String whereId = evalWhereId(pEntity, columnsValues);
    getSrvDatabase().executeDelete(pEntity.getClass()
      .getSimpleName().toUpperCase(), whereId);
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntityClass entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveList(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass) throws Exception {
    String query = evalSqlSelect(pAddParam, pEntityClass) + ";\n";
    return retrieveListByQuery(pAddParam, pEntityClass, query);
  }

  /**
   * <p>Retrieve a list of entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveListWithConditions(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass,
        final String pQueryConditions) throws Exception {
    if (pQueryConditions == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    String query = evalSqlSelect(pAddParam, pEntityClass) + " "
        + pQueryConditions + ";\n";
    return retrieveListByQuery(pAddParam, pEntityClass, query);
  }


  /**
   * <p>Entity's lists with filter "field" (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntity - Entity e.g. an invoice line with filled invoice
   * @param pFieldFor - Field For name e.g. "invoice"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveListForField(
    final Map<String, Object> pAddParam,
      final T pEntity, final String pFieldFor) throws Exception {
    String whereStr = evalWhereForField(pAddParam, pEntity, pFieldFor);
    String query = evalSqlSelect(pAddParam, pEntity.getClass())
      + whereStr + ";";
    @SuppressWarnings("unchecked")
    Class<T> entityClass = (Class<T>) pEntity.getClass();
    return retrieveListByQuery(pAddParam, entityClass, query);
  }

  /**
   * <p>Retrieve a list of entities by complex query that may contain
   * additional joins and filters, see Beige-Webstore for example.</p>
   * @param <T> - type of business object
   * @param pAddParam additional param, e.g. list of needed fields
   * @param pEntityClass entity class
   * @param pQuery Not NULL complex query
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveListByQuery(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass,
        final String pQuery) throws Exception {
    if (pQuery == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    List<T> result = new ArrayList<T>();
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pAddParam, pEntityClass, recordSet));
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntityClass entity class
   * @param pFirst number of the first record (from 0)
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrievePage(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    String query = evalSqlSelect(pAddParam, pEntityClass);
    return retrievePageByQuery(pAddParam, pEntityClass, query,
      pFirst, pPageSize);
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntityClass entity class
   * @param pQueryConditions not null e.g. "WHERE name='U1' ORDER BY id"
   * @param pFirst number of the first record (from 0)
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrievePageWithConditions(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass, final String pQueryConditions,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    String query = evalSqlSelect(pAddParam, pEntityClass) + " "
      + pQueryConditions;
    return retrievePageByQuery(pAddParam, pEntityClass, query,
      pFirst, pPageSize);
  }

  /**
   * <p>Retrieve a page of entities by given complex query.
   * For example it used to retrieve page ItemInList to sell in Beige-Webstore
   * by complex query what may consist of joints to filtered goods/services,
   * it also may has not all fields e.g. omit unused auctioning fields for
   * performance advantage.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param, e.g. set of needed fields names.
   * @param pEntityClass entity class
   * @param pQuery not null complex query without page conditions
   * @param pFirst number of the first record (from 0)
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrievePageByQuery(
    final Map<String, Object> pAddParam, final Class<T> pEntityClass,
      final String pQuery, final Integer pFirst,
        final Integer pPageSize) throws Exception {
    if (pQuery == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    List<T> result = new ArrayList<T>();
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(pQuery + " limit "
        + pPageSize + " offset " + pFirst + ";\n");
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pAddParam, pEntityClass, recordSet));
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final <T> Integer evalRowCount(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass) throws Exception {
    String query = "select count(*) as TOTALROWS from " + pEntityClass
      .getSimpleName().toUpperCase() + ";";
    return evalRowCountByQuery(pAddParam, pEntityClass, query);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final <T> Integer evalRowCountWhere(
    final Map<String, Object> pAddParam,
      final Class<T> pEntityClass, final String pWhere) throws Exception {
    if (pWhere == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    String query = "select count(*) as TOTALROWS from " + pEntityClass
      .getSimpleName().toUpperCase() + " where " + pWhere + ";";
    return evalRowCountByQuery(pAddParam, pEntityClass, query);
  }

  /**
   * <p>Calculate total rows for pagination by custom query.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pQuery not null custom query with named TOTALROWS
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final <T> Integer evalRowCountByQuery(
    final Map<String, Object> pAddParam, final Class<T> pEntityClass,
      final String pQuery) throws Exception {
    if (pQuery == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    return getSrvDatabase().evalIntegerResult(pQuery, "TOTALROWS");
  }

  /**
   * <p>Evaluate common(without WHERE) SQL SELECT
   * statement for entity type. It's used externally
   * to make more complex query with additional joints and filters.</p>
   * @param pAddParam additional param, e.g. set of needed fields names,
   * deep of foreign classes, etc.
   * @param <T> entity type
   * @param pEntityClass entity class
   * @return String SQL DML query
   * @throws Exception - an exception
   **/
  @Override
  public final <T> String evalSqlSelect(final Map<String, Object> pAddParam,
    final Class<T> pEntityClass) throws Exception {
    String tableName = pEntityClass.getSimpleName().toUpperCase();
    StringBuffer result =
      new StringBuffer("select ");
    StringBuffer joints =
      new StringBuffer();
    boolean isFirstField = true;
    TableSql tableSql = getTablesMap().get(pEntityClass.getSimpleName());
    if (tableSql == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "where_is_no_table_def_for::" + pEntityClass.getSimpleName());
    }
    for (Map.Entry<String, FieldSql> entry
      : tableSql.getFieldsMap().entrySet()) {
      if (!entry.getValue().getTypeField()
        .equals(ETypeField.DERIVED_FROM_COMPOSITE)) {
        if (entry.getValue().getForeignEntity() == null) {
          if (isFirstField) {
            isFirstField = false;
          } else {
            result.append(", ");
          }
          String columnName = entry.getKey().toUpperCase();
          result.append(tableName + "."
            + entry.getKey().toUpperCase() + " as " + columnName);
        } else {
          TableSql foreignTableSql = getTablesMap()
            .get(entry.getValue().getForeignEntity());
          String tableForeign = entry.getValue().getForeignEntity()
            .toUpperCase();
          String tbFrAlias;
          if (foreignTableSql.getIdColumnsNames().length > 1) {
            StringBuffer sb = new StringBuffer();
            for (String idClNm : foreignTableSql.getIdColumnsNames()) {
              sb.append(idClNm);
            }
            tbFrAlias = sb.toString().toUpperCase();
          } else {
            tbFrAlias = entry.getKey().toUpperCase();
          }
          joints.append(" left join " + tableForeign + " as "
            + tbFrAlias + " on ");
          for (int i = 0; i < foreignTableSql.getIdColumnsNames().length; i++) {
            if (i > 0) {
              joints.append(" and ");
            }
            String fieldFrNm;
            if (foreignTableSql.getIdColumnsNames().length > 1) {
              //composite name as foreign:
              fieldFrNm = foreignTableSql.getIdColumnsNames()[i].toUpperCase();
            } else {
              fieldFrNm = entry.getKey().toUpperCase();
            }
            joints.append(tableName + "." + fieldFrNm + "=" + tbFrAlias
              + "." + foreignTableSql.getIdColumnsNames()[i].toUpperCase());
          }
          for (Map.Entry<String, FieldSql> entryFor
            : foreignTableSql.getFieldsMap().entrySet()) {
            if (!entry.getValue().getTypeField()
                .equals(ETypeField.DERIVED_FROM_COMPOSITE)) {
              if (isFirstField) {
                isFirstField = false;
              } else {
                result.append(", ");
              }
              String columnForName = tbFrAlias
                + entryFor.getKey().toUpperCase();
              result.append(tbFrAlias + "."
                + entryFor.getKey().toUpperCase() + " as " + columnForName);
            }
          }
        }
      }
    }
    result.append(" from " + tableName + joints);
    return result.toString();
  }

  //to srv-database delegators:
  /**
   * <p>Geter for database ID.</p>
   * @return ID database
   **/
  @Override
  public final int getIdDatabase() {
    return this.srvDatabase.getIdDatabase();
  }

  //Utils:
  /**
   * <p>Load ORM configuration from files.</p>
   * @param pDirName properties base dir name e.g. "beige-orm"
   * @throws Exception - any exception
   **/
  public final void loadPropertiesBase(final String pDirName) throws Exception {
    this.propertiesBase = new PropertiesBase();
    propertiesBase.setDirectory(pDirName);
    this.propertiesBase.setJdbcDriverClass(this.mngSettings
      .getAppSettings().get(PropertiesBase.KEY_JDBC_DRIVER_CLASS));
    this.propertiesBase.setDatabaseName(this.mngSettings
      .getAppSettings().get(PropertiesBase.KEY_DATABASE_NAME));
    if (this.propertiesBase.getDatabaseName() != null
      && this.propertiesBase.getDatabaseName().contains(WORD_CURRENT_DIR)) {
      String currDir = System.getProperty("user.dir");
      this.propertiesBase.setDatabaseName(this.propertiesBase.getDatabaseName()
        .replace(WORD_CURRENT_DIR, currDir + File.separator));
    } else if (this.propertiesBase.getDatabaseName() != null
      && this.propertiesBase.getDatabaseName()
        .contains(WORD_CURRENT_PARENT_DIR)) {
      File fcd = new File(System.getProperty("user.dir"));
      this.propertiesBase.setDatabaseName(this.propertiesBase.getDatabaseName()
        .replace(WORD_CURRENT_PARENT_DIR, fcd.getParent() + File.separator));
    }
    this.propertiesBase.setDataSourceClassName(this.mngSettings
      .getAppSettings().get(PropertiesBase.KEY_DATASOURCE_CLASS));
    this.propertiesBase.setUserName(this.mngSettings
      .getAppSettings().get(PropertiesBase.KEY_USER_NAME));
    this.propertiesBase.setUserPassword(this.mngSettings
      .getAppSettings().get(PropertiesBase.KEY_USER_PASSWORD));
    this.propertiesBase.setDatabaseUrl(this.mngSettings
      .getAppSettings().get(PropertiesBase.KEY_DATABASE_URL));
    if (this.propertiesBase.getDatabaseUrl() != null
      && this.propertiesBase.getDatabaseUrl().contains(WORD_CURRENT_DIR)) {
      String currDir = System.getProperty("user.dir");
      this.propertiesBase.setDatabaseUrl(this.propertiesBase.getDatabaseUrl()
        .replace(WORD_CURRENT_DIR, currDir + File.separator));
    } else if (this.propertiesBase.getDatabaseUrl() != null
      && this.propertiesBase.getDatabaseUrl()
        .contains(WORD_CURRENT_PARENT_DIR)) {
      File fcd = new File(System.getProperty("user.dir"));
      this.propertiesBase.setDatabaseUrl(this.propertiesBase.getDatabaseUrl()
        .replace(WORD_CURRENT_PARENT_DIR, fcd.getParent() + File.separator));
    }
  }

  /**
   * <p>Load map of SQL tables descriptors.</p>
   * @throws Exception - any exception
   **/
  public final void loadSqlTables() throws Exception {
    for (Class<?> clazz : this.mngSettings.getClasses()) {
      TableSql tableSql = new TableSql();
      makeTableSqlFromXml(tableSql, clazz);
      this.tablesMap.put(clazz.getSimpleName(), tableSql);
    }
    for (Class<?> clazz : this.mngSettings.getClasses()) {
      TableSql tableSql = this.tablesMap.get(clazz.getSimpleName());
      StringBuffer sbIdNamesUc = new StringBuffer("");
      for (int i = 0; i < tableSql.getIdColumnsNames().length; i++) {
        if (i > 0) {
          sbIdNamesUc.append(",");
        }
        sbIdNamesUc.append(tableSql.getIdColumnsNames()[i].toUpperCase());
      }
      Field[] fields = getUtlReflection().retrieveFields(clazz);
      for (Field field : fields) {
        FieldSql fieldSql = tableSql.getFieldsMap().get(field.getName());
        if (fieldSql != null) { //if persistable or composite (foreign CID)
          TableSql tableSqlForeign = this.tablesMap.get(field.getType()
            .getSimpleName());
          if (tableSqlForeign != null) {
            //if it's foreign entity according entities list ORDER
            //make sure ORDER of entities!!!
            fieldSql.setForeignEntity(field.getType().getSimpleName());
            String isNullableStr = this.mngSettings.getFieldsSettings()
              .get(clazz).get(field.getName())
                .get("isNullable");
            if (fieldSql.getDefinition() != null // FK composite ID
              && !Boolean.valueOf(isNullableStr)
                && !fieldSql.getDefinition().contains("not null")) {
              fieldSql.setDefinition(fieldSql.getDefinition() + " not null");
            }
            //FK:
            StringBuffer sbForeignIdNamesUc = new StringBuffer("");
            for (int i = 0; i < tableSqlForeign.getIdColumnsNames()
              .length; i++) {
              if (i > 0) {
                sbForeignIdNamesUc.append(",");
              }
              sbForeignIdNamesUc.append(tableSqlForeign
                .getIdColumnsNames()[i].toUpperCase());
            }
            String fkNames;
            if (tableSqlForeign.getIdColumnsNames().length > 1) {
              // composite FK:
              fkNames = sbForeignIdNamesUc.toString();
              // check if it is also primary key:
              boolean isAlsoPk = true;
              if (tableSql.getIdColumnsNames().length
                == tableSqlForeign.getIdColumnsNames().length) {
                for (int i = 0; i < tableSql.getIdColumnsNames()
                  .length; i++) {
                  if (!tableSqlForeign.getIdColumnsNames()[0]
                    .equals(tableSql.getIdColumnsNames()[0])) {
                    isAlsoPk = false;
                    break;
                  }
                }
              }
              if (isAlsoPk) {
                fieldSql.setTypeField(ETypeField.COMPOSITE_FK_PK);
              } else {
                fieldSql.setTypeField(ETypeField.COMPOSITE_FK);
              }
              for (String cIdNm : tableSqlForeign.getIdColumnsNames()) {
                FieldSql fldCid = new FieldSql();
                fldCid.setTypeField(ETypeField.DERIVED_FROM_COMPOSITE);
                if (!Boolean.valueOf(isNullableStr)
                    && !tableSqlForeign.getFieldsMap()
                      .get(cIdNm).getDefinition().contains("not null")) {
                  fldCid.setDefinition(tableSqlForeign.getFieldsMap()
                    .get(cIdNm).getDefinition() + " not null");
                } else if (Boolean.valueOf(isNullableStr)
                    && fldCid.getDefinition().contains("not null")) {
                  fldCid.setDefinition(tableSqlForeign.getFieldsMap()
                    .get(cIdNm).getDefinition().replace(" not null", ""));
                } else {
                  fldCid.setDefinition(tableSqlForeign.getFieldsMap()
                    .get(cIdNm).getDefinition());
                }
                fldCid.setForeignEntity(tableSqlForeign.getFieldsMap()
                  .get(cIdNm).getForeignEntity());
                tableSql.getFieldsMap().put(cIdNm, fldCid);
              }
            } else {
              fkNames = field.getName().toUpperCase();
            }
            String constraintFk = "constraint fk"
              + clazz.getSimpleName()
              + field.getType().getSimpleName() + field.getName()
              + " foreign key (" + fkNames
              + ") references " + field.getType().getSimpleName()
                .toUpperCase()
              + " (" + sbForeignIdNamesUc + ")";
            String constraint = tableSql.getConstraint();
            if (constraint != null) {
              constraint += ",\n" + constraintFk;
            } else {
              constraint = constraintFk;
            }
            tableSql.setConstraint(constraint);
          }
          //if field is PK, make sure NOT NULL:
          if (sbIdNamesUc.toString().contains(field.getName().toUpperCase())
              && !fieldSql.getDefinition().contains("not null")) {
            fieldSql.setDefinition(fieldSql.getDefinition()
              + " not null");
          }
        }
      }
      for (String colIdNm : tableSql.getIdColumnsNames()) {
        if (tableSql.getFieldsMap().get(colIdNm) == null) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "Where is no column ID class/name: " + clazz.getSimpleName()
            + "/" + colIdNm);
        }
      }
      //PK if complex (composite or foreign entity):
      if (tableSql.getIdColumnsNames().length > 1 || tableSql.getFieldsMap()
        .get(tableSql.getIdColumnsNames()[0]).getForeignEntity() != null) {
        String constraintPk = "constraint pk"
          + clazz.getSimpleName() + tableSql.getIdColumnsNames()[0]
            + " primary key (" + sbIdNamesUc + ")";
        if (tableSql.getConstraint() == null) {
          tableSql.setConstraint(constraintPk);
        } else {
          tableSql.setConstraint(constraintPk
            + ",\n" + tableSql.getConstraint());
        }
      }
      String constraintAdd = this.mngSettings.getClassesSettings()
        .get(clazz).get("constraintAdd");
      if (constraintAdd != null) {
        if (tableSql.getConstraint() == null) {
          tableSql.setConstraint(constraintAdd);
        } else {
          tableSql.setConstraint(tableSql.getConstraint()
            + ",\n" + constraintAdd);
        }
      }
    }
  }

  /**
   * <p>Make TableSql from XML properties.</p>
   * @param pTableSql tableSql
   * @param pClazz entity class
   * @throws Exception - any exception
   **/
  public final void makeTableSqlFromXml(final TableSql pTableSql,
    final Class<?> pClazz) throws Exception {
    if (this.mngSettings.getFieldsSettings().get(pClazz) == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no fields settings for class " + pClazz);
    }
    pTableSql.setVersionAlgorithm(Integer.parseInt(this.mngSettings
      .getClassesSettings().get(pClazz)
        .get("versionAlgorithm")));
    pTableSql.setIdColumnsNames(this.mngSettings.getClassesSettings()
      .get(pClazz).get("idColumnsNames").split(","));
    pTableSql.setIdFieldName(this.mngSettings.getClassesSettings().get(pClazz)
      .get("idFieldName"));
    if (pTableSql.getIdFieldName() == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "Where is no field ID name for class: " + pClazz.getSimpleName());
    }
    pTableSql.setOwnerFieldName(this.mngSettings.getClassesSettings()
      .get(pClazz).get("ownerFieldName"));
    Field[] fields = getUtlReflection().retrieveFields(pClazz);
    for (Field field : fields) {
      if (this.mngSettings.getFieldsSettings()
        .get(pClazz).get(field.getName()) != null) {
        FieldSql fieldSql = new FieldSql();
        String definition = this.mngSettings.getFieldsSettings().get(pClazz)
          .get(field.getName())
            .get("definition");
        if (definition != null) {
          String isNullableStr = this.mngSettings.getFieldsSettings()
            .get(pClazz).get(field.getName()).get("isNullable");
          if (!Boolean.valueOf(isNullableStr)
            && !definition.contains("not null")) {
            definition += " not null";
          }
        }
        fieldSql.setDefinition(definition);
        pTableSql.getFieldsMap().put(field.getName(), fieldSql);
      }
    }
  }

  /**
   * <p>Retrieve entity from DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param, e.g. already retrieved TableSql
   * @param pEntityClass entity class
   * @param pQuery SELECT statement
   * @return entity or null
   * @throws Exception - an exception
   **/
  public final <T> T retrieveEntity(final Map<String, Object> pAddParam,
    final Class<T> pEntityClass, final String pQuery) throws Exception {
    T entity = null;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        entity = retrieveEntity(pAddParam, pEntityClass, recordSet);
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return entity;
  }

  /**
   * <p>Retrieve entity from DB.</p>
   * @param <T> entity type
   * @param pAddParam additional params, e.g. already retrieved TableSql.
   * @param pEntityClass entity class
   * @param pRecordSet record set
   * @return entity
   * @throws Exception - an exception
   **/
  public final <T> T retrieveEntity(final Map<String, Object> pAddParam,
    final Class<T> pEntityClass,
      final IRecordSet<RS> pRecordSet) throws Exception {
    @SuppressWarnings("unchecked")
    IFactorySimple<T> facEn = (IFactorySimple<T>) this.entitiesFactoriesFatory
      .lazyGet(pAddParam, pEntityClass);
    T entity = facEn.create(pAddParam);
    this.fillerEntitiesFromRs.fill(pAddParam, entity, pRecordSet);
    return entity;
  }

  /**
   * <p>Evaluate Android compatible values map of given entity.
   * For "itsVersion" evaluate new one and set it in the entity,
   * old version stored in column "itsVersionOld".
   * Used in INSERT/UPDATE statement.</p>
   * @param <T> entity type
   * @param pAddParam additional params, expected "isOnlyId"-true for
   * converting only ID field.
    * @param pEntity entity
   * @return ColumnsValues type-safe map fieldName-fieldValue
   * @throws Exception an exception
   **/
  public final <T> ColumnsValues
    evalColumnsValues(final Map<String, Object> pAddParam,
      final T pEntity) throws Exception {
    TableSql tableSql = getTablesMap().get(pEntity.getClass().getSimpleName());
    if (tableSql.getFieldsMap().containsKey(ISrvOrm.VERSION_NAME)) {
      pAddParam.put("versionAlgorithm", tableSql.getVersionAlgorithm());
    }
    @SuppressWarnings("unchecked")
    IConverter<T, ColumnsValues> convToColVal = (IConverter<T, ColumnsValues>)
      this.factoryCnvEntityToColumnsValues
        .lazyGet(pAddParam, pEntity.getClass());
    return convToColVal.convert(pAddParam, pEntity);
  }

  /**
   * <p>Prepare ColumnsValues for update - remove PK field(s)
   * and OLD_VERSION.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @param pColumnsValues ColumnsValues
   * @throws Exception - an exception
   **/
  public final <T> void prepareColumnValuesForUpdate(
    final ColumnsValues pColumnsValues,
      final T pEntity) throws Exception {
    //remove old version and ID columns:
    pColumnsValues.getLongsMap().remove(ISrvOrm.VERSIONOLD_NAME);
    TableSql tableSql = this.tablesMap.get(pEntity.getClass().getSimpleName());
    for (String idFldNm : tableSql.getIdColumnsNames()) {
      pColumnsValues.remove(idFldNm);
    }
  }

  /**
   * <p>Evaluate Where ID the entity.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @param pColumnsValues columns values
   * @return where conditions e.g. "itsId=1"
   * @throws Exception an exception
   **/
  public final <T> String evalWhereId(
    final T pEntity, final ColumnsValues pColumnsValues) throws Exception {
    TableSql tableSql = this.tablesMap.get(pEntity.getClass().getSimpleName());
    StringBuffer sbWhereId = new StringBuffer("");
    for (int i = 0; i < tableSql.getIdColumnsNames().length; i++) {
      if (i > 0) {
        sbWhereId.append(" and ");
      }
      sbWhereId.append(pEntity.getClass().getSimpleName().toUpperCase()
        + "." + tableSql.getIdColumnsNames()[i].toUpperCase() + "="
          + pColumnsValues.evalSqlValue(tableSql.getIdColumnsNames()[i]));
    }
    return sbWhereId.toString();
  }

  /**
   * <p>Evaluate Where conditions the entity.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @param pColumnsValues columns values
   * @return where conditions e.g. "itsId=1 AND itsVersion=2"
   * @throws Exception an exception
   **/
  public final <T> String evalWhereForUpdate(
    final T pEntity, final ColumnsValues pColumnsValues) throws Exception {
    if (pColumnsValues.ifContains(ISrvOrm.VERSION_NAME)) {
      return evalWhereId(pEntity, pColumnsValues) + " and " + ISrvOrm
        .VERSION_NAME.toUpperCase() + "=" + pColumnsValues
          .evalSqlValue(ISrvOrm.VERSIONOLD_NAME);
    } else {
      return evalWhereId(pEntity, pColumnsValues);
    }
  }


  /**
   * <p>Evaluate where for field (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity
   * @param pAddParam additional param
   * @param pEntity - Entity e.g. an invoice line with filled invoice
   * @param pFieldFor - Field For name e.g. "invoice"
   * @return where clause e.g. " where invoice=1"
   * @throws Exception - an exception
   */
  public final <T> String evalWhereForField(
    final Map<String, Object> pAddParam,
      final T pEntity, final String pFieldFor) throws Exception {
    String[] fieldsNames = new String[] {pFieldFor};
    pAddParam.put("fieldsNames", fieldsNames);
    ColumnsValues columnsValues = evalColumnsValues(pAddParam, pEntity);
    pAddParam.remove("fieldsNames");
    TableSql tableSql = this.tablesMap.get(pEntity.getClass().getSimpleName());
    FieldSql fldSql = tableSql.getFieldsMap().get(pFieldFor);
    StringBuffer sbWhere = new StringBuffer(" where ");
    String tableNm = pEntity.getClass().getSimpleName().toUpperCase();
    if (fldSql.getTypeField().equals(ETypeField.COMPOSITE_FK_PK)
      || fldSql.getTypeField().equals(ETypeField.COMPOSITE_FK)) {
      TableSql tableSqlFr = this.tablesMap.get(fldSql.getForeignEntity());
      for (int i = 0; i < tableSqlFr.getIdColumnsNames().length; i++) {
        if (i > 0) {
          sbWhere.append(" and ");
        }
        sbWhere.append(tableNm + "." + tableSqlFr.getIdColumnsNames()[i]
          .toUpperCase() + "=" + columnsValues
            .evalObjectValue(tableSqlFr.getIdColumnsNames()[i]));
      }
    } else {
      sbWhere.append(tableNm + "." + pFieldFor.toUpperCase() + "="
        + columnsValues.evalObjectValue(pFieldFor));
    }
    return sbWhere.toString();
  }

  /**
   * <p>Load configuration from xml.</p>
   * @param pDirName properties base dir name e.g. "beige-orm"
   * @param pFileName properties file name e.g. persistence-postgresql.xml
   * @throws Exception - an exception
   **/
  public final void loadConfiguration(final String pDirName,
    final String pFileName) throws Exception {
    this.mngSettings.loadConfiguration(pDirName, pFileName);
    loadPropertiesBase(pDirName);
    loadSqlTables();
  }

  /**
   * <p>Create tables if they not exists. If initRdbms in app-settings
   * not null, then run [initRdbms] SQL.
   * If all tables was created then execute insert.sql if it exist.
   * Then if not all table was created and upgrade_[current_version+1].sql
   * exist then execute it.</p>
   * @param pAddParam additional param
   * @return if all tables has neen created or some added
   * @throws Exception - an exception
   **/
  public final boolean initializeDatabase(
    final Map<String, Object> pAddParam) throws Exception {
    String queryExistanceTable = this.mngSettings.getAppSettings()
      .get("checkTableExist");
    boolean ifCreatedOrAdded = false;
    boolean ifAllTablesCreated = true;
    try {
      for (Map.Entry<String, TableSql> entry : getTablesMap().entrySet()) {
        IRecordSet rs = srvDatabase.retrieveRecords(queryExistanceTable
          .replace(ISrvOrm.TABLE_PARAM_NAME_IN_EXISTENCE_QUERY,
            entry.getKey().toUpperCase()));
        if (!rs.moveToFirst()) { //cause problemable SQL identifier standard
          rs = srvDatabase.retrieveRecords(queryExistanceTable
            .replace(ISrvOrm.TABLE_PARAM_NAME_IN_EXISTENCE_QUERY,
              entry.getKey().toLowerCase()));
          if (!rs.moveToFirst()) {
            ifCreatedOrAdded = true;
            String ddlStatment = evalSqlCreateTable(entry.getKey());
            srvDatabase.executeQuery(ddlStatment);
          } else {
            ifAllTablesCreated = false;
          }
        } else {
          ifAllTablesCreated = false;
        }
      }
      String dirPath = "/";
      if (this.propertiesBase.getDirectory() != null) {
        dirPath = "/" + this.propertiesBase.getDirectory()
          + "/";
      }
      String useSubFolder = this.mngSettings
        .getAppSettings().get("useSubFolder");
      String initSql = loadString(dirPath + useSubFolder
          + "/" + "init.sql");
      if (initSql != null) {
        getLogger().info(ASrvOrm.class, "init.sql found, try to execute.");
        for (String initSingle : initSql.split("\n")) {
          if (initSingle.trim().length() > 1 && !initSingle.startsWith("/")) {
            srvDatabase.executeQuery(initSingle);
          }
        }
      } else {
        getLogger().info(ASrvOrm.class, "init.sql not found.");
      }
      if (ifAllTablesCreated) {
        getLogger().info(ASrvOrm.class, "all tables has been created.");
        DatabaseInfo databaseInfo = new DatabaseInfo();
        int dbVer = Integer.parseInt(this.mngSettings
        .getAppSettings().get("databaseVersion"));
        databaseInfo.setDatabaseVersion(dbVer);
        databaseInfo.setDatabaseId(getNewDatabaseId());
        databaseInfo.setDescription(this.mngSettings
          .getAppSettings().get("title"));
        insertEntity(pAddParam, databaseInfo);
        String insertSql = loadString(dirPath + "insert.sql");
        if (insertSql != null) {
          getLogger().info(ASrvOrm.class, dirPath
            + "insert.sql found, try to execute.");
          for (String insertSingle : insertSql.split("\n")) {
            if (insertSingle.trim().length() > 1
                && !insertSingle.startsWith("/")) {
              srvDatabase.executeQuery(insertSingle);
            }
          }
        } else {
          getLogger().info(ASrvOrm.class, dirPath + "insert.sql not found.");
        }
      } else if (ifCreatedOrAdded) {
        getLogger().info(ASrvOrm.class, "new tables has been added.");
      } else {
        getLogger().info(ASrvOrm.class, "tables already created.");
      }
      if (!ifAllTablesCreated) {
        tryUgradeDatabaseAnyWay(dirPath + useSubFolder + "/");
      }
    } finally {
      srvDatabase.releaseResources(); //close connection
    }
    return ifCreatedOrAdded;
  }

  /**
   * <p>Trying to upgrade database in single transaction.</p>
   * @param pUpgradeDir e.g. bs/beige-orm/sqlite
   * @throws Exception - an exception
   **/
  public final void tryUgradeDatabase(
    final String pUpgradeDir) throws Exception {
    Integer nextVersion = srvDatabase.getVersionDatabase() + 1;
    String upgradeSqlName = "upgrade_" + nextVersion + ".sql";
    String upgradeSql = loadString(pUpgradeDir + upgradeSqlName);
    while (upgradeSql != null) {
      getLogger().info(ASrvOrm.class, pUpgradeDir
        + upgradeSqlName + " found, try to execute.");
      //in single transaction:
      try {
        this.srvDatabase.setIsAutocommit(false);
        this.srvDatabase.setTransactionIsolation(ISrvDatabase
          .TRANSACTION_READ_UNCOMMITTED);
        this.srvDatabase.beginTransaction();
        for (String upgradeSingle : upgradeSql.split("\n")) {
          if (upgradeSingle.trim().length() > 1
            && !upgradeSingle.startsWith("/")) {
            srvDatabase.executeQuery(upgradeSingle);
          }
        }
        this.srvDatabase.commitTransaction();
      } catch (Exception ex) {
        this.srvDatabase.rollBackTransaction();
        throw ex;
      }
      nextVersion++;
      upgradeSqlName = "upgrade_" + nextVersion + ".sql";
      upgradeSql = loadString(pUpgradeDir + upgradeSqlName);
    }
  }

  /**
   * <p>Trying to upgrade database with transaction per line,
   * if exception - it doesn't stop.
   * In case when old database has no BEGINNINGINVENTORY,
   * then it create it last version,
   * but alter table for new version will fail, so whole update will fine.</p>
   * @param pUpgradeDir e.g. bs/beige-orm/sqlite
   * @throws Exception - an exception
   **/
  public final void tryUgradeDatabaseAnyWay(
    final String pUpgradeDir) throws Exception {
    Integer nextVersion = srvDatabase.getVersionDatabase() + 1;
    String upgradeSqlName = "upgrade_" + nextVersion + ".sql";
    String upgradeSql = loadString(pUpgradeDir + upgradeSqlName);
    while (upgradeSql != null) {
      getLogger().info(ASrvOrm.class, pUpgradeDir
        + upgradeSqlName + " found, try to execute.");
      for (String upgradeSingle : upgradeSql.split("\n")) {
        if (upgradeSingle.trim().length() > 1
          && !upgradeSingle.startsWith("/")) {
          try {
            srvDatabase.executeQuery(upgradeSingle);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
      nextVersion++;
      upgradeSqlName = "upgrade_" + nextVersion + ".sql";
      upgradeSql = loadString(pUpgradeDir + upgradeSqlName);
    }
  }

  /**
   * <p>Evaluate SQL create table statement.</p>
   * @param pEntityName entity simple name
   * @return SQL create table
   **/
  public final String evalSqlCreateTable(final String pEntityName) {
    TableSql tableSql = getTablesMap().get(pEntityName);
    StringBuffer result =
      new StringBuffer("create table " + pEntityName.toUpperCase() + " (\n");
    boolean isFirstField = true;
    for (Map.Entry<String, FieldSql> entry
      : tableSql.getFieldsMap().entrySet()) {
      if (!(entry.getValue().getTypeField().equals(ETypeField.COMPOSITE_FK)
        || entry.getValue().getTypeField()
          .equals(ETypeField.COMPOSITE_FK_PK))) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(",\n");
        }
        result.append(entry.getKey().toUpperCase() + " "
          + tableSql.getFieldsMap().get(entry.getKey()).getDefinition());
      }
    }
    if (tableSql.getConstraint() != null) {
      result.append(",\n" + tableSql.getConstraint());
    }
    result.append(");\n");
    return result.toString();
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = ASrvOrm.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = ASrvOrm.class.getResourceAsStream(pFileName);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final IUtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final IUtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }

  /**
   * <p>Geter for tablesMap
   * (Class.getSimpleName())-(TableSql).</p>
   * @return Map<String, TableSql> map of SQL tables descriptors
   **/
  public final LinkedHashMap<String, TableSql> getTablesMap() {
    return this.tablesMap;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Geter for mngSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngSettings() {
    return this.mngSettings;
  }

  /**
   * <p>Setter for mngSettings.</p>
   * @param pMngSettings reference/value
   **/
  public final void setMngSettings(final IMngSettings pMngSettings) {
    this.mngSettings = pMngSettings;
  }

  /**
   * <p>Geter for hlpInsertUpdate.</p>
   * @return HlpInsertUpdate
   **/
  public final HlpInsertUpdate getHlpInsertUpdate() {
    return this.hlpInsertUpdate;
  }

  /**
   * <p>Setter for hlpInsertUpdate (shared with SRV Database).</p>
   * @param pHlpInsertUpdate reference
   **/
  public final void setHlpInsertUpdate(final HlpInsertUpdate pHlpInsertUpdate) {
    this.hlpInsertUpdate = pHlpInsertUpdate;
  }

  /**
   * <p>Getter for factoryCnvEntityToColumnsValues.</p>
   * @return IFactoryAppBeansByClass<IConverter<?, ColumnsValues>>
   **/
  public final IFactoryAppBeansByClass<IConverter<?, ColumnsValues>>
    getFactoryCnvEntityToColumnsValues() {
    return this.factoryCnvEntityToColumnsValues;
  }

  /**
   * <p>Setter for factoryCnvEntityToColumnsValues.</p>
   * @param pFactoryCnvEntityToColumnsValues reference
   **/
  public final void setFactoryCnvEntityToColumnsValues(
    final IFactoryAppBeansByClass<IConverter<?, ColumnsValues>>
      pFactoryCnvEntityToColumnsValues) {
    this.factoryCnvEntityToColumnsValues = pFactoryCnvEntityToColumnsValues;
  }

  /**
   * <p>Getter for entitiesFactoriesFatory.</p>
   * @return IFactoryAppBeansByClass<IFactorySimple<?>>
   **/
  public final IFactoryAppBeansByClass<IFactorySimple<?>>
    getEntitiesFactoriesFatory() {
    return this.entitiesFactoriesFatory;
  }

  /**
   * <p>Setter for entitiesFactoriesFatory.</p>
   * @param pEntitiesFactoriesFatory reference
   **/
  public final void setEntitiesFactoriesFatory(
    final IFactoryAppBeansByClass<IFactorySimple<?>> pEntitiesFactoriesFatory) {
    this.entitiesFactoriesFatory = pEntitiesFactoriesFatory;
  }

  /**
   * <p>Getter for fillerEntitiesFromRs.</p>
   * @return IFillerObjectsFrom<IRecordSet<RS>>
   **/
  public final IFillerObjectsFrom<IRecordSet<RS>> getFillerEntitiesFromRs() {
    return this.fillerEntitiesFromRs;
  }

  /**
   * <p>Setter for fillerEntitiesFromRs.</p>
   * @param pFillerEntitiesFromRs reference
   **/
  public final void setFillerEntitiesFromRs(
    final IFillerObjectsFrom<IRecordSet<RS>> pFillerEntitiesFromRs) {
    this.fillerEntitiesFromRs = pFillerEntitiesFromRs;
  }
  /**
   * <p>Getter for fctFillersObjectFields.</p>
   * @return IFactoryAppBeansByClass<IFillerObjectFields<?>>
   **/
  public final IFactoryAppBeansByClass<IFillerObjectFields<?>>
    getFctFillersObjectFields() {
    return this.fctFillersObjectFields;
  }

  /**
   * <p>Setter for fctFillersObjectFields.</p>
   * @param pFctFillersObjectFields reference
   **/
  public final void setFctFillersObjectFields(
    final IFactoryAppBeansByClass<IFillerObjectFields<?>>
      pFctFillersObjectFields) {
    this.fctFillersObjectFields = pFctFillersObjectFields;
  }


  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Setter for propertiesBase.</p>
   * @param pPropertiesBase reference/value
   **/
  public final void setPropertiesBase(final PropertiesBase pPropertiesBase) {
    this.propertiesBase = pPropertiesBase;
  }

  /**
   * <p>Get Properties Base.</p>
   * @return Properties Base
   **/
  public final PropertiesBase getPropertiesBase() {
    return this.propertiesBase;
  }
}
