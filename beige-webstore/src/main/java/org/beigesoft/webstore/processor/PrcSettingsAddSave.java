package org.beigesoft.webstore.processor;

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
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.webstore.persistable.SettingsAdd;
import org.beigesoft.webstore.service.ISrvSettingsAdd;

/**
 * <p>Service that save SettingsAdd into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcSettingsAddSave<RS>
  implements IEntityProcessor<SettingsAdd, Long> {

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
    srvSettingsAdd.saveSettingsAdd(pAddParam, pEntity);
    pRequestData.setAttribute("tradingSettings", srvSettingsAdd
      .lazyGetSettingsAdd(pAddParam));
    pRequestData.setAttribute("accSettings",
      this.srvAccSettings.lazyGetAccSettings(pAddParam));
    return pEntity;
  }


  //Simple getters and setters:
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
