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
 * Model of used debtor/creditor category in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class DebtorCreditorCategoryUsed
  extends ASubaccountUsed<DebtorCreditorCategory> {

  /**
   * <p>DebtorCreditorCategory.</p>
   **/
  private DebtorCreditorCategory subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return DebtorCreditorCategory
   **/
  @Override
  public final DebtorCreditorCategory getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(
    final DebtorCreditorCategory pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
