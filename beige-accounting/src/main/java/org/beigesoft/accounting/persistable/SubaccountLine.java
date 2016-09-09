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

import org.beigesoft.model.IOwned;
import org.beigesoft.persistable.AHasIdLong;

/**
 * <pre>
 * Model of subaccount line in account.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SubaccountLine extends AHasIdLong implements IOwned<Account> {

  /**
   * <p>Account.</p>
   **/
  private Account itsOwner;

  /**
   * <p>Version, changed time algorithm cause check dirty.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Subaccount type, not null, must be same as owner's one.</p>
   **/
  private Integer subaccType;

  /**
   * <p>Subaccount ID, not null.</p>
   **/
  private Long subaccId;

  /**
   * <p>Subaccount name, not null.</p>
   **/
  private String subaccName;

  /**
   * <p>Getter for itsOwner.</p>
   * @return Account
   **/
  @Override
  public final Account getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final Account pItsOwner) {
    this.itsOwner = pItsOwner;
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
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference/value
   **/
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
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
   * @return Integer
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
   * <p>Getter for subaccName.</p>
   * @return String
   **/
  public final String getSubaccName() {
    return this.subaccName;
  }

  /**
   * <p>Setter for subaccName.</p>
   * @param pSubaccName reference
   **/
  public final void setSubaccName(final String pSubaccName) {
    this.subaccName = pSubaccName;
  }
}
