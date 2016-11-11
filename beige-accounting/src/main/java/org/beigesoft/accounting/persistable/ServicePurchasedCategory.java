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

import org.beigesoft.persistable.AHasNameIdLong;

/**
 * <pre>
 * Model of category of service purchased, e.g. "Car engine repairs".
 * It used for filter list of services.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ServicePurchasedCategory extends AHasNameIdLong {

  /**
   * <p>Expense, not null, subaccount for account expense.</p>
   **/
  private Expense expense;

  //Simple getters and setters:
  /**
   * <p>Getter for expense.</p>
   * @return Expense
   **/
  public final Expense getExpense() {
    return this.expense;
  }

  /**
   * <p>Setter for expense.</p>
   * @param pExpense reference
   **/
  public final void setExpense(final Expense pExpense) {
    this.expense = pExpense;
  }
}
