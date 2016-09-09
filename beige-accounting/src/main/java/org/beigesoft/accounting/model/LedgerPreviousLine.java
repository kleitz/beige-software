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

import java.math.BigDecimal;

/**
 * <pre>
 * Ledger previous line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class LedgerPreviousLine {

  /**
   * <p>Debit subacc.</p>
   **/
  private BigDecimal debit = BigDecimal.ZERO;

  /**
   * <p>Credit subacc.</p>
   **/
  private BigDecimal credit = BigDecimal.ZERO;

  /**
   * <p>Balance subacc.</p>
   **/
  private BigDecimal balance = BigDecimal.ZERO;

  //Simple getters and setters:
  /**
   * <p>Getter for debit.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebit() {
    return this.debit;
  }

  /**
   * <p>Setter for debit.</p>
   * @param pDebit reference
   **/
  public final void setDebit(final BigDecimal pDebit) {
    this.debit = pDebit;
  }

  /**
   * <p>Getter for credit.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCredit() {
    return this.credit;
  }

  /**
   * <p>Setter for credit.</p>
   * @param pCredit reference
   **/
  public final void setCredit(final BigDecimal pCredit) {
    this.credit = pCredit;
  }

  /**
   * <p>Getter for balance.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBalance() {
    return this.balance;
  }

  /**
   * <p>Setter for balance.</p>
   * @param pBalance reference
   **/
  public final void setBalance(final BigDecimal pBalance) {
    this.balance = pBalance;
  }
}
