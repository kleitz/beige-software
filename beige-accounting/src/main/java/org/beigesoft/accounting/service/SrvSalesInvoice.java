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
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw merchandise service
   **/
  public SrvSalesInvoice(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvI18n pSrvI18n, final DateFormat pDateFormatter,
          final ISrvWarehouseEntry pSrvWarehouseEntry,
            final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(SalesInvoice.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvI18n, pDateFormatter,
        pSrvWarehouseEntry, pSrvCogsEntry);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final SalesInvoice createEntity(
    final Map<String, Object> pAddParam) throws Exception {
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
  public final void makeAddPrepareForCopy(final Map<String, Object> pAddParam,
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
  public final void makeOtherEntries(final Map<String, Object> pAddParam,
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
            reversingLine.setDescription(getSrvI18n().getMsg("reversed_n")
              + reversedLine.getIdDatabaseBirth() + "-"
                + reversedLine.getItsId()); //local
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
              + " " + getSrvI18n().getMsg("reversing_n")
                + reversingLine.getIdDatabaseBirth() + "-"
                  + reversingLine.getItsId());
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
  public final void addCheckIsReadyToAccount(
    final Map<String, Object> pAddParam,
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
  public final void checkOtherFraudUpdate(final Map<String, Object> pAddParam,
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
  public final void makeFirstPrepareForSave(final Map<String, Object> pAddParam,
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
        .class.getSimpleName() + "short") + " #" + pEntity.getPrepaymentFrom()
          .getIdDatabaseBirth() + "-" + pEntity.getPrepaymentFrom().getItsId()
            + ", " + getDateFormatter().format(pEntity.getPrepaymentFrom()
              .getItsDate()) + ", " + pEntity.getPaymentTotal());
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
        + getSrvI18n().getMsg(PaymentFrom.class.getSimpleName() + "short")
          + " #" + payment.getIdDatabaseBirth() + "-" + payment.getItsId()
            + ", " + getDateFormatter().format(payment.getItsDate())
              + ", " + payment.getItsTotal());
    }
  }
}
