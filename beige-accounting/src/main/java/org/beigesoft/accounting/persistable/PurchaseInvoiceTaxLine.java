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
 * Model of Vendor Invoice tax Line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class PurchaseInvoiceTaxLine extends ADocTaxLine
  implements IOwned<PurchaseInvoice> {

  /**
   * <p>Vendor Invoice.</p>
   **/
  private PurchaseInvoice itsOwner;

  /**
   * <p>Geter for itsOwner.</p>
   * @return PurchaseInvoice
   **/
  @Override
  public final PurchaseInvoice getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final PurchaseInvoice pItsOwner) {
    this.itsOwner = pItsOwner;
  }
}
