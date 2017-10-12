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
import java.io.OutputStream;

import org.beigesoft.model.IRequestData;
import org.beigesoft.model.IHasId;
import org.beigesoft.service.IFillerObjectsFrom;
import org.beigesoft.service.IEntityFileReporter;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Handler that makes file-report entity, e.g. PDF.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndlEntityFileReportReq<RS> implements IHndlFileReportReq {

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
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entitiesMap;

  /**
   * <p>Entities file-reporter factory.</p>
   **/
  private IFactoryAppBeansByName<IEntityFileReporter>
    fctEntitiesFileReporters;

  /**
   * <p>Handle request.</p>
   * @param pRequestData Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final IRequestData pRequestData,
    final OutputStream pSous) throws Exception {
    try {
      String nmEnt = pRequestData.getParameter("nmEnt");
      Class entityClass = this.entitiesMap.get(nmEnt);
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      Hashtable<String, Object> addParam = new Hashtable<String, Object>();
      IHasId<?> entity = null;
      @SuppressWarnings("unchecked")
      IFactorySimple<IHasId<?>> entFac = (IFactorySimple<IHasId<?>>)
        this.entitiesFactoriesFatory.lazyGet(addParam, entityClass);
      entity = entFac.create(addParam);
      this.fillEntityFromReq.fill(addParam, entity, pRequestData);
      String reporterNm = pRequestData.getParameter("reporterNm");
      @SuppressWarnings("unchecked")
      IEntityFileReporter<IHasId<?>, ?> efr =
        (IEntityFileReporter<IHasId<?>, ?>)
          this.fctEntitiesFileReporters.lazyGet(addParam, reporterNm);
      efr.makeReport(addParam, entity, pRequestData, pSous);
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

  /**
   * <p>Getter for fctEntitiesFileReporters.</p>
   * @return IFactoryAppBeansByName<IEntityFileReporter>
   **/
  public final IFactoryAppBeansByName<IEntityFileReporter>
    getFctEntitiesFileReporters() {
    return this.fctEntitiesFileReporters;
  }

  /**
   * <p>Setter for fctEntitiesFileReporters.</p>
   * @param pFctEntitiesFileReporters reference
   **/
  public final void setFctEntitiesFileReporters(
    final IFactoryAppBeansByName<IEntityFileReporter>
      pFctEntitiesFileReporters) {
    this.fctEntitiesFileReporters = pFctEntitiesFileReporters;
  }
}
