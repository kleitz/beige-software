package org.beigesoft.handler;

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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IRequestData;
import org.beigesoft.model.IHasId;
import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.service.IProcessor;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Handler for entity requests like "create, update, delete, list".
 * According "Beigesoft business logic programming".
 * All requests requires transaction.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HandlerEntityRequest<RS> implements IHandlerRequest {

  /**
   * <p>Database service.</p>
   */
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Service that fill entity from request.</p>
   **/
  private IFillerObjectsFrom<IRequestData> fillEntityFromReq;

  /**
   * <p>Entities factories factory.</p>
   **/
  private IFactoryAppBeansByClass<IFactorySimple<?>> entitiesFactoriesFatory;

  /**
   * <p>Entities processors factory.</p>
   **/
  private IFactoryAppBeansByName<IEntityProcessor> entitiesProcessorsFactory;

  /**
   * <p>Entities processors names holder.</p>
   **/
  private IHolderForClassByName<String> entitiesProcessorsNamesHolder;

  /**
   * <p>Processors factory.</p>
   **/
  private IFactoryAppBeansByName<IProcessor> processorsFactory;

  /**
   * <p>Processors names holder.</p>
   **/
  private IHolderForClassByName<String> processorsNamesHolder;

  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entitiesMap;

  /**
   * <p>Handle request.</p>
   * @param pRequestData Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(
    final IRequestData pRequestData) throws Exception {
    try {
      String[] actionsArr = pRequestData
        .getParameter("nmsAct").split(",");
      String nmEnt = pRequestData.getParameter("nmEnt");
      Class entityClass = this.entitiesMap.get(nmEnt);
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      Hashtable<String, Object> addParam = new Hashtable<String, Object>();
      IHasId<?> entity = null;
      if (actionsArr[0].startsWith("entity")) {
        // actions like "save", "delete"
        @SuppressWarnings("unchecked")
        IFactorySimple<IHasId<?>> entFac = (IFactorySimple<IHasId<?>>)
          this.entitiesFactoriesFatory.lazyGet(addParam, entityClass);
        entity = entFac.create(addParam);
        this.fillEntityFromReq.fill(addParam, entity, pRequestData);
      }
      for (String actionNm : actionsArr) {
        if (actionNm.startsWith("entity")) {
          if (entity == null) { // it's may be change entity to owner:
           entity = (IHasId<?>) addParam.get("nextEntity");
           if (entity == null) {
              throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
                "wrong_request_entity_not_filled");
            }
           entityClass = entity.getClass();
          }
          String entProcNm = this.entitiesProcessorsNamesHolder
            .getFor(entityClass, actionNm);
          @SuppressWarnings("unchecked")
          IEntityProcessor<IHasId<?>, ?> ep =
            (IEntityProcessor<IHasId<?>, ?>)
            this.entitiesProcessorsFactory.lazyGet(addParam, entProcNm);
          entity = ep.process(addParam, entity, pRequestData);
        } else { // else actions like "list" (page)
          String procNm = this.processorsNamesHolder
            .getFor(entityClass, actionNm);
          IProcessor proc = this.processorsFactory
            .lazyGet(addParam, procNm);
          proc.process(addParam, pRequestData);
        }
      }
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
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
   * <p>Getter for fillEntityFromReq.</p>
   * @return IFillerObjectsFrom<IRequestData>
   **/
  public final IFillerObjectsFrom<IRequestData> getFillEntityFromReq() {
    return this.fillEntityFromReq;
  }

  /**
   * <p>Setter for fillEntityFromReq.</p>
   * @param pFillEntityFromReq reference
   **/
  public final void setFillEntityFromReq(
    final IFillerObjectsFrom<IRequestData> pFillEntityFromReq) {
    this.fillEntityFromReq = pFillEntityFromReq;
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
   * <p>Getter for entitiesProcessorsFactory.</p>
   * @return IFactoryAppBeansByName<IEntityProcessor>
   **/
  public final IFactoryAppBeansByName<IEntityProcessor>
    getEntitiesProcessorsFactory() {
    return this.entitiesProcessorsFactory;
  }

  /**
   * <p>Setter for entitiesProcessorsFactory.</p>
   * @param pEntitiesProcessorsFactory reference
   **/
  public final void setEntitiesProcessorsFactory(
    final IFactoryAppBeansByName<IEntityProcessor>
      pEntitiesProcessorsFactory) {
    this.entitiesProcessorsFactory = pEntitiesProcessorsFactory;
  }

  /**
   * <p>Getter for entitiesProcessorsNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String>
    getEntitiesProcessorsNamesHolder() {
    return this.entitiesProcessorsNamesHolder;
  }

  /**
   * <p>Setter for entitiesProcessorsNamesHolder.</p>
   * @param pEntitiesProcessorsNamesHolder reference
   **/
  public final void setEntitiesProcessorsNamesHolder(
    final IHolderForClassByName<String> pEntitiesProcessorsNamesHolder) {
    this.entitiesProcessorsNamesHolder = pEntitiesProcessorsNamesHolder;
  }

  /**
   * <p>Getter for processorsFactory.</p>
   * @return IFactoryAppBeansByName<IProcessor>
   **/
  public final IFactoryAppBeansByName<IProcessor> getProcessorsFactory() {
    return this.processorsFactory;
  }

  /**
   * <p>Setter for processorsFactory.</p>
   * @param pProcessorsFactory reference
   **/
  public final void setProcessorsFactory(
    final IFactoryAppBeansByName<IProcessor> pProcessorsFactory) {
    this.processorsFactory = pProcessorsFactory;
  }

  /**
   * <p>Getter for processorsNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getProcessorsNamesHolder() {
    return this.processorsNamesHolder;
  }

  /**
   * <p>Setter for processorsNamesHolder.</p>
   * @param pProcessorsNamesHolder reference
   **/
  public final void setProcessorsNamesHolder(
    final IHolderForClassByName<String> pProcessorsNamesHolder) {
    this.processorsNamesHolder = pProcessorsNamesHolder;
  }

  /**
   * <p>Getter for entitiesMap.</p>
   * @return Map<String, Class<?>>
   **/
  public final Map<String, Class<?>> getEntitiesMap() {
    return this.entitiesMap;
  }

  /**
   * <p>Setter for entitiesMap.</p>
   * @param pEntitiesMap reference
   **/
  public final void setEntitiesMap(final Map<String, Class<?>> pEntitiesMap) {
    this.entitiesMap = pEntitiesMap;
  }
}
