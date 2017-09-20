package org.beigesoft.service;

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
