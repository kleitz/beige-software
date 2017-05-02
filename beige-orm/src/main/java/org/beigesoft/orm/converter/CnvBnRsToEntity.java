package org.beigesoft.orm.converter;

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
import java.util.Hashtable;
import java.lang.reflect.Field;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.converter.IConverterByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.TableSql;

/**
 * <p>Converter field from JDBC result-set to Entity.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class CnvBnRsToEntity<RS> implements
  IConverterByName<IRecordSet<RS>, Object> {

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private Map<String, TableSql> tablesMap;

  /**
   * <p>Entitie's factories factory.</p>
   **/
  private IFactoryAppBeansByClass<IFactorySimple<?>> entitiesFactoriesFatory;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFactoryAppBeansByClass<IFillerObjectFields<?>> fillersFieldsFactory;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Filler Objects From IRecordSet.</p>
   **/
  private IFillerObjectsFrom<IRecordSet<RS>> fillerObjectsFromRs;

  /**
   * <p>Convert RS to entity.</p>
   * @param pAddParam additional params, e.g. entity class UserRoleTomcat
   * to reveal derived columns for its composite ID, or field Enum class
   * to reveal Enum value by index.
   * @param pFrom from a bean
   * @param pName by a name
   * @return pTo to a bean
   * @throws Exception - an exception
   **/
  @Override
  public final Object convert(final Map<String, Object> pAddParam,
    final IRecordSet<RS> pFrom, final String pName) throws Exception {
    Class fieldClass = (Class) pAddParam.get("fieldClass");
    Class entityClass = (Class) pAddParam.get("entityClass");
    Integer deepLevel = (Integer) pAddParam.get("deepLevel");
    String currentLevelName = "currentLevel";
    Integer currentLevel = (Integer) pAddParam.get(currentLevelName);
    if (currentLevel == null) {
      currentLevel = 1;
      pAddParam.put(currentLevelName, currentLevel);
    }
    Object entity = convertOnlyId(pAddParam, pFrom, pName, fieldClass,
      entityClass, currentLevel, deepLevel);
    // it's may be enter into recursion:
    if (entity != null && currentLevel < deepLevel) {
      currentLevel++;
      pAddParam.put(currentLevelName, currentLevel);
      fillerObjectsFromRs.fill(pAddParam, entity, pFrom);
      // exit form recursion:
      currentLevel--;
      pAddParam.put(currentLevelName, currentLevel);
    }
    return entity;
  }

  /**
   * <p>Convert RS to entity (fill only ID).</p>
   * @param pAddParam additional params, e.g. entity class UserRoleTomcat
   * to reveal derived columns for its composite ID, or field Enum class
   * to reveal Enum value by index.
   * @param pFrom from a bean
   * @param pName by a name
   * @param pFieldClass field class
   * @param pEntityClass entity class
   * @param pCurrentLevel Current Level
   * @param pDeepLevel Deep Level
   * @return pTo to a bean
   * @throws Exception - an exception
   **/
  public final Object convertOnlyId(final Map<String, Object> pAddParam,
    final IRecordSet<RS> pFrom, final String pName, final Class pFieldClass,
      final Class pEntityClass, final Integer pCurrentLevel,
        final Integer pDeepLevel) throws Exception {
    // foreign entity's ID map
    Map<String, Object> idNmValMap = new Hashtable<String, Object>();
    TableSql tableSql = this.tablesMap.get(pFieldClass.getSimpleName());
    for (String idFldNm : tableSql.getIdColumnsNames()) {
      Object idVal;
      Field rapiFld = this.fieldsRapiHolder.getFor(pFieldClass, idFldNm);
      String columnAlias;
      if (pCurrentLevel == 1) {
        // e.g. PREPAYMENTTO - ACCCASH.ITSID as ACCCASHITSID
        columnAlias = pName.toUpperCase() + idFldNm.toUpperCase();
      } else {
        // there is only ID of foreign entity in SELECT query:
        // e.g. PERSISTABLELINE
        // PERSISTABLEHEAD.ITSDEPARTMENT as PERSISTABLEHEADITSDEPARTMENT
        String currForeignFieldNm = (String) pAddParam
         .get("foreignFieldNmL" + (pCurrentLevel - 1));
        String tableAlias = currForeignFieldNm.toUpperCase();
        if (tableSql.getIdColumnsNames().length > 1) {
          // composite derived names:
          columnAlias = tableAlias + idFldNm.toUpperCase();
        } else {
          columnAlias = tableAlias + pName.toUpperCase();
        }
      }
      if (tableSql.getFieldsMap().get(idFldNm)
          .getForeignEntity() != null) {
        // foreign entity again, e.g. UserTomcat in
        // UserRoleTomcatPriority.userRoleTomcat.itsUser
        TableSql tableSqlFr = this.tablesMap
          .get(rapiFld.getType().getSimpleName());
        if (tableSqlFr.getIdColumnsNames().length > 1 || tableSqlFr
          .getFieldsMap().get(tableSqlFr.getIdColumnsNames()[0])
            .getForeignEntity() != null) {
          String msg = "There is no rule to fill foreign2 ID - "
              + rapiFld.getType();
          throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
            msg);
        }
        Field rapiFldFr = this.fieldsRapiHolder
          .getFor(rapiFld.getType(), tableSqlFr.getIdFieldName());
        Object idEntFr = getSimpleId(rapiFldFr.getType(), pFrom, columnAlias);
        if (idEntFr != null) {
          @SuppressWarnings("unchecked")
          IFactorySimple<Object> facEnFr =
            (IFactorySimple<Object>) this.entitiesFactoriesFatory
             .lazyGet(pAddParam, rapiFld.getType());
          idVal = facEnFr.create(pAddParam);
          @SuppressWarnings("unchecked")
          IFillerObjectFields<Object> filler =
            (IFillerObjectFields<Object>)
              fillersFieldsFactory.lazyGet(pAddParam, rapiFld.getType());
          filler.fill(pAddParam, idVal, idEntFr,
            tableSqlFr.getIdColumnsNames()[0]);
        } else {
          idVal = null;
        }
      } else {
        // simple ID
        idVal = getSimpleId(rapiFld.getType(), pFrom, columnAlias);
      }
      if (idVal != null) {
        idNmValMap.put(idFldNm, idVal);
      }
    }
    if (idNmValMap.size() > 0) {
      @SuppressWarnings("unchecked")
      IFactorySimple<Object> facEn =
        (IFactorySimple<Object>) this.entitiesFactoriesFatory
          .lazyGet(pAddParam, pFieldClass);
      Object entity = facEn.create(pAddParam);
      @SuppressWarnings("unchecked")
      IFillerObjectFields<Object> filler =
        (IFillerObjectFields<Object>)
          fillersFieldsFactory.lazyGet(pAddParam, pFieldClass);
      // e.g. UserRoleTomcatPriority.userRoleTomcat:
      // {itsRole - String='role1', itsUser - UserTomcat {itsUser='admin'}}
      for (Map.Entry<String, Object> entry : idNmValMap.entrySet()) {
        filler.fill(pAddParam, entity, entry.getValue(), entry.getKey());
      }
      return entity;
    }
    return null;
  }

  /**
   * <p>Get from RS simple ID-able value.</p>
   * @param pFieldType Field Type
   * @param pFrom from RS
   * @param pColumnAlias Column Alias
   * @return simple ID value
   * @throws Exception - an exception
   **/
  public final Object getSimpleId(final Class<?> pFieldType,
    final IRecordSet<RS> pFrom, final String pColumnAlias) throws Exception {
    if (Integer.class == pFieldType) {
      return pFrom.getInteger(pColumnAlias);
    } else if (Long.class == pFieldType) {
      return pFrom.getLong(pColumnAlias);
    } else if (String.class == pFieldType) {
      return pFrom.getString(pColumnAlias);
    } else {
      String msg =
        "There is no rule to get column ID-able "
          + pColumnAlias + " of " + pFieldType;
      throw new ExceptionWithCode(ExceptionWithCode.NOT_YET_IMPLEMENTED,
        msg);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for tablesMap.</p>
   * @return Map<String, TableSql>
   **/
  public final Map<String, TableSql> getTablesMap() {
    return this.tablesMap;
  }

  /**
   * <p>Setter for tablesMap.</p>
   * @param pTablesMap reference
   **/
  public final void setTablesMap(final Map<String, TableSql> pTablesMap) {
    this.tablesMap = pTablesMap;
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
    final IFactoryAppBeansByClass<IFactorySimple<?>>
      pEntitiesFactoriesFatory) {
    this.entitiesFactoriesFatory = pEntitiesFactoriesFatory;
  }

  /**
   * <p>Getter for fillersFieldsFactory.</p>
   * @return IFactoryAppBeansByClass<IFillerObjectFields<?>>
   **/
  public final IFactoryAppBeansByClass<IFillerObjectFields<?>>
    getFillersFieldsFactory() {
    return this.fillersFieldsFactory;
  }

  /**
   * <p>Setter for fillersFieldsFactory.</p>
   * @param pFillersFieldsFactory reference
   **/
  public final void setFillersFieldsFactory(
    final IFactoryAppBeansByClass<IFillerObjectFields<?>>
      pFillersFieldsFactory) {
    this.fillersFieldsFactory = pFillersFieldsFactory;
  }

  /**
   * <p>Getter for fieldsRapiHolder.</p>
   * @return IHolderForClassByName<Field>
   **/
  public final IHolderForClassByName<Field> getFieldsRapiHolder() {
    return this.fieldsRapiHolder;
  }

  /**
   * <p>Setter for fieldsRapiHolder.</p>
   * @param pFieldsRapiHolder reference
   **/
  public final void setFieldsRapiHolder(
    final IHolderForClassByName<Field> pFieldsRapiHolder) {
    this.fieldsRapiHolder = pFieldsRapiHolder;
  }

  /**
   * <p>Getter for fillerObjectsFromRs.</p>
   * @return IFillerObjectsFrom<IRecordSet<RS>>
   **/
  public final IFillerObjectsFrom<IRecordSet<RS>> getFillerObjectsFromRs() {
    return this.fillerObjectsFromRs;
  }

  /**
   * <p>Setter for fillerObjectsFromRs.</p>
   * @param pFillerObjectsFromRs reference
   **/
  public final void setFillerObjectsFromRs(
    final IFillerObjectsFrom<IRecordSet<RS>> pFillerObjectsFromRs) {
    this.fillerObjectsFromRs = pFillerObjectsFromRs;
  }
}
