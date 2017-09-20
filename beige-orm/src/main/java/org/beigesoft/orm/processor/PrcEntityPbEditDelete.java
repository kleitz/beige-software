package org.beigesoft.orm.processor;

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

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IRequestData;
import org.beigesoft.persistable.IPersistableBase;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.service.ISrvDate;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.service.ASrvOrm;
import org.beigesoft.orm.model.FieldSql;
import org.beigesoft.orm.model.TableSql;

/**
 * <p>Service that retrieve entity, check if it foreign
 * and put into request data for farther editing or confirm delete.
 * Use PrcEntityRetrieve for printing.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class PrcEntityPbEditDelete<RS, T extends IPersistableBase>
  implements IEntityProcessor<T, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private ASrvOrm<RS> srvOrm;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private IMngSettings mngUvdSettings;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFactoryAppBeansByClass<IFillerObjectFields<?>> fillersFieldsFactory;

  /**
   * <p>Entities factories factory.</p>
   **/
  private IFactoryAppBeansByClass<IFactorySimple<?>> entitiesFactoriesFatory;

  /**
   * <p>Properties service.</p>
   **/
  private UtlProperties utlProperties;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDate srvDate;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterToFromString<?>>
    convertersFieldsFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Process entity request.</p>
   * @param pAddParam additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(
    final Map<String, Object> pAddParam,
      final T pEntity, final IRequestData pRequestData) throws Exception {
    T entity = this.srvOrm.retrieveEntity(pAddParam, pEntity);
    if (entity.getIdDatabaseBirth() != getSrvOrm().getIdDatabase()) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "can_not_change_foreign_src");
    }
    entity.setIsNew(false);
    pRequestData.setAttribute("entity", entity);
    pRequestData.setAttribute("mngUvds", this.mngUvdSettings);
    pRequestData.setAttribute("srvOrm", this.srvOrm);
    pRequestData.setAttribute("srvDate", this.srvDate);
    pRequestData.setAttribute("hldCnvFtfsNames",
      this.fieldConverterNamesHolder);
    pRequestData.setAttribute("fctCnvFtfs", this.convertersFieldsFatory);
    // owned lists:
    String ownedLists = mngUvdSettings.getClassesSettings()
      .get(pEntity.getClass()).get("ownedLists");
    if (ownedLists != null) {
      Map<Class<?>, List<Object>> ownedListsMap =
        new LinkedHashMap<Class<?>, List<Object>>();
      pRequestData.setAttribute("ownedListsMap", ownedListsMap);
      Set<String> ownedListsSet = utlProperties
        .evalPropsStringsSet(ownedLists);
      for (String className : ownedListsSet) {
        Class<?> entityFolClass = Class.forName(className);
        @SuppressWarnings("unchecked")
        IFillerObjectFields<Object> fillerEfol = (IFillerObjectFields<Object>)
          this.fillersFieldsFactory.lazyGet(pAddParam, entityFolClass);
        // find owner field name:
        String ownerFieldName = null;
        TableSql tableSqlEfol = this.srvOrm.getTablesMap()
          .get(entityFolClass.getSimpleName());
        for (Map.Entry<String, FieldSql> entry : tableSqlEfol
          .getFieldsMap().entrySet()) {
          if (pEntity.getClass().getSimpleName()
            .equals(entry.getValue().getForeignEntity())) {
            ownerFieldName = entry.getKey();
          }
        }
        if (ownerFieldName == null) {
          throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
            "Can't find owner field name for class/class from owned list: "
              + pEntity.getClass() + "/" + entityFolClass);
        }
        @SuppressWarnings("unchecked")
        IFactorySimple<Object> entFolFac = (IFactorySimple<Object>)
          this.entitiesFactoriesFatory.lazyGet(pAddParam, entityFolClass);
        Object entityFol = entFolFac.create(pAddParam);
        fillerEfol.fill(pAddParam, entityFol, entity, ownerFieldName);
        List<Object> entities = this.srvOrm
          .retrieveListForField(pAddParam, entityFol, ownerFieldName);
        ownedListsMap.put(entityFolClass, entities);
      }
    }
    return entity;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ASrvOrm<RS>
   **/
  public final ASrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ASrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for mngUvdSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngUvdSettings() {
    return this.mngUvdSettings;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngUvdSettings reference
   **/
  public final void setMngUvdSettings(final IMngSettings pMngUvdSettings) {
    this.mngUvdSettings = pMngUvdSettings;
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
   * <p>Geter for utlProperties.</p>
   * @return UtlProperties
   **/
  public final UtlProperties getUtlProperties() {
    return this.utlProperties;
  }

  /**
   * <p>Setter for utlProperties.</p>
   * @param pUtlProperties reference
   **/
  public final void setUtlProperties(final UtlProperties pUtlProperties) {
    this.utlProperties = pUtlProperties;
  }

  /**
   * <p>Getter for srvDate.</p>
   * @return ISrvDate
   **/
  public final ISrvDate getSrvDate() {
    return this.srvDate;
  }

  /**
   * <p>Setter for srvDate.</p>
   * @param pSrvDate reference
   **/
  public final void setSrvDate(final ISrvDate pSrvDate) {
    this.srvDate = pSrvDate;
  }

  /**
   * <p>Getter for convertersFieldsFatory.</p>
   * @return IFactoryAppBeansByName<IConverterToFromString<?>>
   **/
  public final IFactoryAppBeansByName<IConverterToFromString<?>>
    getConvertersFieldsFatory() {
    return this.convertersFieldsFatory;
  }

  /**
   * <p>Setter for convertersFieldsFatory.</p>
   * @param pConvertersFieldsFatory reference
   **/
  public final void setConvertersFieldsFatory(
    final IFactoryAppBeansByName<IConverterToFromString<?>>
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
}
