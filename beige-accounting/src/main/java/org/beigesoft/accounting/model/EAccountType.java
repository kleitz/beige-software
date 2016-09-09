package org.beigesoft.accounting.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <pre>
 * Account Type - ASSET/LIABILITY/
 * OWNERS_EQUITY/GROSS_INCOME_REVENUE/GROSS_INCOME_EXPENSE.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public enum EAccountType {

  /**
   * <p>If asset.</p>
   **/
  ASSET,

  /**
   * <p>If liability.</p>
   **/
  LIABILITY,

  /**
   * <p>If Owner's equity.</p>
   **/
  OWNERS_EQUITY,

  /**
   * <p>If revenue of gross income.</p>
   **/
  GROSS_INCOME_REVENUE,

  /**
   * <p>If expense of gross income.</p>
   **/
  GROSS_INCOME_EXPENSE;
}
