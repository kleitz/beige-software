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

import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.persistable.WarehouseSite;

/**
 * <p>Business service for increase available goods in WEB-Store.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvIncreaseGoods {

  /**
   * <p>Increase available goods in WEB-Store.</p>
   * @param pAddParam additional param
   * @param goods a goods
   * @param warehouseSite Warehouse Site
   * @param itsQuantity quantity
   * @throws Exception - an exception
   **/
  void registerIncrease(Map<String, Object> pAddParam, InvItem goods,
    WarehouseSite warehouseSite, Integer itsQuantity) throws Exception;
}
