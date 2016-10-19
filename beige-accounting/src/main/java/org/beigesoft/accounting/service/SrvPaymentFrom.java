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

import java.util.Date;
import java.util.Map;

import org.beigesoft.holder.IAttributes;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.PaymentFrom;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for prepayment for sales.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers,
 * and here it not used yet.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvPaymentFrom<RS>
  extends ASrvDocument<RS, PaymentFrom> {

  /**
   * <p>Type Codes of sub-accounts service.</p>
   **/
  private ISrvSubaccCode srvTypeCode;

  /**
   * <p>Sales Invoice service.</p>
   **/
  private SrvSalesInvoice srvSalesInvoice;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvPaymentFrom() {
    super(PaymentFrom.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvTypeCodeSubacc Type Codes of sub-accounts service
   * @param pSrvSalesInvoice Sales Invoice service
   **/
  public SrvPaymentFrom(final ISrvOrm<RS> pSrvOrm,
    final SrvSalesInvoice pSrvSalesInvoice,
      final ISrvAccSettings pSrvAccSettings, final ISrvAccEntry pSrvAccEntry,
        final ISrvSubaccCode pSrvTypeCodeSubacc) {
    super(PaymentFrom.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry);
    this.srvTypeCode = pSrvTypeCodeSubacc;
    this.srvSalesInvoice = pSrvSalesInvoice;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final PaymentFrom createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    PaymentFrom entity = new PaymentFrom();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsDate(new Date());
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve other data of entity e.g. warehouse entries.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void retrieveOtherDataFor(final Map<String, Object> pAddParam,
    final PaymentFrom pEntity) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
  }

  /**
   * <p>Make additional preparations on entity copy.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeAddPrepareForCopy(final Map<String, Object> pAddParam,
    final PaymentFrom pEntity) throws Exception {
    if (pEntity.getReversedId() == null) {
      pEntity.setSalesInvoice(null);
    }
    addTypeCodeIntoAttrs(pAddParam);
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
    final PaymentFrom pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])) {
      this.srvSalesInvoice.calculateTotalPayment(pEntity
        .getSalesInvoice());
      getSrvOrm().updateEntity(pEntity.getSalesInvoice());
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
      final PaymentFrom pEntity) throws Exception {
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
    final PaymentFrom pEntity,
      final PaymentFrom pOldEntity) throws Exception {
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
    final PaymentFrom pEntity) throws Exception {
    //BeigeORM refresh:
    pEntity.setAccCash(getSrvOrm()
      .retrieveEntity(pEntity.getAccCash()));
    if (pEntity.getAccCash().getSubaccType() != null
      && pEntity.getSubaccCashId() == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "select_subaccount");
    }
    SalesInvoice purchaseInvoice = getSrvOrm().retrieveEntity(pEntity
      .getSalesInvoice());
    if (!purchaseInvoice.getHasMadeAccEntries()
      || purchaseInvoice.getReversedId() != null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "purchase_invoice_must_be_accounted");
    }
    pEntity.setSalesInvoice(purchaseInvoice);
  }

  /**
   * <p>Added source types.</p>
   * @param pAddParam additional param
   */
  public final void addTypeCodeIntoAttrs(final Map<String, Object> pAddParam) {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("typeCodeSubaccMap",
      this.srvTypeCode.getTypeCodeMap());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvTypeCode.</p>
   * @return ISrvSubaccCode
   **/
  public final ISrvSubaccCode getSrvTypeCode() {
    return this.srvTypeCode;
  }

  /**
   * <p>Setter for srvTypeCode.</p>
   * @param pSrvTypeCode reference
   **/
  public final void setSrvTypeCode(final ISrvSubaccCode pSrvTypeCode) {
    this.srvTypeCode = pSrvTypeCode;
  }

  /**
   * <p>Getter for srvSalesInvoice.</p>
   * @return SrvSalesInvoice
   **/
  public final SrvSalesInvoice getSrvSalesInvoice() {
    return this.srvSalesInvoice;
  }

  /**
   * <p>Setter for srvSalesInvoice.</p>
   * @param pSrvSalesInvoice reference
   **/
  public final void setSrvSalesInvoice(
    final SrvSalesInvoice pSrvSalesInvoice) {
    this.srvSalesInvoice = pSrvSalesInvoice;
  }
}
