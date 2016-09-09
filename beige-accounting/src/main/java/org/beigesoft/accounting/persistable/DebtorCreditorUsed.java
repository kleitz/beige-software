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
 * Model of used debtor/creditor in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class DebtorCreditorUsed extends ASubaccountUsed<DebtorCreditor> {

  /**
   * <p>DebtorCreditor.</p>
   **/
  private DebtorCreditor subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return DebtorCreditor
   **/
  @Override
  public final DebtorCreditor getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final DebtorCreditor pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
