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

import org.beigesoft.accounting.persistable.base.ASubaccountUsed;

/**
 * <pre>
 * Model of used Property in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class PropertyUsed extends ASubaccountUsed<Property> {

  /**
   * <p>Property.</p>
   **/
  private Property subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return Property
   **/
  @Override
  public final Property getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final Property pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
