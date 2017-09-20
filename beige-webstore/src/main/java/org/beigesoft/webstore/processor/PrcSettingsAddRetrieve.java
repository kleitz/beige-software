package org.beigesoft.webstore.processor;

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
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.webstore.persistable.SettingsAdd;
import org.beigesoft.webstore.service.ISrvSettingsAdd;

/**
 * <p>Service that retrieve SettingsAdd from DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcSettingsAddRetrieve<RS>
  implements IEntityProcessor<SettingsAdd, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private IMngSettings mngUvdSettings;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Business service for trading settings.</p>
   **/
  private ISrvSettingsAdd srvSettingsAdd;

  /**
   * <p>Process entity request.</p>
   * @param pAddParam additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final SettingsAdd process(
    final Map<String, Object> pAddParam,
      final SettingsAdd pEntity,
        final IRequestData pRequestData) throws Exception {
    SettingsAdd entity = srvSettingsAdd
      .lazyGetSettingsAdd(pAddParam);
    pRequestData.setAttribute("tradingSettings", srvSettingsAdd
      .lazyGetSettingsAdd(pAddParam));
    pRequestData.setAttribute("accSettings",
      this.srvAccSettings.lazyGetAccSettings(pAddParam));
    pRequestData.setAttribute("entity", entity);
    pRequestData.setAttribute("mngUvds", this.mngUvdSettings);
    pRequestData.setAttribute("srvOrm", this.srvOrm);
    return entity;
  }


  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
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

  /**
   * <p>Getter for mngUvdSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngUvdSettings() {
    return this.mngUvdSettings;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngUvdSettings reference
   **/
  public final void setMngUvdSettings(final IMngSettings pMngUvdSettings) {
    this.mngUvdSettings = pMngUvdSettings;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final void setSrvAccSettings(final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }

  /**
   * <p>Getter for srvSettingsAdd.</p>
   * @return ISrvSettingsAdd
   **/
  public final ISrvSettingsAdd getSrvSettingsAdd() {
    return this.srvSettingsAdd;
  }

  /**
   * <p>Setter for srvSettingsAdd.</p>
   * @param pSrvSettingsAdd reference
   **/
  public final void setSrvSettingsAdd(
    final ISrvSettingsAdd pSrvSettingsAdd) {
    this.srvSettingsAdd = pSrvSettingsAdd;
  }
}
