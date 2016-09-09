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

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.persistable.AHasIdLong;

/**
 * <pre>
 * Model that store balance of a account and
 * subaccount at the start of each month (or week etc) to improve performance.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class BalanceAt extends AHasIdLong {

  /**
   * <p>Version changed time algorithm.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Date, Not Null, usually start of month.</p>
   **/
  private Date itsDate;

  /**
   * <p>Account e.g Inventory.</p>
   **/
  private Account itsAccount;

  /**
   * <p>Subacccount type, e.g. 2002 - InvItem,
   * 2003 - Tax, 2004 - DebtorCreditor.
   * This is constant [entity].constTypeCode().</p>
   **/
  private Integer subaccType;

  /**
   * <p>Foreign ID of subaccount.</p>
   **/
  private Long subaccId;

  /**
   * <p>Appearance of subaccount.</p>
   **/
  private String subaccount;

  /**
   * <p>Debit.</p>
   **/
  private BigDecimal itsBalance = new BigDecimal("0.00");

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
   * <p>Getter for itsVersion.</p>
   * @return Long
   **/
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference
   **/
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
  }

  /**
   * <p>Getter for itsAccount.</p>
   * @return Account
   **/
  public final Account getItsAccount() {
    return this.itsAccount;
  }

  /**
   * <p>Setter for itsAccount.</p>
   * @param pItsAccount reference
   **/
  public final void setItsAccount(final Account pItsAccount) {
    this.itsAccount = pItsAccount;
  }

  /**
   * <p>Getter for subaccType.</p>
   * @return Integer
   **/
  public final Integer getSubaccType() {
    return this.subaccType;
  }

  /**
   * <p>Setter for subaccType.</p>
   * @param pSubaccType reference
   **/
  public final void setSubaccType(final Integer pSubaccType) {
    this.subaccType = pSubaccType;
  }

  /**
   * <p>Getter for subaccId.</p>
   * @return Long
   **/
  public final Long getSubaccId() {
    return this.subaccId;
  }

  /**
   * <p>Setter for subaccId.</p>
   * @param pSubaccId reference
   **/
  public final void setSubaccId(final Long pSubaccId) {
    this.subaccId = pSubaccId;
  }

  /**
   * <p>Getter for subaccount.</p>
   * @return String
   **/
  public final String getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  public final void setSubaccount(final String pSubaccount) {
    this.subaccount = pSubaccount;
  }

  /**
   * <p>Getter for itsBalance.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsBalance() {
    return this.itsBalance;
  }

  /**
   * <p>Setter for itsBalance.</p>
   * @param pItsBalance reference
   **/
  public final void setItsBalance(final BigDecimal pItsBalance) {
    this.itsBalance = pItsBalance;
  }
}
