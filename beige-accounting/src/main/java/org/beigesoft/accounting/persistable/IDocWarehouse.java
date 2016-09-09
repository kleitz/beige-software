package org.beigesoft.accounting.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.accounting.model.EWarehouseMovementType;

/**
 * <pre>
 * Abstract model of document that makes warehouse entries,
 * e.g. PurchaseInvoice, Manufacture
 * </pre>
 *
 * @author Yury Demidenko
 */
public interface IDocWarehouse extends IDoc {

  /**
   * <p>If owned lines make warehouse entries this return
   * their type.</p>
   * @return Boolean
   **/
  EWarehouseMovementType getLinesWarehouseType();
}
