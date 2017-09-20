package org.beigesoft.replicator.service;

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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.persistable.APersistableBaseVersion;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Service to synchronize APersistableBaseVersion (itsVersion changed time).
 * It's untransactional service. Transaction must be started.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvEntitySyncPersistableBaseVersion<RS> implements ISrvEntitySync {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>
   * Synchronize  APersistableBaseVersion (itsVersion changed time).
   * </p>
   * @param pAddParam additional params
   * @param pEntity object
   * @return isNew if entity exist in database (need update)
   * @throws Exception - an exception
   **/
  @Override
  public final boolean sync(final Map<String, Object> pAddParam,
    final Object pEntity) throws Exception {
    APersistableBaseVersion entityPb = (APersistableBaseVersion) pEntity;
    int currDbId = getSrvOrm().getIdDatabase();
    if (currDbId == entityPb.getIdDatabaseBirth()) {
      throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
        "Foreign entity born in this database! {ID, ID BIRTH, DB BIRTH}:"
          + " {" + entityPb.getItsId() + ", " + entityPb.getIdBirth()
            + "," + entityPb.getIdDatabaseBirth());
    }
    String tblNm = pEntity.getClass().getSimpleName().toUpperCase();
    String whereStr = " where " + tblNm + ".IDBIRTH=" + entityPb.getItsId()
      + " and " + tblNm + ".IDDATABASEBIRTH=" + entityPb.getIdDatabaseBirth();
    APersistableBaseVersion entityPbDb = getSrvOrm()
      .retrieveEntityWithConditions(pAddParam, entityPb.getClass(), whereStr);
    entityPb.setIdBirth(entityPb.getItsId());
    entityPb.setItsId(null);
    boolean isNew = true;
    if (entityPbDb != null) {
      entityPb.setItsVersion(entityPbDb.getItsVersion());
      entityPb.setItsId(entityPbDb.getItsId());
      isNew = false;
    }
    return isNew;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
}
