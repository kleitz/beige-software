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
