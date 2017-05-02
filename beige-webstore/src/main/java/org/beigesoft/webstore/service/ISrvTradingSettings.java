package org.beigesoft.webstore.service;

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

import org.beigesoft.webstore.persistable.TradingSettings;

/**
 * <p>Trading settings service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvTradingSettings {

  /**
   * <p>Retrieve/get Trading settings.</p>
   * @param pAddParam additional param
   * @return Trading settings
   * @throws Exception - an exception
   **/
  TradingSettings lazyGetTradingSettings(
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Clear Trading settings to retrieve from
   * database new version.</p>
   * @param pAddParam additional param
   **/
  void clearTradingSettings(Map<String, Object> pAddParam);

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  void saveTradingSettings(Map<String, Object> pAddParam,
      TradingSettings pEntity) throws Exception;
}
