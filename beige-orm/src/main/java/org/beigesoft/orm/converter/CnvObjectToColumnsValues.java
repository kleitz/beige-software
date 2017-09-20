package org.beigesoft.orm.converter;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.beigesoft.log.ILogger;
import org.beigesoft.converter.IConverter;
import org.beigesoft.converter.IConverterIntoByName;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.model.ETypeField;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.FieldSql;

/**
 * <p>Generic converter of any entity into ColumnValues.
 * </p>
 *
 * @param <T> type of object
 * @author Yury Demidenko
 */
public class CnvObjectToColumnsValues<T>
  implements IConverter<T, ColumnsValues> {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private Map<String, TableSql> tablesMap;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>>
    fieldsConvertersFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldsConvertersNamesHolder;

  /**
   * <p>Object class.</p>
   **/
  private Class<T> objectClass;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>Convert to column values.</p>
   * @param pAddParam additional params, expected "isOnlyId"-not null for
   * converting only ID field.
   * @param pObject entity
   * @return ColumnsValues Columns Values
   * @throws Exception - an exception
   **/
  @Override
  public final ColumnsValues convert(final Map<String, Object> pAddParam,
    final T pObject) throws Exception {
    ColumnsValues result = new ColumnsValues();
    TableSql tableSql = this.tablesMap.get(this.objectClass.getSimpleName());
    result.setIdColumnsNames(tableSql.getIdColumnsNames());
    if (pAddParam.get("isOnlyId") != null) {
      if (tableSql.getIdColumnsNames().length > 0 && tableSql.getFieldsMap()
          .get(tableSql.getIdColumnsNames()[0]).getTypeField()
            .equals(ETypeField.DERIVED_FROM_COMPOSITE)) {
          // it's also composite foreign ID
        for (Map.Entry<String, FieldSql> entry : tableSql
          .getFieldsMap().entrySet()) {
          if (entry.getValue().getTypeField()
            .equals(ETypeField.COMPOSITE_FK_PK)) {
            convertField(pAddParam, entry.getKey(), pObject,
              true, result, tableSql);
            break;
          }
        }
      } else { // simple or foreign entity or composite ID (not also foreign):
        for (String idFldNm : tableSql.getIdColumnsNames()) {
          convertField(pAddParam, idFldNm, pObject, true, result, tableSql);
        }
      }
    } else if (pAddParam.get("fieldsNames") != null) {
      String[] fieldsNames = (String[]) pAddParam.get("fieldsNames");
      for (String fldNm : fieldsNames) {
        convertField(pAddParam, fldNm, pObject,
          false, result, tableSql);
      }
    } else {
      for (Map.Entry<String, FieldSql> entry : tableSql
        .getFieldsMap().entrySet()) {
        if (!entry.getValue().getTypeField()
          .equals(ETypeField.DERIVED_FROM_COMPOSITE)) {
          // Foreign entity with composite ID will be represented
          // as several ID fields, e.g. {itsName='admin', itsRole='role'}.
          convertField(pAddParam, entry.getKey(), pObject,
            false, result, tableSql);
        }
      }
    }
    return result;
  }

  /**
   * <p>Convert field to column values.</p>
   * @param pAddParam additional params, expected "isOnlyId"-not null for
   * converting only ID field.
   * @param pFieldName Field Name
   * @param pObject entity
   * @param pIsItId is retrieve only ID
   * @param pResult Columns Values
   * @param pTableSql Table Sql
   * @throws Exception - an exception
   **/
  public final void convertField(final Map<String, Object> pAddParam,
    final String pFieldName, final T pObject, final boolean pIsItId,
      final ColumnsValues pResult, final TableSql pTableSql) throws Exception {
    Method getter = this.gettersRapiHolder
      .getFor(this.objectClass, pFieldName);
    Object fieldVal = getter.invoke(pObject);
    boolean isShowDbMsg = this.logger.getIsShowDebugMessagesFor(getClass());
    int dbgDetLev = this.logger.getDetailLevel();
    if (fieldVal == null) {
      // PK = null means autogenerated for INSERT operation
      // so INSERT must no contains PK
      boolean isPk = false;
      if (pIsItId) {
        isPk = true;
      }
      if (pTableSql.getFieldsMap().get(pFieldName).getTypeField()
        .equals(ETypeField.DERIVED_FROM_COMPOSITE)) {
        isPk = true;
      }
      for (String idFldNm : pTableSql.getIdColumnsNames()) {
        if (idFldNm.equals(pFieldName)) {
          isPk = true;
          break;
        }
      }
      if (isPk && isShowDbMsg) {
        getLogger().debug(null, CnvObjectToColumnsValues.class,
          "PK is null for CV field/object - "
            + pFieldName + "/" + pObject);
        return;
      }
    }
    Map<String, Object> addParam = pAddParam;
    if (addParam == null) {
      addParam = new Hashtable<String, Object>();
    }
    Field field = this.fieldsRapiHolder
      .getFor(this.objectClass, pFieldName);
    // for foreign entity nullable field:
    addParam.put("fromClass", field.getType());
    String cnvrtName = this.fieldsConvertersNamesHolder
      .getFor(this.objectClass, pFieldName);
    if (isShowDbMsg && dbgDetLev > 10) {
      getLogger().debug(null, CnvObjectToColumnsValues.class,
        "Attempt to convert into CV field/type/value/converter - "
          + pFieldName + "/" + field.getType().getSimpleName()
            + "/" + fieldVal + "/" + cnvrtName);
    }
    @SuppressWarnings("unchecked")
    IConverterIntoByName<Object, ColumnsValues> confFld =
      (IConverterIntoByName<Object, ColumnsValues>) this.fieldsConvertersFatory
        .lazyGet(null, cnvrtName);
    confFld.convert(addParam, fieldVal, pResult, pFieldName);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for logger.</p>
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
   * <p>Getter for fieldsConvertersFatory.</p>
   * @return IFactoryAppBeansByName<IConverter<?, ?>>
   **/
  public final IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>>
    getFieldsConvertersFatory() {
    return this.fieldsConvertersFatory;
  }

  /**
   * <p>Setter for fieldsConvertersFatory.</p>
   * @param pFieldsConvertersFatory reference
   **/
  public final void setFieldsConvertersFatory(
    final IFactoryAppBeansByName<IConverterIntoByName<?, ColumnsValues>>
      pFieldsConvertersFatory) {
    this.fieldsConvertersFatory = pFieldsConvertersFatory;
  }

  /**
   * <p>Getter for fieldsConvertersNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldsConvertersNamesHolder() {
    return this.fieldsConvertersNamesHolder;
  }

  /**
   * <p>Setter for fieldsConvertersNamesHolder.</p>
   * @param pFieldsConvertersNamesHolder reference
   **/
  public final void setFieldsConvertersNamesHolder(
    final IHolderForClassByName<String> pFieldsConvertersNamesHolder) {
    this.fieldsConvertersNamesHolder = pFieldsConvertersNamesHolder;
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
   * <p>Getter for objectClass.</p>
   * @return Class<T>
   **/
  public final Class<T> getObjectClass() {
    return this.objectClass;
  }

  /**
   * <p>Setter for objectClass.</p>
   * @param pObjectClass reference
   **/
  public final void setObjectClass(final Class<T> pObjectClass) {
    this.objectClass = pObjectClass;
  }

  /**
   * <p>Getter for gettersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getGettersRapiHolder() {
    return this.gettersRapiHolder;
  }

  /**
   * <p>Setter for gettersRapiHolder.</p>
   * @param pGettersRapiHolder reference
   **/
  public final void setGettersRapiHolder(
    final IHolderForClassByName<Method> pGettersRapiHolder) {
    this.gettersRapiHolder = pGettersRapiHolder;
  }
}
