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

/**
 * <p>Service to synchronize entity (that just read) with entity in database.
 * Usually just return isNew. For APersistableBase it must fill
 * properly {itsId, idBirth and idDatabaseBirth}.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvEntitySync {

  /**
   * <p>
   * Synchronize entity (that just read) with entity in database.
   * Usually just return isNew. For APersistableBase it must fill
   * properly {itsId, idBirth and idDatabaseBirth}.
   * </p>
   * @param pAddParam additional params
   * @param pEntity object
   * @return isNew if entity exist in database (need update)
   * @throws Exception - an exception
   **/
  boolean sync(Map<String, Object> pAddParam,
    Object pEntity) throws Exception;
}
