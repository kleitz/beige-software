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
 * Model of used Employee Category in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class EmployeeCategoryUsed extends ASubaccountUsed<EmployeeCategory> {

  /**
   * <p>EmployeeCategory.</p>
   **/
  private EmployeeCategory subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return EmployeeCategory
   **/
  @Override
  public final EmployeeCategory getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final EmployeeCategory pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
