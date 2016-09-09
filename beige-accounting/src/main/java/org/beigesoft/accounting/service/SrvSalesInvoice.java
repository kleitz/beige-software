package org.beigesoft.accounting.service;

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
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DateFormat;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.PaymentFrom;
import org.beigesoft.accounting.persistable.PrepaymentFrom;
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.SalesInvoiceLine;
import org.beigesoft.accounting.persistable.SalesInvoiceTaxLine;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.service.ISrvI18n;

/**
 * <p>Business service for customer invoice.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers,
 * and here it not used yet.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvSalesInvoice<RS>
  extends ASrvDocumentCogs<RS, SalesInvoice> {

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Date Formatter.</p>
   **/
  private DateFormat dateFormatter;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvSalesInvoice() {
    super(SalesInvoice.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw merchandise service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvSalesInvoice(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry,
            final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    super(SalesInvoice.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvWarehouseEntry, pSrvCogsEntry);
    this.srvI18n = pSrvI18n;
    this.dateFormatter = pDateFormatter;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final SalesInvoice createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    SalesInvoice entity = new SalesInvoice();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsDate(new Date());
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }


  /**
   * <p>Make additional preparations on entity copy.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeAddPrepareForCopy(final Map<String, ?> pAddParam,
    final SalesInvoice pEntity) throws Exception {
    pEntity.setTotalTaxes(BigDecimal.ZERO);
    pEntity.setSubtotal(BigDecimal.ZERO);
    if (pEntity.getReversedId() == null) {
      pEntity.setPaymentTotal(BigDecimal.ZERO);
      pEntity.setPaymentDescription("");
    }
  }

  /**
   * <p>Make other entries include reversing if it's need when save.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pIsNew if entity was new
   * @throws Exception - an exception
   **/
  @Override
  public final void makeOtherEntries(final Map<String, ?> pAddParam,
    final SalesInvoice pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])) {
      if (pEntity.getReversedId() != null) {
        //reverse none-reversed lines:
        List<SalesInvoiceLine> reversedLines = getSrvOrm().
          retrieveEntityOwnedlist(SalesInvoiceLine.class,
            SalesInvoice.class, pEntity.getReversedId());
        for (SalesInvoiceLine reversedLine : reversedLines) {
          if (reversedLine.getReversedId() == null) {
            SalesInvoiceLine reversingLine = new SalesInvoiceLine();
            reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
            reversingLine.setReversedId(reversedLine.getItsId());
            reversingLine.setInvItem(reversedLine.getInvItem());
            reversingLine.setUnitOfMeasure(reversedLine.getUnitOfMeasure());
            reversingLine.setItsPrice(reversedLine.getItsPrice());
            reversingLine.setItsQuantity(reversedLine.getItsQuantity()
              .negate());
            reversingLine.setItsTotal(reversedLine.getItsTotal().negate());
            reversingLine.setSubtotal(reversedLine.getSubtotal().negate());
            reversingLine.setTotalTaxes(reversedLine.getTotalTaxes().negate());
            reversingLine.setTaxesDescription(reversedLine
              .getTaxesDescription());
            reversingLine.setIsNew(true);
            reversingLine.setItsOwner(pEntity);
            reversingLine.setDescription("Reversed ID: "
              + reversedLine.getItsId());
            getSrvOrm().insertEntity(reversingLine);
            getSrvWarehouseEntry().reverseDraw(pAddParam, reversingLine);
            getSrvCogsEntry().reverseDraw(pAddParam, reversingLine,
              pEntity.getItsDate(), pEntity.getItsId());
            String descr;
            if (reversedLine.getDescription() == null) {
              descr = "";
            } else {
              descr = reversedLine.getDescription();
            }
            reversedLine.setDescription(descr
              + " reversing ID: " + reversingLine.getItsId());
            reversedLine.setReversedId(reversingLine.getItsId());
            getSrvOrm().updateEntity(reversedLine);
          }
        }
        List<SalesInvoiceTaxLine> reversedTaxLines = getSrvOrm().
          retrieveEntityOwnedlist(SalesInvoiceTaxLine.class,
            SalesInvoice.class, pEntity.getReversedId());
        for (SalesInvoiceTaxLine reversedLine : reversedTaxLines) {
          if (reversedLine.getReversedId() == null) {
            SalesInvoiceTaxLine reversingLine = new SalesInvoiceTaxLine();
            reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
            reversingLine.setReversedId(reversedLine.getItsId());
            reversingLine.setItsTotal(reversedLine.getItsTotal().negate());
            reversingLine.setTax(reversedLine.getTax());
            reversingLine.setIsNew(true);
            reversingLine.setItsOwner(pEntity);
            getSrvOrm().insertEntity(reversingLine);
            reversedLine.setReversedId(reversingLine.getItsId());
            getSrvOrm().updateEntity(reversedLine);
          }
        }
      }
      if (pEntity.getPrepaymentFrom() != null) {
        if (pEntity.getReversedId() != null) {
          pEntity.getPrepaymentFrom().setSalesInvoiceId(null);
        } else {
          pEntity.getPrepaymentFrom().setSalesInvoiceId(pEntity.getItsId());
        }
        getSrvOrm().updateEntity(pEntity.getPrepaymentFrom());
      }
    }
  }

  /**
   * <p>Additional check document for ready to account (make acc.entries).</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void addCheckIsReadyToAccount(final Map<String, ?> pAddParam,
    final SalesInvoice pEntity) throws Exception {
    //nothing
  }

  /**
   * <p>Check other fraud update e.g. prevent change completed unaccounted
   * manufacturing process.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pOldEntity old saved entity
   * @throws Exception - an exception
   **/
  @Override
  public final void checkOtherFraudUpdate(final Map<String, ?> pAddParam,
    final SalesInvoice pEntity,
      final SalesInvoice pOldEntity) throws Exception {
    //nothing
  }

  /**
   * <p>Make save preparations before insert/update block if it's need.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeFirstPrepareForSave(final Map<String, ?> pAddParam,
    final SalesInvoice pEntity) throws Exception {
    if (pEntity.getPrepaymentFrom() != null) {
      pEntity.setPrepaymentFrom(getSrvOrm().retrieveEntity(pEntity
        .getPrepaymentFrom()));
      if (pEntity.getReversedId() == null && pEntity.getPrepaymentFrom()
        .getSalesInvoiceId() != null && !pEntity.getHasMadeAccEntries()) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "prepayment_already_in_use");
      }
      if (pEntity.getReversedId() == null && !pEntity.getPrepaymentFrom()
        .getCustomer().getItsId().equals(pEntity.getCustomer().getItsId())) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "prepayment_for_different_vendor");
      }
    }
    if (pEntity.getReversedId() != null && pEntity.getPrepaymentFrom() != null
      && pEntity.getPaymentTotal().compareTo(pEntity.getPrepaymentFrom()
        .getItsTotal()) != 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "reverse_payments_first");
    }
    if (pEntity.getReversedId() != null && pEntity.getPrepaymentFrom() == null
      && pEntity.getPaymentTotal().compareTo(BigDecimal.ZERO) != 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "reverse_payments_first");
    }
    if (pEntity.getReversedId() == null) {
      calculateTotalPayment(pEntity);
    }
  }

  //Utils:
  /**
   * <p>Calculate Total Payment.</p>
   * @param pEntity SalesInvoice
   * @throws Exception - an exception
   **/
  public final void calculateTotalPayment(
    final SalesInvoice pEntity) throws Exception {
    if (pEntity.getPrepaymentFrom() != null) {
      pEntity.setPaymentTotal(pEntity.getPrepaymentFrom().getItsTotal());
      pEntity.setPaymentDescription(getSrvI18n().getMsg(PrepaymentFrom
        .class.getSimpleName()) + " # " + pEntity.getPrepaymentFrom()
          .getItsId() + ", " + getDateFormatter().format(pEntity
            .getPrepaymentFrom().getItsDate()) + ", "
              + pEntity.getPaymentTotal());
    } else {
      pEntity.setPaymentTotal(BigDecimal.ZERO);
      pEntity.setPaymentDescription("");
    }
    List<PaymentFrom> payments = getSrvOrm()
      .retrieveListWithConditions(PaymentFrom.class,
        "where PAYMENTFROM.HASMADEACCENTRIES=1 and PAYMENTFROM.REVERSEDID"
          + " is null and SALESINVOICE=" + pEntity.getItsId());
    for (PaymentFrom payment : payments) {
      pEntity.setPaymentTotal(pEntity.getPaymentTotal()
        .add(payment.getItsTotal()));
      pEntity.setPaymentDescription(pEntity.getPaymentDescription() + " "
        + getSrvI18n().getMsg(PaymentFrom.class.getSimpleName()) + " # "
          + payment.getItsId() + ", " + getDateFormatter()
            .format(payment.getItsDate()) + ", " + payment.getItsTotal());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvI18n.</p>
   * @return ISrvI18n
   **/
  public final ISrvI18n getSrvI18n() {
    return this.srvI18n;
  }

  /**
   * <p>Setter for srvI18n.</p>
   * @param pSrvI18n reference
   **/
  public final void setSrvI18n(final ISrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }

  /**
   * <p>Getter for dateFormatter.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateFormatter() {
    return this.dateFormatter;
  }

  /**
   * <p>Setter for dateFormatter.</p>
   * @param pDateFormatter reference
   **/
  public final void setDateFormatter(final DateFormat pDateFormatter) {
    this.dateFormatter = pDateFormatter;
  }
}
