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
 * Model of Customer Invoice.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SalesInvoice extends ADocWithTaxes
  implements IDocWarehouse {

  /**
   * <p>Customer.</p>
   **/
  private DebtorCreditor customer;

  /**
   * <p>Lines.</p>
   **/
  private List<SalesInvoiceLine> itsLines;

  /**
   * <p>Taxes lines.</p>
   **/
  private List<SalesInvoiceTaxLine> taxesLines;

  /**
   * <p>Prepayment.</p>
   **/
  private PrepaymentFrom prepaymentFrom;

  /**
   * <p>Payment total (prepayment and afterpayment).</p>
   **/
  private BigDecimal paymentTotal = BigDecimal.ZERO;

  /**
   * <p>Payment description, read only.</p>
   **/
  private String paymentDescription;

  /**
   * <p>Payment done by date, if required.</p>
   **/
  private Date payByDate;

  /**
   * <p>OOP friendly Constant of code type 2.</p>
   **/
  @Override
  public final Integer constTypeCode() {
    return 2;
  }

  /**
   * <p>If owned lines make warehouse entries this return
   * their type.</p>
   * @return Boolean
   **/
  @Override
  public final EWarehouseMovementType getLinesWarehouseType() {
    return EWarehouseMovementType.WITHDRAWAL;
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
   * <p>Geter for customer.</p>
   * @return DebtorCreditor
   **/
  public final DebtorCreditor getCustomer() {
    return this.customer;
  }

  /**
   * <p>Setter for customer.</p>
   * @param pCustomer reference
   **/
  public final void setCustomer(final DebtorCreditor pCustomer) {
    this.customer = pCustomer;
  }

  /**
   * <p>Geter for itsLines.</p>
   * @return List<SalesInvoiceLine>
   **/
  public final List<SalesInvoiceLine> getItsLines() {
    return this.itsLines;
  }

  /**
   * <p>Setter for itsLines.</p>
   * @param pItsLines reference
   **/
  public final void setItsLines(final List<SalesInvoiceLine> pItsLines) {
    this.itsLines = pItsLines;
  }

  /**
   * <p>Geter for taxesLines.</p>
   * @return List<SalesInvoiceTaxLine>
   **/
  public final List<SalesInvoiceTaxLine> getTaxesLines() {
    return this.taxesLines;
  }

  /**
   * <p>Setter for taxesLines.</p>
   * @param pTaxesLines reference
   **/
  public final void setTaxesLines(
    final List<SalesInvoiceTaxLine> pTaxesLines) {
    this.taxesLines = pTaxesLines;
  }

  /**
   * <p>Getter for prepaymentFrom.</p>
   * @return PrepaymentFrom
   **/
  public final PrepaymentFrom getPrepaymentFrom() {
    return this.prepaymentFrom;
  }

  /**
   * <p>Setter for prepaymentFrom.</p>
   * @param pPrepaymentFrom reference
   **/
  public final void setPrepaymentFrom(final PrepaymentFrom pPrepaymentFrom) {
    this.prepaymentFrom = pPrepaymentFrom;
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
}
