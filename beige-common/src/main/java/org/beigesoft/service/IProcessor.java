package org.beigesoft.service;

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

import org.beigesoft.model.IRequestData;

/**
 * <p>Abstraction of service that process a request.</p>
 *
 * @author Yury Demidenko
 */
public interface IProcessor {

  /**
   * <p>Process request.</p>
   * @param pAddParam additional param
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  void process(Map<String, Object> pAddParam,
    IRequestData pRequestData) throws Exception;
}
