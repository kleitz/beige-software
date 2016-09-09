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

import java.util.List;
import java.util.Map;

import org.beigesoft.accounting.model.WarehouseSiteRestLine;

/**
 * <p>WarehouseSite Rests service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvWarehouseSiteRests {

  /**
   * <p>Retrieve WarehouseSite Rests.</p>
   * @param pAddParam additional param
   * @return WarehouseSite Rests Lines
   * @throws Exception - an exception
   **/
  List<WarehouseSiteRestLine> retrieveWarehouseSiteRests(
    Map<String, ?> pAddParam) throws Exception;
}
