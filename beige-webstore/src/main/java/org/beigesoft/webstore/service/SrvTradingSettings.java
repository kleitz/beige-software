package org.beigesoft.webstore.service;

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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.webstore.persistable.TradingSettings;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for trading settings.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvTradingSettings<RS>
  implements ISrvTradingSettings {

  /**
   * <p>Current TradingSettings.</p>
   **/
  private TradingSettings tradingSettings;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvTradingSettings() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   **/
  public SrvTradingSettings(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Retrieve/get Trading settings.</p>
   * @param pAddParam additional param
   * @return Trading settings
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized TradingSettings lazyGetTradingSettings(
    final Map<String, Object> pAddParam) throws Exception {
    if (this.tradingSettings == null) {
      this.tradingSettings = getSrvOrm()
        .retrieveEntityById(pAddParam, TradingSettings.class, 1L);
      if (this.tradingSettings == null) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "There is no trading settings!!!");
      }
    }
    return this.tradingSettings;
  }

  /**
   * <p>Clear Trading settings to retrieve from
   * database new version.</p>
   * @param pAddParam additional param
   **/
  @Override
  public final synchronized void clearTradingSettings(
    final Map<String, Object> pAddParam) {
    this.tradingSettings = null;
  }

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void saveTradingSettings(
    final Map<String, Object> pAddParam,
      final TradingSettings pEntity) throws Exception {
    if (pEntity.getIsNew()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "insert_not_allowed::" + pAddParam.get("user"));
    } else if (pEntity.getItsId() != 1L) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "id_not_allowed::" + pAddParam.get("user"));
    } else {
      getSrvOrm().updateEntity(pAddParam, pEntity);
      this.tradingSettings = null;
      lazyGetTradingSettings(pAddParam);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
}
