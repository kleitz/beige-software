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
 * <p>Abstraction of service that clear current database
 * then get identical copy of another one
 * (exclude tables with authentication User/Role/Tomcat/Jetty)
 * through given file or network connection.</p>
 *
 * @author Yury Demidenko
 */
public interface IClearDbThenGetAnotherCopy {

  /**
   * <p>It will clear current database then copy
   * data from another through given file or network connection.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  void clearDbThenGetAnotherCopy(
    Map<String, Object> pAddParam) throws Exception;
}
