package org.beigesoft.accounting.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <pre>
 * Type of warehouse movement source LOAD/WITHDRAWAL/MOVE.
 * E.g. PurchaseInvoiceLine - LOAD, SalesInvoiceLine - WITHDRAWAL.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public enum EWarehouseMovementType {

  /**
   * <p>If loads (put).</p>
   **/
  LOAD,

  /**
   * <p>If withdrawals.</p>
   **/
  WITHDRAWAL,

  /**
   * <p>If Owner's equity.</p>
   **/
  MOVE,
}
