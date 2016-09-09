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

import org.beigesoft.accounting.persistable.base.ASubaccount;

/**
 * <pre>
 * Model of wage type e.g. for cooking, sick compensation.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class WageType extends ASubaccount {

  /**
   * <p>Expense, not null.</p>
   **/
  private Expense expense;

  /**
   * <p>OOP friendly Constant of code type.</p>
   * @return 2008
   **/
  @Override
  public final Integer constTypeCode() {
    return 2008;
  }

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
