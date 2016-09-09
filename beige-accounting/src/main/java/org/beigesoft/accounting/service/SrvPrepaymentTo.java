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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.holder.IAttributes;
import org.beigesoft.accounting.persistable.PrepaymentTo;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for prepayment for purchase.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers,
 * and here it not used yet.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvPrepaymentTo<RS>
  extends ASrvDocument<RS, PrepaymentTo> {

  /**
   * <p>Type Codes of sub-accounts service.</p>
   **/
  private ISrvSubaccCode srvTypeCode;
  /**
   * <p>minimum constructor.</p>
   **/
  public SrvPrepaymentTo() {
    super(PrepaymentTo.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvTypeCodeSubacc Type Codes of sub-accounts service
   **/
  public SrvPrepaymentTo(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvSubaccCode pSrvTypeCodeSubacc) {
    super(PrepaymentTo.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry);
    this.srvTypeCode = pSrvTypeCodeSubacc;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final PrepaymentTo createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    PrepaymentTo entity = new PrepaymentTo();
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
  public final void retrieveOtherDataFor(final Map<String, ?> pAddParam,
    final PrepaymentTo pEntity) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
  }

  /**
   * <p>Make additional preparations on entity copy.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeAddPrepareForCopy(final Map<String, ?> pAddParam,
    final PrepaymentTo pEntity) throws Exception {
    pEntity.setPurchaseInvoiceId(null);
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
  public final void makeOtherEntries(final Map<String, ?> pAddParam,
    final PrepaymentTo pEntity, final boolean pIsNew) throws Exception {
    //nothing
  }

  /**
   * <p>Additional check document for ready to account (make acc.entries).</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void addCheckIsReadyToAccount(final Map<String, ?> pAddParam,
    final PrepaymentTo pEntity) throws Exception {
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
    final PrepaymentTo pEntity,
      final PrepaymentTo pOldEntity) throws Exception {
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
    final PrepaymentTo pEntity) throws Exception {
    //BeigeORM refresh:
    pEntity.setAccCash(getSrvOrm()
      .retrieveEntity(pEntity.getAccCash()));
    if (pEntity.getAccCash().getSubaccType() != null
      && pEntity.getSubaccCashId() == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "select_subaccount");
    }
  }

  /**
   * <p>Added source types.</p>
   * @param pAddParam additional param
   */
  public final void addTypeCodeIntoAttrs(final Map<String, ?> pAddParam) {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("typeCodeSubaccMap",
      this.srvTypeCode.getTypeCodeMap());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvTypeCode.</p>
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
}
