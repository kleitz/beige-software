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

import org.beigesoft.model.IOwned;
import org.beigesoft.accounting.persistable.base.ADocTaxLine;

/**
 * <pre>
 * Model of Purchase Return Tax Line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class PurchaseReturnTaxLine extends ADocTaxLine
  implements IOwned<PurchaseReturn> {
  /**
   * <p>Customer Invoice.</p>
   **/
  private PurchaseReturn itsOwner;

  /**
   * <p>Geter for itsOwner.</p>
   * @return PurchaseReturn
   **/
  @Override
  public final PurchaseReturn getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final PurchaseReturn pItsOwner) {
    this.itsOwner = pItsOwner;
  }
}
