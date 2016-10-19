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

import org.beigesoft.accounting.model.WarehouseRestLine;

/**
 * <p>Warehouse Rests service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvWarehouseRests {

  /**
   * <p>Retrieve Warehouse Rests.</p>
   * @param pAddParam additional param
   * @return Warehouse Rests Lines
   * @throws Exception - an exception
   **/
  List<WarehouseRestLine> retrieveWarehouseRests(
    Map<String, Object> pAddParam) throws Exception;
}
