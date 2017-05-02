package org.beigesoft.accounting.service;

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

import org.beigesoft.accounting.persistable.AccSettings;

/**
 * <p>Accounting settings service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvAccSettings {

  /**
   * <p>Retrieve/get Accounting settings.</p>
   * @param pAddParam additional param
   * @return Accounting settings
   * @throws Exception - an exception
   **/
  AccSettings lazyGetAccSettings(
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Clear Accounting settings to retrieve from
   * database new version.</p>
   * @param pAddParam additional param
   **/
  void clearAccSettings(Map<String, Object> pAddParam);

  /**
   * <p>Save acc-settings into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  void saveAccSettings(Map<String, Object> pAddParam,
      AccSettings pEntity) throws Exception;
}
