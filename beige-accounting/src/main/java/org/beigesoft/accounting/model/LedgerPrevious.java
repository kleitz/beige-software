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

import java.util.Map;
import java.util.LinkedHashMap;
import java.math.BigDecimal;

/**
 * <pre>
 * Ledger previous line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class LedgerPrevious {

  /**
   * <p>Lines of subaccounts or single line
   * with empty name if they not exist.</p>
   **/
  private Map<String, LedgerPreviousLine> linesMap =
    new LinkedHashMap<String, LedgerPreviousLine>();

  /**
   * <p>Debit account total.</p>
   **/
  private BigDecimal debitAcc = BigDecimal.ZERO;

  /**
   * <p>Credit account total.</p>
   **/
  private BigDecimal creditAcc = BigDecimal.ZERO;

  /**
   * <p>Balance account total.</p>
   **/
  private BigDecimal balanceAcc = BigDecimal.ZERO;

  //Simple getters and setters:
  /**
   * <p>Getter for linesMap.</p>
   * @return Map<String, LedgerPreviousLine>
   **/
  public final Map<String, LedgerPreviousLine> getLinesMap() {
    return this.linesMap;
  }

  /**
   * <p>Setter for linesMap.</p>
   * @param pLinesMap reference
   **/
  public final void setLinesMap(final Map<String,
    LedgerPreviousLine> pLinesMap) {
    this.linesMap = pLinesMap;
  }

  /**
   * <p>Getter for debitAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebitAcc() {
    return this.debitAcc;
  }

  /**
   * <p>Setter for debitAcc.</p>
   * @param pDebitAcc reference
   **/
  public final void setDebitAcc(final BigDecimal pDebitAcc) {
    this.debitAcc = pDebitAcc;
  }

  /**
   * <p>Getter for creditAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCreditAcc() {
    return this.creditAcc;
  }

  /**
   * <p>Setter for creditAcc.</p>
   * @param pCreditAcc reference
   **/
  public final void setCreditAcc(final BigDecimal pCreditAcc) {
    this.creditAcc = pCreditAcc;
  }

  /**
   * <p>Getter for balanceAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBalanceAcc() {
    return this.balanceAcc;
  }

  /**
   * <p>Setter for balanceAcc.</p>
   * @param pBalanceAcc reference
   **/
  public final void setBalanceAcc(final BigDecimal pBalanceAcc) {
    this.balanceAcc = pBalanceAcc;
  }
}
