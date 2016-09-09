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
public class EmployeeUsed extends ASubaccountUsed<Employee> {

  /**
   * <p>Employee.</p>
   **/
  private Employee subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return Employee
   **/
  @Override
  public final Employee getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final Employee pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
