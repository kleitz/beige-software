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

import java.util.List;

import org.beigesoft.webstore.model.TradingCatalog;

/**
 * <p>Trading catalog service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvTradingCatalog {

  /**
   * <p>Retrieve/get Trading catalog.</p>
   * @return Trading catalog
   * @throws Exception - an exception
   **/
  List<TradingCatalog> getTradingCatalog() throws Exception;
}
