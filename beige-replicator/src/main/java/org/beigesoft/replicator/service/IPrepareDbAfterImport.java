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
 * <p>Abstraction of service prepares database after import.
 * For Postgresql it's needs to reset auto-incremented ID sequences.</p>
 *
 * @author Yury Demidenko
 */
public interface IPrepareDbAfterImport {

  /**
   * <p>It prepares database after import.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  void prepareDbAfterImport(Map<String, Object> pAddParam) throws Exception;
}
