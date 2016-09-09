package org.beigesoft.web.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import javax.servlet.http.HttpServletRequest;
import org.beigesoft.log.ILogger;

/**
 * <p>Abstraction of service that fill entity from pequest.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvWebMvc {

  /**
   * <p>Fill entity from pequest.</p>
   * @param <T> entity type
   * @param pEntity Entity to fill
   * @param pReq - servlet request
   * @throws Exception - an exception
   **/
  <T> void fillEntity(T pEntity, HttpServletRequest pReq) throws Exception;

  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  ILogger getLogger();

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  void setLogger(ILogger pLogger);
}
