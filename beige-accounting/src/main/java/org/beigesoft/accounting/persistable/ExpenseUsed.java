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
 * Model of used expense in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ExpenseUsed extends ASubaccountUsed<Expense> {

  /**
   * <p>Expense.</p>
   **/
  private Expense subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return Expense
   **/
  @Override
  public final Expense getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final Expense pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
