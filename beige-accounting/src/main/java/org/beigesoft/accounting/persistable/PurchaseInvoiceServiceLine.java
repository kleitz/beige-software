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
 * Model of Vendor Invoice ServicePurchased Line.
 * It is immutable.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class PurchaseInvoiceServiceLine extends APersistableBase
  implements IOwned<PurchaseInvoice> {

  /**
   * <p>Version, reliable autoincrement algorithm.</p>
   **/
  private Long itsVersion;

  /**
   * <p>ServicePurchased.</p>
   **/
  private ServicePurchased service;

  /**
   * <p>Account service expense, e.g. Expenses or InventoryCapitalizedCost,
   * Not Null.</p>
   **/
  private Account accExpense;

  /**
   * <p>Vendor Invoice.</p>
   **/
  private PurchaseInvoice itsOwner;

  /**
   * <p>Cost.</p>
   **/
  private BigDecimal itsCost = BigDecimal.ZERO;

  /**
   * <p>Total taxes.</p>
   **/
  private BigDecimal totalTaxes = BigDecimal.ZERO;

  /**
   * <p>Taxes description, uneditable,
   * e.g. "tax1 10%=12, tax2 5%=6".</p>
   **/
  private String taxesDescription;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal itsTotal = BigDecimal.ZERO;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Geter for itsOwner.</p>
   * @return PurchaseInvoice
   **/
  @Override
  public final PurchaseInvoice getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final PurchaseInvoice pItsOwner) {
    this.itsOwner = pItsOwner;
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
   * <p>Getter for service.</p>
   * @return ServicePurchased
   **/
  public final ServicePurchased getService() {
    return this.service;
  }

  /**
   * <p>Setter for service.</p>
   * @param pService reference
   **/
  public final void setService(final ServicePurchased pService) {
    this.service = pService;
  }

  /**
   * <p>Getter for accExpense.</p>
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
   * <p>Getter for itsCost.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsCost() {
    return this.itsCost;
  }

  /**
   * <p>Setter for itsCost.</p>
   * @param pItsCost reference
   **/
  public final void setItsCost(final BigDecimal pItsCost) {
    this.itsCost = pItsCost;
  }

  /**
   * <p>Getter for totalTaxes.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotalTaxes() {
    return this.totalTaxes;
  }

  /**
   * <p>Setter for totalTaxes.</p>
   * @param pTotalTaxes reference
   **/
  public final void setTotalTaxes(final BigDecimal pTotalTaxes) {
    this.totalTaxes = pTotalTaxes;
  }

  /**
   * <p>Getter for taxesDescription.</p>
   * @return String
   **/
  public final String getTaxesDescription() {
    return this.taxesDescription;
  }

  /**
   * <p>Setter for taxesDescription.</p>
   * @param pTaxesDescription reference
   **/
  public final void setTaxesDescription(final String pTaxesDescription) {
    this.taxesDescription = pTaxesDescription;
  }

  /**
   * <p>Getter for itsTotal.</p>
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
