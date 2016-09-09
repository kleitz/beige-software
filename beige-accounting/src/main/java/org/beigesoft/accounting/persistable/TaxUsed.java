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
 * Model of used tax in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class TaxUsed extends ASubaccountUsed<Tax> {

  /**
   * <p>Tax.</p>
   **/
  private Tax subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return Tax
   **/
  @Override
  public final Tax getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final Tax pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
