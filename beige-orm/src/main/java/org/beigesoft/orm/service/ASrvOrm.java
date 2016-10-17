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

import java.util.Date;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.net.URL;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.model.PropertiesBase;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.FieldSql;
import org.beigesoft.persistable.DatabaseInfo;
import org.beigesoft.persistable.APersistableBase;
import org.beigesoft.log.ILogger;

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
   * <p>If need to SQL escape for value string.
   * Android do it itself.</p>
   **/
  private boolean isNeedsToSqlEscape = true;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Settings service.</p>
   **/
  private MngSettings mngSettings;

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
   * <p>Record retriever.</p>
   **/
  private ISrvRecordRetriever<RS> srvRecordRetriever;

  /**
   * <p>Reflection service.</p>
   **/
  private final UtlReflection utlReflection = new UtlReflection();

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>SQL Escape service.</p>
   **/
  private ISrvSqlEscape srvSqlEscape;

  /**
   * <p>Helper to create Insert Update statement
   * by Android way.</p>
   **/
  private HlpInsertUpdate hlpInsertUpdate;

  /**
   * <p>Create entity.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T createEntity(
    final Class<T> pEntityClass) throws Exception {
    Constructor<T> constructor = pEntityClass.getDeclaredConstructor();
    T entity = constructor.newInstance();
    if (APersistableBase.class.isAssignableFrom(pEntityClass)) {
      initPersistableBase(entity);
    }
    return entity;
  }

    /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pEntityOwnerClass entity owner class
   * @param idEntityOwner entity owner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T createEntityWithOwner(final Class<T> pEntityClass,
    final Class<?> pEntityOwnerClass,
      final Object idEntityOwner) throws Exception {
    Constructor<T> constructor = pEntityClass.getDeclaredConstructor();
    T entity = constructor.newInstance();
    if (APersistableBase.class.isAssignableFrom(pEntityClass)) {
      initPersistableBase(entity);
    }
    Constructor<?> constrEntityOwner = pEntityOwnerClass
      .getDeclaredConstructor();
    Object entityOwner = constrEntityOwner.newInstance();
    TableSql tableSqlEntityOwner = getTablesMap()
      .get(pEntityOwnerClass.getSimpleName());
    Field fieldIdEntityOwner = getUtlReflection().retrieveField(
      pEntityOwnerClass, tableSqlEntityOwner.getIdName());
    fieldIdEntityOwner.setAccessible(true);
    if (fieldIdEntityOwner.getType() == Integer.class) {
      Integer value = Integer.valueOf(idEntityOwner.toString());
      fieldIdEntityOwner.set(entityOwner, value);
    } else if (fieldIdEntityOwner.getType() == Long.class) {
      Long value = Long.valueOf(idEntityOwner.toString());
      fieldIdEntityOwner.set(entityOwner, value);
    } else if (fieldIdEntityOwner.getType() == String.class) {
      fieldIdEntityOwner.set(entityOwner, idEntityOwner.toString());
    } else {
      String msg =
        "There is no rule to fill column id value from field "
          + fieldIdEntityOwner.getName() + " of "
            + fieldIdEntityOwner.getType() + " in " + entityOwner;
      throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED, msg);
    }
    String ownerFieldName = evalOwnerFieldName(pEntityClass, pEntityOwnerClass);
    Field fieldEntityOwner = getUtlReflection().retrieveField(pEntityClass,
      ownerFieldName);
    fieldEntityOwner.setAccessible(true);
    fieldEntityOwner.set(entity, entityOwner);
    if (APersistableBase.class.isAssignableFrom(pEntityClass)) {
      initPersistableBase(entity);
    }
    return entity;
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveCopyEntity(
    final Class<T> pEntityClass, final Object pId) throws Exception {
    T entity = retrieveEntityById(pEntityClass, pId);
    TableSql tableSql = getTablesMap().get(pEntityClass.getSimpleName());
    Field fieldId = getUtlReflection().retrieveField(pEntityClass,
      tableSql.getIdName());
    fieldId.setAccessible(true);
    fieldId.set(entity, null);
    if (APersistableBase.class.isAssignableFrom(pEntityClass)) {
      initPersistableBase(entity);
    }
    return entity;
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntity(final T pEntity) throws Exception {
    @SuppressWarnings("unchecked")
    Class<T> entityClass = (Class<T>) pEntity.getClass();
    TableSql tableSql = getTablesMap().get(entityClass.getSimpleName());
    Field fieldId = getUtlReflection().retrieveField(entityClass,
      tableSql.getIdName());
    fieldId.setAccessible(true);
    Object id = fieldId.get(pEntity);
    return retrieveEntityById(entityClass, id);
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntityById(
    final Class<T> pEntityClass, final Object pId) throws Exception {
    String query = evalSqlSelect(pEntityClass, pId);
    return retrieveEntity(pEntityClass, query);
  }

  /**
   * <p>Retrieve entity from DB.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pQuery SELECT statement
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntity(
    final Class<T> pEntityClass, final String pQuery) throws Exception {
    T entity = null;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        entity = retrieveEntity(pEntityClass, recordSet);
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return entity;
  }

  /**
   * <p>Retrieve entity from DB by given query conditions.
   * The first record in recordset will be used.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pQueryConditions e.g. "WHERE name='U1' ORDER BY id"
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retrieveEntityWithConditions(
    final Class<T> pEntityClass,
      final String pQueryConditions) throws Exception {
    String query = evalSqlSelect(pEntityClass);
    if (pQueryConditions == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    return retrieveEntity(pEntityClass, query + " " + pQueryConditions
      + ";\n");
  }

  /**
   * <p>Update entity in DB.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void updateEntity(
    final T pEntity) throws Exception {
    ColumnsValues columnsValues = evalColumnsValuesAndFillNewVersion(pEntity);
    String whereStr = evalWhereForUpdate(pEntity, columnsValues);
    columnsValues.getLongsMap().remove(ISrvOrm.VERSIONOLD_NAME);
    TableSql tableSql = this.tablesMap.get(pEntity.getClass().getSimpleName());
    columnsValues.getLongsMap().remove(tableSql.getIdName());
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
   * <p>Update entity with condition, e.g. for complex ID.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @param pWhere Where e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void updateEntityWhere(
    final T pEntity, final String pWhere) throws Exception {
    if (pWhere == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    ColumnsValues columnsValues = evalColumnsValuesAndFillNewVersion(pEntity);
    columnsValues.getLongsMap().remove(ISrvOrm.VERSIONOLD_NAME);
    TableSql tableSql = this.tablesMap.get(pEntity.getClass().getSimpleName());
    columnsValues.getLongsMap().remove(tableSql.getIdName());
    int result = getSrvDatabase().executeUpdate(pEntity.getClass()
      .getSimpleName().toUpperCase(), columnsValues, pWhere);
    if (result != 1) {
      if (result == 0 && columnsValues.ifContains(ISrvOrm.VERSION_NAME)) {
        throw new ExceptionWithCode(ISrvDatabase.DIRTY_READ, "dirty_read");
      } else {
        String query = hlpInsertUpdate.evalSqlUpdate(pEntity.getClass()
          .getSimpleName().toUpperCase(), columnsValues,
            pWhere);
        throw new ExceptionWithCode(ISrvDatabase.ERROR_INSERT_UPDATE,
          "It should be 1 row updated but it was "
            + result + ", query:\n" + query);
      }
    }
  }

  /**
   * <p>Delete entity with condition, e.g. complex ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pWhere Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void deleteEntityWhere(
    final Class<T> pEntityClass, final String pWhere) throws Exception {
    if (pWhere == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    getSrvDatabase().executeDelete(pEntityClass.getSimpleName()
      .toUpperCase(), pWhere);
  }

  /**
   * <p>Delete entity with NON-COMPLEX ID from DB.</p>
   * @param <T> entity type
   * @param <K> ID type
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void deleteEntity(
    final T pEntity) throws Exception {
    TableSql tableSql = this.getTablesMap().get(pEntity.getClass()
      .getSimpleName());
    Field fieldId = this.utlReflection.retrieveField(pEntity.getClass(),
      tableSql.getIdName());
    fieldId.setAccessible(true);
    Object id = fieldId.get(pEntity);
    deleteEntity(pEntity.getClass(), id);
  }

  /**
   * <p>Delete entity from DB by given NON-COMPLEX ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void deleteEntity(
    final Class<T> pEntityClass, final Object pId) throws Exception {
    TableSql tableSql = this.getTablesMap().get(pEntityClass.getSimpleName());
    String idStr;
    if (pId instanceof String) {
      idStr = "'" + pId.toString() + "'";
    } else {
      idStr = pId.toString();
    }
    getSrvDatabase().executeDelete(pEntityClass.getSimpleName().toUpperCase(),
      tableSql.getIdName() + "=" + idStr);
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveList(
    final Class<T> pEntityClass) throws Exception {
    List<T> result = new ArrayList<T>();
    IRecordSet<RS> recordSet = null;
    try {
      String query = evalSqlSelect(pEntityClass) + ";\n";
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pEntityClass, recordSet));
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
   * <p>Retrieve a list of entities.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveListWithConditions(
    final Class<T> pEntityClass,
      final String pQueryConditions) throws Exception {
    if (pQueryConditions == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    List<T> result = new ArrayList<T>();
    IRecordSet<RS> recordSet = null;
    try {
      String query = evalSqlSelect(pEntityClass) + " "
          + pQueryConditions + ";\n";
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pEntityClass, recordSet));
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
   * @param pEntityClass entity class
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrievePage(
    final Class<T> pEntityClass,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    List<T> result = new ArrayList<T>();
    IRecordSet<RS> recordSet = null;
    try {
      String query = evalSqlSelect(pEntityClass)
          + " limit " + pPageSize + " offset " + pFirst + ";\n";
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pEntityClass, recordSet));
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
   * @param pEntityClass entity class
   * @param pQueryConditions not null e.g. "WHERE name='U1' ORDER BY id"
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrievePageWithConditions(
    final Class<T> pEntityClass, final String pQueryConditions,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    if (pQueryConditions == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    List<T> result = new ArrayList<T>();
    IRecordSet<RS> recordSet = null;
    try {
      String query = evalSqlSelect(pEntityClass) + " " + pQueryConditions
          + " limit " + pPageSize + " offset " + pFirst + ";\n";
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pEntityClass, recordSet));
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
   * @param pEntityClass entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final <T> Integer evalRowCount(
    final Class<T> pEntityClass) throws Exception {
    String query = "select count(*) as TOTALROWS from " + pEntityClass
      .getSimpleName().toUpperCase() + ";";
    return getSrvDatabase().evalIntegerResult(query, "TOTALROWS");
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final <T> Integer evalRowCountWhere(
    final Class<T> pEntityClass, final String pWhere) throws Exception {
    if (pWhere == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "param_null_not_accepted");
    }
    String query = "select count(*) as TOTALROWS from " + pEntityClass
      .getSimpleName().toUpperCase() + " where " + pWhere + ";";
    return getSrvDatabase().evalIntegerResult(query, "TOTALROWS");
  }

  /**
   * <p>Retrieve entity's owned lists (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity owned
   * @param pEntityClass owned e.g. org.model.InvoiceLine.class
   * @param pEntityOwner - Entity Owner e.g. an invoice
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveEntityOwnedlist(
    final Class<T> pEntityClass,
      final Object pEntityOwner) throws Exception {
    List<T> result = new ArrayList<T>();
    TableSql tableSqlOwner = getTablesMap().get(pEntityOwner.getClass()
      .getSimpleName());
    Field fieldIdOwner = getUtlReflection().retrieveField(pEntityOwner
      .getClass(), tableSqlOwner.getIdName());
    fieldIdOwner.setAccessible(true);
    Object idOwner = fieldIdOwner.get(pEntityOwner);
    String idOwnerStr;
    if (idOwner instanceof String) {
      idOwnerStr = "'" + idOwner + "'";
    } else {
      idOwnerStr = idOwner.toString();
    }
    String ownerFieldName = evalOwnerFieldName(pEntityClass,
      pEntityOwner.getClass());
    IRecordSet<RS> recordSet = null;
    try {
      String query = evalSqlSelect(pEntityClass) + " where "
        + pEntityClass.getSimpleName().toUpperCase() + "."
        + ownerFieldName.toUpperCase() + " = " + idOwnerStr + ";\n";
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pEntityClass, recordSet));
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
   * <p>Retrieve entity's owned lists (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity owned
   * @param pEntityClass owned e.g. org.model.InvoiceLine.class
   * @param pOwnerClass owner e.g. org.model.Invoice.class
   * @param pEntityOwnerId - Entity Owner ID e.g. an invoice ID
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retrieveEntityOwnedlist(final Class<T> pEntityClass,
    final Class<?> pOwnerClass, final Object pEntityOwnerId) throws Exception {
    List<T> result = new ArrayList<T>();
    String ownerFieldName = evalOwnerFieldName(pEntityClass, pOwnerClass);
    IRecordSet<RS> recordSet = null;
    try {
      String ownerIdStr;
      if (pEntityOwnerId instanceof String) {
        ownerIdStr = "'" + pEntityOwnerId + "'";
      } else {
        ownerIdStr = pEntityOwnerId.toString();
      }
      String query = evalSqlSelect(pEntityClass) + " where "
        + pEntityClass.getSimpleName().toUpperCase() + "."
        + ownerFieldName.toUpperCase() + "=" + ownerIdStr + ";\n";
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          result.add(retrieveEntity(pEntityClass, recordSet));
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
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
      if (!tableSql.getIsFullDefinedInXml()) {
        //make foreign keys alghorithm:
        Field[] fields = getUtlReflection().retrieveFields(clazz);
        for (Field field : fields) {
          FieldSql fieldSql = tableSql.getFieldsMap().get(field.getName());
          if (fieldSql != null) { //if persistable
            TableSql tableSqlForeign = this.tablesMap.get(field.getType()
              .getSimpleName());
            if (tableSqlForeign != null) {
              //if it's foreign entity according entities list ORDER
              //make sure ORDER of entities!!!
              fieldSql.setForeignEntity(field.getType().getSimpleName());
              String definition = tableSqlForeign.getIdDefinitionForeign();
              if (definition == null) {
                throw new ExceptionWithCode(ExceptionWithCode
                  .CONFIGURATION_MISTAKE, "There is no " + TableSql
                    .KEY_ID_DEFINITION_FOREIGN + " for " + clazz + " / "
                      + field.getName());
              }
              String isNullableStr = this.mngSettings.getFieldsSettings()
                .get(clazz.getCanonicalName()).get(field.getName())
                  .get("isNullable");
              if (!Boolean.valueOf(isNullableStr)
                && !definition.contains("not null")) {
                definition += " not null";
              }
              fieldSql.setForeignEntity(field.getType().getSimpleName());
              fieldSql.setDefinition(definition);
              //tableSql:
              String constraint = tableSql.getConstraint();
              String constraintFk = "constraint fk"
                + clazz.getSimpleName()
                + field.getType().getSimpleName() + field.getName()
                + " foreign key (" + field.getName().toUpperCase()
                + ") references " + field.getType().getSimpleName()
                  .toUpperCase()
                + " (" + tableSqlForeign.getIdName().toUpperCase() + ")";
              if (constraint != null) {
                constraint += ",\n" + constraintFk;
              } else {
                constraint = constraintFk;
              }
              tableSql.setConstraint(constraint);
            }
          }
        }
        String constraintAdd = this.mngSettings.getClassesSettings()
          .get(clazz.getCanonicalName()).get("constraintAdd");
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
  }

  /**
   * <p>Make TableSql from XML properties.</p>
   * @param pTableSql tableSql
   * @param pClazz entity class
   * @throws Exception - any exception
   **/
  public final void makeTableSqlFromXml(final TableSql pTableSql,
    final Class<?> pClazz) throws Exception {
    pTableSql.setVersionAlgorithm(Integer.parseInt(this.mngSettings
      .getClassesSettings().get(pClazz.getCanonicalName())
        .get(TableSql.KEY_VERSION_ALGORITHM)));
    pTableSql.setConstraint(this.mngSettings.getClassesSettings().get(pClazz
      .getCanonicalName()).get(TableSql.KEY_CONSTRAINT));
    pTableSql.setIdName(this.mngSettings.getClassesSettings().get(pClazz
      .getCanonicalName()).get(TableSql.KEY_ID_NAME));
    pTableSql.setIdDefinitionForeign(this.mngSettings.getClassesSettings()
      .get(pClazz.getCanonicalName()).get(TableSql.KEY_ID_DEFINITION_FOREIGN));
    pTableSql.setIsFullDefinedInXml("true".equals(this.mngSettings
      .getClassesSettings().get(pClazz.getCanonicalName())
        .get(TableSql.KEY_IF_FULL_DEFINE_IN_XML)));
    Field[] fields = getUtlReflection().retrieveFields(pClazz);
    for (Field field : fields) {
      if (this.mngSettings.getFieldsSettings().get(pClazz
        .getCanonicalName()) == null) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "There is no fields settings for class " + pClazz);
      }
      boolean isExcludedFromClass = false;
      String strExluded = this.mngSettings.getClassesSettings().get(pClazz
          .getCanonicalName()).get(MngSettings.KEY_EXCLUDED_FIELDS);
      if (strExluded != null && strExluded.contains(field.getName())) {
        isExcludedFromClass = true;
      }
      if (!isExcludedFromClass && this.mngSettings.getFieldsSettings()
        .get(pClazz.getCanonicalName()).get(field.getName()) != null) {
        FieldSql fieldSql = new FieldSql();
        String definition = this.mngSettings.getFieldsSettings().get(pClazz
          .getCanonicalName()).get(field.getName())
            .get(FieldSql.KEY_DEFINITION);
        if (definition != null) {
          String isNullableStr = this.mngSettings.getFieldsSettings().get(pClazz
            .getCanonicalName()).get(field.getName()).get("isNullable");
          if (!Boolean.valueOf(isNullableStr)
            && !definition.contains("not null")) {
            definition += " not null";
          }
        }
        fieldSql.setDefinition(definition);
        fieldSql.setForeignEntity(this.mngSettings.getFieldsSettings()
          .get(pClazz.getCanonicalName()).get(field.getName())
            .get(FieldSql.KEY_FOREIGN_ENTITY));
        pTableSql.getFieldsMap().put(field.getName(), fieldSql);
      }
    }
  }

  /**
   * <p>Retrieve entity from DB.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param recordSet record set
   * @return entity
   * @throws Exception - an exception
   **/
  public final <T> T retrieveEntity(
    final Class<T> pEntityClass,
      final IRecordSet<RS> recordSet) throws Exception {
    T entity = null;
    TableSql tableSql = getTablesMap().get(
      pEntityClass.getSimpleName());
    Constructor<T> constructor = pEntityClass.getDeclaredConstructor();
    entity = constructor.newInstance();
    for (Map.Entry<String, FieldSql> entry
      : tableSql.getFieldsMap().entrySet()) {
      String columnAlias = pEntityClass.getSimpleName().toUpperCase()
        + entry.getKey().toUpperCase();
      Field field = getUtlReflection().retrieveField(pEntityClass,
        entry.getKey());
      if (entry.getValue().getForeignEntity() != null) {
        TableSql tableSqlFrn = getTablesMap().
          get(entry.getValue().getForeignEntity());
        Constructor<?> constructorFrn = field.getType()
          .getDeclaredConstructor();
        Object entityFrn = constructorFrn.newInstance();
        for (String fldNameFrn
          : tableSqlFrn.getFieldsMap().keySet()) {
          String columnAliasFrn = entry.getKey()
            .toUpperCase() + fldNameFrn.toUpperCase();
          Field fieldFrn = getUtlReflection().retrieveField(field.getType(),
            fldNameFrn);
          TableSql tableSqlFrnFrn = getTablesMap().
            get(fieldFrn.getType().getSimpleName());
          if (tableSqlFrnFrn != null) {
            Constructor<?> constructorFrnFrn = fieldFrn.getType()
              .getDeclaredConstructor();
            Object entityFrnFrn = constructorFrnFrn.newInstance();
            Field fieldFrnFrn = getUtlReflection()
              .retrieveField(fieldFrn.getType(), tableSqlFrnFrn.getIdName());
            if (!fillSimpleField(fieldFrnFrn, entityFrnFrn, columnAliasFrn,
              recordSet)) {
              String msg = "3nd level. There is no rule to fill ID "
                + fieldFrnFrn.getName()
                  + " of " + fieldFrnFrn.getType() + " in "
                    + fieldFrn.getType();
              throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
                msg);
            }
            Object idFrnVal = fieldFrnFrn.get(entityFrnFrn);
            if (idFrnVal != null) {
              fieldFrn.setAccessible(true);
              fieldFrn.set(entityFrn, entityFrnFrn);
            }
          } else if (!fillSimpleField(fieldFrn, entityFrn, columnAliasFrn,
            recordSet)) {
            String msg = "2nd level. There is no rule to fill field "
              + fieldFrn.getName()
              + " of " + fieldFrn.getType() + " in " + field.getType();
            throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
              msg);
          }
        }
        Field fieldIdFrn = getUtlReflection()
          .retrieveField(entityFrn.getClass(), tableSqlFrn.getIdName());
        fieldIdFrn.setAccessible(true);
        Object idFrnVal = fieldIdFrn.get(entityFrn);
        if (idFrnVal != null) {
          field.setAccessible(true);
          field.set(entity, entityFrn);
        }
      } else if (!fillSimpleField(field, entity, columnAlias, recordSet)) {
        String msg = "There is no rule to fill field "
          + field.getName()
          + " of " + field.getType() + " in " + pEntityClass;
        throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED, msg);
      }
    }
    return entity;
  }

  /**
   * <p>Fill entity field with reflection if it is simple type.</p>
   * @param pField field
   * @param pEntity entity
   * @param pColumnAlias column alias
   * @param pRecordSet record set
   * @return boolean if field filled
   * @throws Exception an exception
   **/
  public final boolean fillSimpleField(final Field pField, final Object pEntity,
    final String pColumnAlias,
      final IRecordSet<RS> pRecordSet) throws Exception {
    pField.setAccessible(true);
    if (tryToFillIdable(pField, pEntity, pColumnAlias,
      pRecordSet, pField.getType())) {
      return true;
    }
    if (pField.getType() == Double.class) {
      pField.set(pEntity, getSrvRecordRetriever()
        .getDouble(pRecordSet.getRecordSet(), pColumnAlias));
    } else if (pField.getType() == Float.class) {
      pField.set(pEntity, getSrvRecordRetriever()
        .getFloat(pRecordSet.getRecordSet(), pColumnAlias));
    } else if (pField.getType() == BigDecimal.class) {
      pField.set(pEntity, getSrvRecordRetriever()
        .getBigDecimal(pRecordSet.getRecordSet(), pColumnAlias));
    } else if (pField.getType() == Date.class) {
      pField.set(pEntity, getSrvRecordRetriever()
        .getDate(pRecordSet.getRecordSet(), pColumnAlias));
    } else if (Enum.class.isAssignableFrom(pField.getType())) {
      Integer intVal = getSrvRecordRetriever()
        .getInteger(pRecordSet.getRecordSet(), pColumnAlias);
      Enum val = null;
      if (intVal != null) {
        val = (Enum) pField.getType().getEnumConstants()[intVal];
      }
      pField.set(pEntity, val);
    } else if (pField.getType() == Boolean.class) {
      pField.set(pEntity, getSrvRecordRetriever()
        .getBoolean(pRecordSet.getRecordSet(), pColumnAlias));
    } else {
      return false;
    }
    return true;
  }

  /**
   * <p>Fill entity field with reflection if it is simple,
   * ID allawed type.</p>
   * @param pField field
   * @param pEntity entity
   * @param pColumnAlias column alias
   * @param pRecordSet record set
   * @param pFieldType Field Type
   * @return boolean if field filled
   * @throws Exception an exception
   **/
  public final boolean tryToFillIdable(final Field pField, final Object pEntity,
    final String pColumnAlias,
      final IRecordSet<RS> pRecordSet,
        final Type pFieldType) throws Exception {
    if (Integer.class == pFieldType) {
      pField.set(pEntity, getSrvRecordRetriever()
      .getInteger(pRecordSet.getRecordSet(), pColumnAlias));
      return true;
    } else if (Long.class == pFieldType) {
      pField.set(pEntity, getSrvRecordRetriever()
      .getLong(pRecordSet.getRecordSet(), pColumnAlias));
      return true;
    } else if (String.class == pFieldType) {
      String strVal = getSrvRecordRetriever()
      .getString(pRecordSet.getRecordSet(), pColumnAlias);
      getLogger().debug(ASrvOrm.class,
        "SrvOrm: fill field/value: " + pColumnAlias + "/" + strVal);
      pField.set(pEntity, strVal);
      return true;
    }
    return false;
  }

  /**
   * <p>Evaluate Android compatible values map of given entity.
   * For "itsVersion" evaluate new one and set it in the entity,
   * old version stored in column "itsVersionOld".
   * Used in INSERT/UPDATE statement.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @return ColumnsValues type-safe map fieldName-fieldValue
   * @throws Exception an exception
   **/
  public final <T> ColumnsValues
    evalColumnsValuesAndFillNewVersion(
      final T pEntity) throws Exception {
    ColumnsValues columnsValues = new ColumnsValues();
    TableSql tableSql = getTablesMap().get(
      pEntity.getClass().getSimpleName());
    columnsValues.setIdName(tableSql.getIdName());
    for (String fieldName : tableSql.getFieldsMap().keySet()) {
      try {
        Field field = getUtlReflection().retrieveField(pEntity.getClass(),
          fieldName);
        field.setAccessible(true);
        TableSql tableSqlForeign = this.tablesMap.get(field.getType()
          .getSimpleName());
        if (tableSqlForeign != null) {
          Object foreignEntity = field.get(pEntity);
          Field fieldId = getUtlReflection().retrieveField(field.getType(),
            tableSqlForeign.getIdName());
          getLogger().debug(ASrvOrm.class, "ORM: try to fill column FID "
            + fieldName + " with " + foreignEntity + " in "
              + pEntity.getClass().getSimpleName() + " type PID: "
                + fieldId.getType());
          if (foreignEntity == null) {
            Type fieldType = fieldId.getType();
            if (fieldType == Integer.class) {
              Integer value = null;
              columnsValues.put(fieldName, value);
            } else if (fieldType == Long.class) {
              Long value = null;
              columnsValues.put(fieldName, value);
            } else if (fieldType == String.class) {
              String value = null;
              columnsValues.put(fieldName, value);
            } else {
              String msg =
                "There is no rule to fill column foreign id value from field "
                  + fieldName + " of " + field.getType() + " in " + pEntity;
              throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
                msg);
            }
          } else {
            fieldId.setAccessible(true);
            if (!tryToFillColumnIdable(columnsValues, fieldName, fieldId,
              foreignEntity, fieldId.getType())) {
              String msg =
                "There is no rule to fill column foreign id value from field "
                  + fieldName + " of " + field.getType() + " in " + pEntity;
              throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
                msg);
            }
          }
        } else if (!fillSimpleColumnAndFillNewVersion(
          columnsValues, field, pEntity)) {
          String msg = "There is no rule to fill column value from field "
            + fieldName
            + " of " + field.getType() + " in " + pEntity;
          throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
            msg);
        }
      } catch (ExceptionWithCode e) {
        throw e;
      } catch (Exception e) {
        String msg = "Can't fill field SQL from "
          + fieldName + " in " + pEntity;
        Exception exc = new Exception(msg);
        exc.setStackTrace(e.getStackTrace());
        throw exc;
      }
    }
    return columnsValues;
  }

  /**
   * <p>Fill Android compatible values map of given entity and field.
   * For "itsVersion" evaluate new one and set it in the entity,
   * old version stored in column "itsVersionOld".
   * Used in INSERT/UPDATE statement.</p>
   * @param <T> entity type
   * @param pColumnsValues type-safe map fieldName-fieldValue
   * @param pEntity entity
   * @param pField field reflection
   * @return if filled
   * @throws Exception an exception
   **/
  public final <T> boolean fillSimpleColumnAndFillNewVersion(
    final ColumnsValues pColumnsValues,
    final Field pField, final T pEntity) throws Exception {
    if (ISrvOrm.VERSION_NAME.equals(pField.getName())) {
      TableSql tableSql = getTablesMap().get(
        pEntity.getClass().getSimpleName());
      Long valueLngOld = (Long) pField.get(pEntity);
      Long valueLngNew = null;
      if (tableSql.getVersionAlgorithm() == 1) {
        valueLngNew = new Date().getTime();
      } else {
        if (valueLngOld == null) {
          valueLngNew = 1L;
        } else {
          valueLngNew = valueLngOld + 1;
        }
      }
      pColumnsValues.put(pField.getName(), valueLngNew);
      pColumnsValues.put(ISrvOrm.VERSIONOLD_NAME, valueLngOld);
      pField.set(pEntity, valueLngNew);
      return true;
    }
    if (tryToFillColumnIdable(pColumnsValues, pField.getName(), pField, pEntity,
        pField.getType())) {
      return true;
    } else if (pField.getType() == Float.class) {
      Float value = (Float) pField.get(pEntity);
      pColumnsValues.put(pField.getName(), value);
      return true;
    } else if (pField.getType() == Double.class) {
      Double value = (Double) pField.get(pEntity);
      pColumnsValues.put(pField.getName(), value);
      return true;
    } else if (pField.getType() == BigDecimal.class) {
      BigDecimal valueBigDecimal = (BigDecimal) pField.get(pEntity);
      Double value;
      if (valueBigDecimal == null) {
        value = null;
      } else {
        value = valueBigDecimal.doubleValue();
      }
      pColumnsValues.put(pField.getName(), value);
      return true;
    } else if (pField.getType() == Date.class) {
      Date valueDate = (Date) pField.get(pEntity);
      Long value;
      if (valueDate == null) {
        value = null;
      } else {
        value = valueDate.getTime();
      }
      pColumnsValues.put(pField.getName(), value);
      return true;
    } else if (pField.getType() == Boolean.class) {
      Boolean valueBool = (Boolean) pField.get(pEntity);
      Integer value;
      if (valueBool) {
        value = 1;
      } else {
        value = 0;
      }
      pColumnsValues.put(pField.getName(), value);
      return true;
    } else if (Enum.class.isAssignableFrom(pField.getType())) {
      Enum valEn = (Enum) pField.get(pEntity);
      Integer value = null;
      if (valEn != null) {
        value = valEn.ordinal();
      }
      pColumnsValues.put(pField.getName(), value);
      return true;
    }
    return false;
  }

  /**
   * <p>Fill Android compatible values map of given entity and field
   * of ID allowed type.</p>
   * @param <T> entity type
   * @param pColumnsValues type-safe map fieldName-fieldValue
   * @param pColumnName Column Name
   * @param pField field reflection
   * @param pEntity entity
   * @param pFieldType field type
   * @return if filled
   * @throws Exception an exception
   **/
  public final <T> boolean tryToFillColumnIdable(
    final ColumnsValues pColumnsValues, final String pColumnName,
      final Field pField, final T pEntity,
        final Type pFieldType) throws Exception {
    if (Integer.class == pFieldType) {
      Integer valInt = (Integer) pField.get(pEntity);
      pColumnsValues.put(pColumnName, valInt);
      return true;
    } else if (Long.class == pFieldType) {
      Long valLn = (Long) pField.get(pEntity);
      pColumnsValues.put(pColumnName, valLn);
      return true;
    } else if (String.class == pFieldType) {
      String valStr = (String) pField.get(pEntity);
      if (valStr != null && this.isNeedsToSqlEscape) {
        valStr = srvSqlEscape.escape(valStr);
      }
      pColumnsValues.put(pColumnName, valStr);
      return true;
    }
    return false;
  }

  /**
   * <p>Return owner field name in owned entity e.g. "itsInvoice"
   * in invoiceLine.</p>
   * @param pEntityOwnedClass Entity Owned Class
   * @param pEntityOwnerClass Entity Owner Class
   * @return owner field name
   * @throws Exception an exception
   **/
  public final String evalOwnerFieldName(
    final Class<?> pEntityOwnedClass,
      final Class<?> pEntityOwnerClass) throws Exception {
    TableSql tableSqlEod = getTablesMap().get(pEntityOwnedClass
      .getSimpleName());
    String ownerFieldName = null;
    for (Map.Entry<String, FieldSql> entry : tableSqlEod.getFieldsMap()
      .entrySet()) {
      if (pEntityOwnerClass
      .getSimpleName().equals(entry.getValue().getForeignEntity())) {
        ownerFieldName = entry.getKey();
      }
    }
    if (ownerFieldName == null) {
      String msg = "Can't find owner field name for subentity/entity: "
        + pEntityOwnedClass + "/" + pEntityOwnerClass;
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE, msg);
    }
    return ownerFieldName;
  }

  /**
   * <p>Init database birth and ID birth of entity.</p>
   * @param pEntity entity
   * @throws Exception an exception
   **/
  public final void initPersistableBase(final Object pEntity) throws Exception {
    Field fieldIdDbBirth = getUtlReflection().retrieveField(pEntity.getClass(),
      APersistableBase.ID_DATABASE_BIRTH_NAME);
    fieldIdDbBirth.setAccessible(true);
    if (getSrvDatabase() != null) { //for test purpose, otherwise it must be set
      fieldIdDbBirth.set(pEntity, getSrvDatabase().getIdDatabase());
    }
    Field fieldIdBirth = getUtlReflection().retrieveField(pEntity.getClass(),
      APersistableBase.ID_BIRTH_NAME);
    fieldIdBirth.setAccessible(true);
    fieldIdBirth.set(pEntity, null);
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
    String whereVer = "";
    if (pColumnsValues.ifContains(ISrvOrm.VERSION_NAME)) {
      whereVer = " and ITSVERSION="
        + pColumnsValues.evalSqlValue(ISrvOrm.VERSIONOLD_NAME);
    }
    String idName = getTablesMap().get(pEntity.getClass()
      .getSimpleName()).getIdName();
    return idName.toUpperCase() + "="
      + pColumnsValues.evalSqlValue(idName) + whereVer;
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
   * @return if all tables has neen created or some added
   * @throws Exception - an exception
   **/
  public final boolean initializeDatabase() throws Exception {
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
      String dirPath = File.separator;
      if (this.propertiesBase.getDirectory() != null) {
        dirPath = File.separator + this.propertiesBase.getDirectory()
          + File.separator;
      }
      String initRdbms = this.mngSettings.getAppSettings().get("initRdbms");
      if (initRdbms != null) {
        String initSql = loadString(dirPath + initRdbms);
        if (initSql != null) {
          getLogger().info(ASrvOrm.class, dirPath
            + initRdbms + " found, try to execute.");
          for (String initSingle : initSql.split("\n")) {
            if (initSingle.trim().length() > 1 && !initSingle.startsWith("/")) {
              srvDatabase.executeQuery(initSingle);
            }
          }
        } else {
          getLogger().info(ASrvOrm.class, dirPath + initRdbms + " not found.");
        }
      }
      if (ifAllTablesCreated) {
        getLogger().info(ASrvOrm.class, "all tables has been created.");
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setDatabaseVersion(1);
        Double randomDbl = Math.random() * 1000000000;
        databaseInfo.setDatabaseId(randomDbl.intValue());
        databaseInfo.setDescription("a database");
        insertEntity(databaseInfo);
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
        Integer nestVersion = srvDatabase.getVersionDatabase() + 1;
        String upgradeSqlName = "upgrade_" + nestVersion + ".sql";
        String upgradeSql = loadString(dirPath + upgradeSqlName);
        if (upgradeSql != null) {
          getLogger().info(ASrvOrm.class, dirPath
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
        } else {
          getLogger().info(ASrvOrm.class, dirPath
            + upgradeSqlName + " not found.");
        }
      }
    } finally {
      srvDatabase.releaseResources(); //close connection
    }
    return ifCreatedOrAdded;
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
    for (String fieldName : tableSql.getFieldsMap().keySet()) {
      if (isFirstField) {
        isFirstField = false;
      } else {
        result.append(",\n");
      }
      result.append(fieldName.toUpperCase() + " "
        + tableSql.getFieldsMap().get(fieldName).getDefinition());
    }
    if (tableSql.getConstraint() != null) {
      result.append(",\n" + tableSql.getConstraint());
    }
    result.append(");\n");
    return result.toString();
  }

  /**
   * <p>Evaluate common(without WHERE) SQL SELECT
   * statement for entity type.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @return String SQL DML query
   * @throws Exception - an exception
   **/
  public final <T> String evalSqlSelect(
    final Class<T> pEntityClass) throws Exception {
    String tableName = pEntityClass.getSimpleName().toUpperCase();
    StringBuffer result =
      new StringBuffer("select ");
    StringBuffer joints =
      new StringBuffer();
    boolean isFirstField = true;
    TableSql tableSql = getTablesMap().get(pEntityClass.getSimpleName());
    int idx = 0;
    for (Map.Entry<String, FieldSql> entry
      : tableSql.getFieldsMap().entrySet()) {
      if (isFirstField) {
        isFirstField = false;
      } else {
        result.append(", ");
      }
      if (idx++ == 2) {
        result.append("\n");
        idx = 0;
      }
      if (entry.getValue().getForeignEntity() == null) {
          String columnName = tableName + entry.getKey().toUpperCase();
          result.append(tableName + "."
            + entry.getKey().toUpperCase() + " as " + columnName);
      } else {
        TableSql foreignTableSql = getTablesMap()
          .get(entry.getValue().getForeignEntity());
        String tableForeign = entry.getValue().getForeignEntity().toUpperCase();
        String fieldFrAndTbFrAlias = entry.getKey().toUpperCase();
        joints.append(" left join " + tableForeign + " as "
          + fieldFrAndTbFrAlias + " on " + tableName + "."
          + fieldFrAndTbFrAlias + "=" + fieldFrAndTbFrAlias
          + "." + foreignTableSql.getIdName().toUpperCase());
        boolean isForFirstField = true;
        for (Map.Entry<String, FieldSql> entryFor
          : foreignTableSql.getFieldsMap().entrySet()) {
          if (isForFirstField) {
            isForFirstField = false;
          } else {
            result.append(", ");
          }
          if (idx++ == 2) {
            result.append("\n");
            idx = 0;
          }
          String columnForName = fieldFrAndTbFrAlias
            + entryFor.getKey().toUpperCase();
          result.append(fieldFrAndTbFrAlias + "."
            + entryFor.getKey().toUpperCase() + " as " + columnForName);
        }
      }
    }
    result.append("\nfrom " + tableName + joints);
    return result.toString();
  }

  /**
   * <p>Evaluate SQL SELECT statement for the entity with ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @return String SQL DML query
   * @throws Exception - an exception
   **/
  public final <T> String evalSqlSelect(
    final Class<T> pEntityClass,
      final Object pId) throws Exception {
    TableSql tableSql = this.getTablesMap().get(pEntityClass.getSimpleName());
    String idStr;
    if (pId instanceof String) {
      idStr = "'" + pId.toString() + "'";
    } else {
      idStr = pId.toString();
    }
    return evalSqlSelect(pEntityClass)
      + " where " + pEntityClass.getSimpleName().toUpperCase()
        + "." + tableSql.getIdName().toUpperCase() + "="
          + idStr + ";\n";
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
   * <p>Getter for isNeedsToSqlEscape.</p>
   * @return boolean
   **/
  public final boolean getIsNeedsToSqlEscape() {
    return this.isNeedsToSqlEscape;
  }

  /**
   * <p>Setter for isNeedsToSqlEscape.</p>
   * @param pIsNeedsToSqlEscape reference
   **/
  public final void setIsNeedsToSqlEscape(final boolean pIsNeedsToSqlEscape) {
    this.isNeedsToSqlEscape = pIsNeedsToSqlEscape;
  }

  /**
   * <p>Geter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final UtlReflection getUtlReflection() {
    return this.utlReflection;
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
   * <p>Get recordset retriever.</p>
   * @return recordset retriever
   **/
  public final ISrvRecordRetriever<RS> getSrvRecordRetriever() {
    return this.srvRecordRetriever;
  }

  /**
   * <p>Set recordset retriever.</p>
   * @param pSrvRecordRetriever recordset retriever
   **/
  public final void setSrvRecordRetriever(
    final ISrvRecordRetriever<RS> pSrvRecordRetriever) {
    this.srvRecordRetriever = pSrvRecordRetriever;
  }

  /**
   * <p>Geter for srvSqlEscape.</p>
   * @return ISrvSqlEscape
   **/
  public final ISrvSqlEscape getSrvSqlEscape() {
    return this.srvSqlEscape;
  }

  /**
   * <p>Setter for srvSqlEscape.</p>
   * @param pSrvSqlEscape reference
   **/
  public final void setSrvSqlEscape(final ISrvSqlEscape pSrvSqlEscape) {
    this.srvSqlEscape = pSrvSqlEscape;
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
   * @return MngSettings
   **/
  public final MngSettings getMngSettings() {
    return this.mngSettings;
  }

  /**
   * <p>Setter for mngSettings.</p>
   * @param pMngSettings reference/value
   **/
  public final void setMngSettings(final MngSettings pMngSettings) {
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
