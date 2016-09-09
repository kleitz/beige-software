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
 * Model of Sales Return tax Line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SalesReturnTaxLine extends ADocTaxLine
  implements IOwned<SalesReturn> {

  /**
   * <p>Vendor Invoice.</p>
   **/
  private SalesReturn itsOwner;

  /**
   * <p>Geter for itsOwner.</p>
   * @return SalesReturn
   **/
  @Override
  public final SalesReturn getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final SalesReturn pItsOwner) {
    this.itsOwner = pItsOwner;
  }
}
