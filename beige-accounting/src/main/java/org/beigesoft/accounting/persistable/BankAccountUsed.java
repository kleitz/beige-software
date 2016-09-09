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
 * Model of used Bank Account in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class BankAccountUsed extends ASubaccountUsed<BankAccount> {

  /**
   * <p>BankAccount.</p>
   **/
  private BankAccount subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return BankAccount
   **/
  @Override
  public final BankAccount getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final BankAccount pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
