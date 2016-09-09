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

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.model.IHasTypeCode;
import org.beigesoft.persistable.APersistableBase;

/**
 * <pre>
 * Model of Input Accounting entries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class AccountingEntries extends APersistableBase
 implements IHasTypeCode {

  /**
   * <p>Version, changed time algorithm cause check dirty of
   * calculated from it (derived) records.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Date.</p>
   **/
  private Date itsDate;

  /**
   * <p>Total debit.</p>
   **/
  private BigDecimal totalDebit = new BigDecimal("0.00");

  /**
   * <p>Total credit.</p>
   **/
  private BigDecimal totalCredit = new BigDecimal("0.00");

  /**
   * <p>Lines.</p>
   **/
  private List<AccountingEntry> itsLines;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Constant of code type.</p>
   * @return 3
   **/
  public final Integer constTypeCode() {
    return 3;
  }

  //Hiding references getters and setters:
  /**
   * <p>Geter for itsDate.</p>
   * @return Date
   **/
  public final Date getItsDate() {
    if (this.itsDate == null) {
      return null;
    }
    return new Date(this.itsDate.getTime());
  }

  /**
   * <p>Setter for itsDate.</p>
   * @param pItsDate reference
   **/
  public final void setItsDate(final Date pItsDate) {
    if (pItsDate == null) {
      this.itsDate = null;
    } else {
      this.itsDate = new Date(pItsDate.getTime());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for itsVersion.</p>
   * @return Long
   **/
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Geter for totalDebit.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotalDebit() {
    return this.totalDebit;
  }

  /**
   * <p>Setter for totalDebit.</p>
   * @param pTotalDebit reference
   **/
  public final void setTotalDebit(final BigDecimal pTotalDebit) {
    this.totalDebit = pTotalDebit;
  }

  /**
   * <p>Geter for totalCredit.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotalCredit() {
    return this.totalCredit;
  }

  /**
   * <p>Setter for totalCredit.</p>
   * @param pTotalCredit reference
   **/
  public final void setTotalCredit(final BigDecimal pTotalCredit) {
    this.totalCredit = pTotalCredit;
  }

  /**
   * <p>Geter for itsLines.</p>
   * @return List<AccountingEntry>
   **/
  public final List<AccountingEntry> getItsLines() {
    return this.itsLines;
  }

  /**
   * <p>Setter for itsLines.</p>
   * @param pItsLines reference
   **/
  public final void setItsLines(final List<AccountingEntry> pItsLines) {
    this.itsLines = pItsLines;
  }

  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }
}
