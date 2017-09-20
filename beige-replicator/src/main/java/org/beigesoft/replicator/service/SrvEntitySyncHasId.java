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
   * @param pAddParam additional params
   * @param pEntity object
   * @return isNew if entity exist in database (need update)
   * @throws Exception - an exception
   **/
  @Override
  public final boolean sync(final Map<String, Object> pAddParam,
    final Object pEntity) throws Exception {
    Object entityPbDb = getSrvOrm().retrieveEntity(pAddParam, pEntity);
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
