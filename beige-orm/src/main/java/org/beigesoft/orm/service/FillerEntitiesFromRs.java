package org.beigesoft.orm.service;

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
import java.util.Collection;
import java.lang.reflect.Field;

import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.converter.IConverterByName;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.log.ILogger;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.ETypeField;
import org.beigesoft.orm.model.TableSql;

/**
 * <p>Service that fill object (entity)
 * from a from SQL result-set (JBDC or Android).
 * Compliance with Beige-ORM specification #4.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FillerEntitiesFromRs<RS>
  implements IFillerObjectsFrom<IRecordSet<RS>> {

  /**
   * <p>Tables SQL desccriptors map "EntitySimpleName - TableSql".
   * Ordered cause creating tables is order dependent.</p>
   **/
  private Map<String, TableSql> tablesMap;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFactoryAppBeansByClass<IFillerObjectFields<?>> fillersFieldsFactory;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterByName<IRecordSet<RS>, ?>>
    convertersFieldsFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Fields RAPI holder.</p>
   **/
  private IHolderForClassByName<Field> fieldsRapiHolder;

  /**
   * <p>Fill object's fields from record-set.</p>
   * @param <T> object (entity) type
   * @param pAddParam additional param, e.g. neededFieldNames -set
   * of needed fields names
   * @param pEntity Object to fill
   * @param pSource Source
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pAddParam,
    final T pEntity, final IRecordSet<RS> pSource) throws Exception {
    @SuppressWarnings("unchecked")
    IFillerObjectFields<T> filler = (IFillerObjectFields<T>)
      fillersFieldsFactory.lazyGet(pAddParam, pEntity.getClass());
    TableSql tableSql = this.tablesMap.get(pEntity.getClass().getSimpleName());
    @SuppressWarnings("unchecked")
    Collection<String> fieldsNames =
      (Collection<String>) pAddParam.get("neededFieldNames");
    if (fieldsNames == null) {
      fieldsNames = tableSql.getFieldsMap().keySet();
    }
    boolean isShowDbMsg = this.logger.getIsShowDebugMessagesFor(getClass());
    int dbgDetLev = this.logger.getDetailLevel();
    for (String fieldName : fieldsNames) {
      if (!tableSql.getFieldsMap().get(fieldName).getTypeField()
            .equals(ETypeField.DERIVED_FROM_COMPOSITE)) {
        String convName = this.fieldConverterNamesHolder.getFor(pEntity
         .getClass(), fieldName);
        IConverterByName<IRecordSet<RS>, ?> conv = this.convertersFieldsFatory
          .lazyGet(pAddParam, convName);
        if (conv != null) { // e.g. transient or composite (non-foreign) itsId
          try {
            Field field = this.fieldsRapiHolder
              .getFor(pEntity.getClass(), fieldName);
            String parName;
            String tableAlias;
            Integer currentLevel = (Integer) pAddParam.get("currentLevel");
            if (currentLevel != null && currentLevel > 1) {
              String currForeignFieldNm = (String) pAddParam
               .get("foreignFieldNmL" + (currentLevel - 1));
              tableAlias = currForeignFieldNm.toUpperCase();
            } else {
              tableAlias = "";
            }
            if (Enum.class.isAssignableFrom(field.getType())) {
              parName = tableAlias + fieldName.toUpperCase();
              pAddParam.put("fieldClass", field.getType());
            } else {
              if (tableSql.getFieldsMap().get(fieldName)
                .getForeignEntity() != null) {
                parName = fieldName;
                // foreign entity class for factory:
                if (currentLevel == null) {
                  currentLevel = 1;
                }
                pAddParam.put("foreignFieldNmL" + currentLevel, fieldName);
                pAddParam.put("fieldClass", field.getType());
                pAddParam.put("entityClass", pEntity.getClass());
                String deepRestName = "deepLevel";
                Integer deepLevel = (Integer) pAddParam.get(deepRestName);
                if (deepLevel == null) {
                  deepLevel = 2; // reveal through 2-nd level by default
                  pAddParam.put(deepRestName, deepLevel);
                }
              } else {
                parName = tableAlias + fieldName.toUpperCase();
              }
            }
            Object fieldVal = conv.convert(pAddParam, pSource, parName);
            if (isShowDbMsg && dbgDetLev > 10) {
              this.logger.debug(null, FillerEntitiesFromRs.class,
                "Converted from RS field/type/converter/value: " + fieldName
                  + "/" + field.getType().getSimpleName() + "/"
                    + conv.getClass().getSimpleName() + "/" + fieldVal);
            }
            filler.fill(pAddParam, pEntity, fieldVal, fieldName);
          } catch (Exception ex) {
            String msg = "Can't fill field/class: " + fieldName + "/"
              + pEntity.getClass().getCanonicalName();
            throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
              msg, ex);
          }
        }
      }
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
   * <p>Getter for convertersFieldsFatory.</p>
   * @return IFactoryAppBeansByName<IConverterByName<IRecordSet<RS>, ?>>
   **/
  public final IFactoryAppBeansByName<IConverterByName<IRecordSet<RS>, ?>>
    getConvertersFieldsFatory() {
    return this.convertersFieldsFatory;
  }

  /**
   * <p>Setter for convertersFieldsFatory.</p>
   * @param pConvertersFieldsFatory reference
   **/
  public final void setConvertersFieldsFatory(
    final IFactoryAppBeansByName<IConverterByName<IRecordSet<RS>, ?>>
      pConvertersFieldsFatory) {
    this.convertersFieldsFatory = pConvertersFieldsFatory;
  }

  /**
   * <p>Getter for fieldConverterNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldConverterNamesHolder() {
    return this.fieldConverterNamesHolder;
  }

  /**
   * <p>Setter for fieldConverterNamesHolder.</p>
   * @param pFieldConverterNamesHolder reference
   **/
  public final void setFieldConverterNamesHolder(
    final IHolderForClassByName<String> pFieldConverterNamesHolder) {
    this.fieldConverterNamesHolder = pFieldConverterNamesHolder;
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
}
