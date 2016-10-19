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

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.accounting.persistable.IDrawItemSource;
import org.beigesoft.accounting.persistable.IMakingWarehouseEntry;
import org.beigesoft.accounting.persistable.IDocWarehouse;
import org.beigesoft.accounting.persistable.base.ADrawItemEntry;

/**
 * <p>Business service for draw inventory item from a holder
 * (e.g. purchase invoice line) to sale, manufacture, use, loss.</p>
 *
 * @param <T> draw item entry type e.g. CogsEntry
 * @author Yury Demidenko
 */
public interface ISrvDrawItemEntry<T extends ADrawItemEntry> {

  /**
   * <p>Withdrawal item.</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @param pDateAccount date of account
   * @param pDrawingOwnerId drawing Owner Id if exists
   * @throws Exception - an exception
   **/
  void withdrawal(Map<String, Object> pAddParam,
    IMakingWarehouseEntry pEntity, Date pDateAccount,
      Long pDrawingOwnerId) throws Exception;

  /**
   * <p>Withdrawal warehouse item for use/sale/loss from given source.</p>
   * @param pAddParam additional param
   * @param pEntity drawing entity
   * @param pSource drawn entity
   * @param pQuantityToDraw quantity to draw
   * @throws Exception - an exception
   **/
  void withdrawalFrom(Map<String, Object> pAddParam,
    IMakingWarehouseEntry pEntity,
      IDrawItemSource pSource, BigDecimal pQuantityToDraw) throws Exception;

  /**
   * <p>Reverse a withdrawal material for manufacture product.</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @param pDateAccount date of account
   * @param pDrawingOwnerId drawing Owner Id if exists
   * @throws Exception - an exception
   **/
  void reverseDraw(Map<String, Object> pAddParam,
    IMakingWarehouseEntry pEntity, Date pDateAccount,
      Long pDrawingOwnerId) throws Exception;

  /**
   * <p>Retrieve entries for document.</p>
   * @param pAddParam additional param
   * @param pEntity a document
   * @return warehouse entries made by this document
   * @throws Exception - an exception
   **/
  List<T> retrieveEntriesFor(Map<String, Object> pAddParam,
    IDocWarehouse pEntity) throws Exception;
}
