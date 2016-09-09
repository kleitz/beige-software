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
 * Model of Debtor/Creditor Customer/Vendor.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class DebtorCreditor extends ASubaccount {

  /**
   * <p>Debtor/Creditor Category.</p>
   **/
  private DebtorCreditorCategory itsCategory;

  /**
   * <p>Phone.</p>
   **/
  private String itsPhone;

  /**
   * <p>Address.</p>
   **/
  private String itsAddress;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>OOP friendly Constant of code type.</p>
   * @return 2004
   **/
  @Override
  public final Integer constTypeCode() {
    return 2004;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsCategory.</p>
   * @return DebtorCreditorCategory
   **/
  public final DebtorCreditorCategory getItsCategory() {
    return this.itsCategory;
  }

  /**
   * <p>Setter for itsCategory.</p>
   * @param pItsCategory reference
   **/
  public final void setItsCategory(
    final DebtorCreditorCategory pItsCategory) {
    this.itsCategory = pItsCategory;
  }

  /**
   * <p>Getter for itsPhone.</p>
   * @return String
   **/
  public final String getItsPhone() {
    return this.itsPhone;
  }

  /**
   * <p>Setter for itsPhone.</p>
   * @param pItsPhone reference
   **/
  public final void setItsPhone(final String pItsPhone) {
    this.itsPhone = pItsPhone;
  }

  /**
   * <p>Getter for itsAddress.</p>
   * @return String
   **/
  public final String getItsAddress() {
    return this.itsAddress;
  }

  /**
   * <p>Setter for itsAddress.</p>
   * @param pItsAddress reference
   **/
  public final void setItsAddress(final String pItsAddress) {
    this.itsAddress = pItsAddress;
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
