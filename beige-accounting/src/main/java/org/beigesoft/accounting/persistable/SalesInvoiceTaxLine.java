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
 * Model of Customer Invoice Tax Line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SalesInvoiceTaxLine extends ADocTaxLine
  implements IOwned<SalesInvoice> {
  /**
   * <p>Customer Invoice.</p>
   **/
  private SalesInvoice itsOwner;

  /**
   * <p>Geter for itsOwner.</p>
   * @return SalesInvoice
   **/
  @Override
  public final SalesInvoice getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final SalesInvoice pItsOwner) {
    this.itsOwner = pItsOwner;
  }
}
