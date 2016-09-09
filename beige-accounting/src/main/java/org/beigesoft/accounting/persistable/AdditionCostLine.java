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

import java.math.BigDecimal;

import org.beigesoft.model.IOwned;
import org.beigesoft.persistable.APersistableBase;

/**
 * <pre>
 * Model of ManufacturingProcess Line of
 * additional direct/indirect uncapitalized costs.
 * It is unchangeable.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class AdditionCostLine extends APersistableBase
  implements IOwned<ManufacturingProcess> {

  /**
   * <p>Product In Progress.</p>
   **/
  private ManufacturingProcess itsOwner;

  /**
   * <p>Account, Not Null, e.g. WagesExpences.</p>
   **/
  private Account accExpense;

  /**
   * <p>Subacccount type.
   * This is constant [entity].constTypeCode(), Not Null.</p>
   **/
  private Integer subaccExpenseType;

  /**
   * <p>Foreign ID of subaccount, Not Null.</p>
   **/
  private Long subaccExpenseId;

  /**
   * <p>Appearance of subaccount e.g. "Indirect libor", Not Null.</p>
   **/
  private String subaccExpense;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal itsTotal = new BigDecimal("0.00");

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Geter for itsOwner.</p>
   * @return ManufacturingProcess
   **/
  @Override
  public final ManufacturingProcess getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final ManufacturingProcess pItsOwner) {
    this.itsOwner = pItsOwner;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for accExpense.</p>
   * @return Account
   **/
  public final Account getAccExpense() {
    return this.accExpense;
  }

  /**
   * <p>Setter for accExpense.</p>
   * @param pAccExpense reference
   **/
  public final void setAccExpense(final Account pAccExpense) {
    this.accExpense = pAccExpense;
  }

  /**
   * <p>Geter for subaccExpenseType.</p>
   * @return Integer
   **/
  public final Integer getSubaccExpenseType() {
    return this.subaccExpenseType;
  }

  /**
   * <p>Setter for subaccExpenseType.</p>
   * @param pSubaccExpenseType reference
   **/
  public final void setSubaccExpenseType(final Integer pSubaccExpenseType) {
    this.subaccExpenseType = pSubaccExpenseType;
  }

  /**
   * <p>Geter for subaccExpenseId.</p>
   * @return Long
   **/
  public final Long getSubaccExpenseId() {
    return this.subaccExpenseId;
  }

  /**
   * <p>Setter for subaccExpenseId.</p>
   * @param pSubaccExpenseId reference
   **/
  public final void setSubaccExpenseId(final Long pSubaccExpenseId) {
    this.subaccExpenseId = pSubaccExpenseId;
  }

  /**
   * <p>Geter for subaccExpense.</p>
   * @return String
   **/
  public final String getSubaccExpense() {
    return this.subaccExpense;
  }

  /**
   * <p>Setter for subaccExpense.</p>
   * @param pSubaccExpense reference
   **/
  public final void setSubaccExpense(final String pSubaccExpense) {
    this.subaccExpense = pSubaccExpense;
  }

  /**
   * <p>Geter for itsTotal.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsTotal() {
    return this.itsTotal;
  }

  /**
   * <p>Setter for itsTotal.</p>
   * @param pItsTotal reference
   **/
  public final void setItsTotal(final BigDecimal pItsTotal) {
    this.itsTotal = pItsTotal;
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
