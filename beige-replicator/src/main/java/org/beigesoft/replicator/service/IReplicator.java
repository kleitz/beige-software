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
 * <p>Abstraction of service that replicate data
 * from requested(source) database to requesting(destination) database.</p>
 *
 * @author Yury Demidenko
 */
public interface IReplicator {

  /**
   * <p>It will replicate data from requested(source) database
   * to requesting(destination) database.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  void replicate(
    Map<String, Object> pAddParam) throws Exception;
}
