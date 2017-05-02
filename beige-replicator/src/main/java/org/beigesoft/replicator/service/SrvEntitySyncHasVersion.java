package org.beigesoft.replicator.service;

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

import org.beigesoft.model.IHasVersion;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Service to synchronize IHasVersion.
 * It's untransactional service. Transaction must be started.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvEntitySyncHasVersion<RS> implements ISrvEntitySync {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>
   * Synchronize IHasVersion.
   * </p>
   * @param pAddParam additional params
   * @param pEntity object
   * @return isNew if entity exist in database (need update)
   * @throws Exception - an exception
   **/
  @Override
  public final boolean sync(final Map<String, Object> pAddParam,
    final Object pEntity) throws Exception {
    IHasVersion entityPb = (IHasVersion) pEntity;
    IHasVersion entityPbDb = getSrvOrm().retrieveEntity(pAddParam, entityPb);
    boolean isNew = true;
    if (entityPbDb != null) {
      entityPb.setItsVersion(entityPbDb.getItsVersion());
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
