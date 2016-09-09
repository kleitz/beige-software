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
import org.beigesoft.accounting.persistable.base.ATaxLine;

/**
 * <pre>
 * Model of inventory item tax category line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class InvItemTaxCategoryLine extends ATaxLine
  implements IOwned<InvItemTaxCategory> {

  /**
   * <p>Owner.</p>
   **/
  private InvItemTaxCategory itsOwner;

  /**
   * <p>Geter for itsOwner.</p>
   * @return InvItemTaxCategory
   **/
  @Override
  public final InvItemTaxCategory getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final InvItemTaxCategory pItsOwner) {
    this.itsOwner = pItsOwner;
  }
}
