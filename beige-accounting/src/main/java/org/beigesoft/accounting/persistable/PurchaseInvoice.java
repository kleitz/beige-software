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
import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.accounting.persistable.base.ADocWithTaxes;
import org.beigesoft.accounting.model.EWarehouseMovementType;

/**
 * <pre>
 * Model of Vendor Invoice.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class PurchaseInvoice extends ADocWithTaxes
  implements IDocWarehouse {

  /**
   * <p>There is no goods in stock.</p>
   **/
  public static final int THERE_IS_NO_GOODS = 1301;

  /**
   * <p>There is withdrawals from this source!
   * It arises when theRest != quantity for non-reversed item source</p>
   **/
  public static final int SOURSE_IS_IN_USE = 1303;

  /**
   * <p>Vendor.</p>
   **/
  private DebtorCreditor vendor;

  /**
   * <p>Prepayment.</p>
   **/
  private PrepaymentTo prepaymentTo;

  /**
   * <p>Payment total (prepayment and afterpayment).</p>
   **/
  private BigDecimal paymentTotal = BigDecimal.ZERO;

  /**
   * <p>Payment description, read only.</p>
   **/
  private String paymentDescription;

  /**
   * <p>Lines.</p>
   **/
  private List<PurchaseInvoiceLine> itsLines;

  /**
   * <p>Services.</p>
   **/
  private List<PurchaseInvoiceServiceLine> services;

  /**
   * <p>Taxes lines.</p>
   **/
  private List<PurchaseInvoiceTaxLine> taxesLines;

  /**
   * <p>Payment done by date, if required.</p>
   **/
  private Date payByDate;

  /**
   * <p>OOP friendly Constant of code type 1.</p>
   **/
  @Override
  public final Integer constTypeCode() {
    return 1;
  }

  /**
   * <p>If owned lines make warehouse entries this return
   * their type.</p>
   * @return Boolean
   **/
  @Override
  public final EWarehouseMovementType getLinesWarehouseType() {
    return EWarehouseMovementType.LOAD;
  }

  //Hiding references getters and setters:
  /**
   * <p>Getter for payByDate.</p>
   * @return Date
   **/
  public final Date getPayByDate() {
    if (this.payByDate == null) {
      return null;
    }
    return new Date(this.payByDate.getTime());
  }

  /**
   * <p>Setter for payByDate.</p>
   * @param pPayByDate reference
   **/
  public final void setPayByDate(final Date pPayByDate) {
    if (pPayByDate == null) {
      this.payByDate = null;
    } else {
      this.payByDate = new Date(pPayByDate.getTime());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for vendor.</p>
   * @return DebtorCreditor
   **/
  public final DebtorCreditor getVendor() {
    return this.vendor;
  }

  /**
   * <p>Setter for vendor.</p>
   * @param pVendor reference
   **/
  public final void setVendor(final DebtorCreditor pVendor) {
    this.vendor = pVendor;
  }

  /**
   * <p>Getter for prepaymentTo.</p>
   * @return PrepaymentTo
   **/
  public final PrepaymentTo getPrepaymentTo() {
    return this.prepaymentTo;
  }

  /**
   * <p>Setter for prepaymentTo.</p>
   * @param pPrepaymentTo reference
   **/
  public final void setPrepaymentTo(final PrepaymentTo pPrepaymentTo) {
    this.prepaymentTo = pPrepaymentTo;
  }

  /**
   * <p>Getter for paymentTotal.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPaymentTotal() {
    return this.paymentTotal;
  }

  /**
   * <p>Setter for paymentTotal.</p>
   * @param pPaymentTotal reference
   **/
  public final void setPaymentTotal(final BigDecimal pPaymentTotal) {
    this.paymentTotal = pPaymentTotal;
  }

  /**
   * <p>Getter for paymentDescription.</p>
   * @return String
   **/
  public final String getPaymentDescription() {
    return this.paymentDescription;
  }

  /**
   * <p>Setter for paymentDescription.</p>
   * @param pPaymentDescription reference
   **/
  public final void setPaymentDescription(final String pPaymentDescription) {
    this.paymentDescription = pPaymentDescription;
  }

  /**
   * <p>Geter for itsLines.</p>
   * @return List<PurchaseInvoiceLine>
   **/
  public final List<PurchaseInvoiceLine> getItsLines() {
    return this.itsLines;
  }

  /**
   * <p>Setter for itsLines.</p>
   * @param pItsLines reference
   **/
  public final void setItsLines(final List<PurchaseInvoiceLine> pItsLines) {
    this.itsLines = pItsLines;
  }

  /**
   * <p>Getter for services.</p>
   * @return List<PurchaseInvoiceServiceLine>
   **/
  public final List<PurchaseInvoiceServiceLine> getServices() {
    return this.services;
  }

  /**
   * <p>Setter for services.</p>
   * @param pServices reference
   **/
  public final void setServices(
    final List<PurchaseInvoiceServiceLine> pServices) {
    this.services = pServices;
  }

  /**
   * <p>Geter for taxesLines.</p>
   * @return List<PurchaseInvoiceTaxLine>
   **/
  public final List<PurchaseInvoiceTaxLine> getTaxesLines() {
    return this.taxesLines;
  }

  /**
   * <p>Setter for taxesLines.</p>
   * @param pTaxesLines reference
   **/
  public final void setTaxesLines(
    final List<PurchaseInvoiceTaxLine> pTaxesLines) {
    this.taxesLines = pTaxesLines;
  }
}
