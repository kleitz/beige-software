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

import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Service to synchronize entity (that just read) with entity in database.
 * It just check if it's new.
 * It's untransactional service. Transaction must be started.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvEntitySyncHasId<RS> implements ISrvEntitySync {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>
   * Synchronize entity (that just read) with entity in database.
   * It just check if it's new.
   * </p>
   * @param pEntity object
   * @param pAddParam additional params
   * @return isNew if entity exist in database (need update)
   * @throws Exception - an exception
   **/
  @Override
  public final boolean sync(final Object pEntity,
    final Map<String, Object> pAddParam) throws Exception {
    Object entityPbDb = getSrvOrm().retrieveEntity(pEntity);
    return entityPbDb == null;
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
