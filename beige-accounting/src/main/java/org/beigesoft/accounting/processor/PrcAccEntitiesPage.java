package org.beigesoft.accounting.processor;

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
import org.beigesoft.service.IProcessor;
import org.beigesoft.accounting.service.ISrvAccSettings;

/**
 * <p>Service that retrieve accounting entities page.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcAccEntitiesPage<RS> implements IProcessor {

  /**
   * <p>Process Entities Page delegate.</p>
   **/
  private IProcessor prcEntitiesPage;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Process entity request.</p>
   * @param pAddParam additional param
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pAddParam,
    final IRequestData pRequestData) throws Exception {
    this.prcEntitiesPage.process(pAddParam, pRequestData);
    pRequestData.setAttribute("accSettings",
      this.srvAccSettings.lazyGetAccSettings(pAddParam));
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prcEntitiesPage.</p>
   * @return IProcessor
   **/
  public final IProcessor getPrcEntitiesPage() {
    return this.prcEntitiesPage;
  }

  /**
   * <p>Setter for prcEntitiesPage.</p>
   * @param pPrcEntitiesPage reference
   **/
  public final void setPrcEntitiesPage(
    final IProcessor pPrcEntitiesPage) {
    this.prcEntitiesPage = pPrcEntitiesPage;
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
}
