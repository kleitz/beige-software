package org.beigesoft.orm.processor;

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
import org.beigesoft.service.IProcessor;
import org.beigesoft.orm.service.ISrvEntitiesPage;

/**
 * <p>Service that retrieve entities page.</p>
 *
 * @author Yury Demidenko
 */
public class PrcEntitiesPage implements IProcessor {

  /**
   * <p>Page service.</p>
   **/
  private ISrvEntitiesPage srvEntitiesPage;

  /**
   * <p>Process entity request.</p>
   * @param pAddParam additional param
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pAddParam,
    final IRequestData pRequestData) throws Exception {
    this.srvEntitiesPage.retrievePage(pAddParam, pRequestData);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvEntitiesPage.</p>
   * @return ISrvEntitiesPage
   **/
  public final ISrvEntitiesPage getSrvEntitiesPage() {
    return this.srvEntitiesPage;
  }

  /**
   * <p>Setter for srvEntitiesPage.</p>
   * @param pSrvEntitiesPage reference
   **/
  public final void setSrvEntitiesPage(
    final ISrvEntitiesPage pSrvEntitiesPage) {
    this.srvEntitiesPage = pSrvEntitiesPage;
  }
}
