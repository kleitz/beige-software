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
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.text.DateFormat;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.persistable.SalesReturn;
import org.beigesoft.accounting.persistable.SalesReturnLine;
import org.beigesoft.accounting.persistable.SalesReturnTaxLine;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.service.ISrvI18n;

/**
 * <p>Business service for vendor invoice.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers,
 * and here it not used yet.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvSalesReturn<RS>
  extends ASrvDocumentFull<RS, SalesReturn> {

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
  public SrvSalesReturn() {
    super(SalesReturn.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   * @param pSrvCogsEntry Draw material service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvSalesReturn(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry,
            final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry,
              final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    super(SalesReturn.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvWarehouseEntry, pSrvUseMaterialEntry, pSrvCogsEntry);
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
  public final SalesReturn createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    SalesReturn entity = new SalesReturn();
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
    final SalesReturn pEntity) throws Exception {
    pEntity.setTotalTaxes(BigDecimal.ZERO);
    pEntity.setSubtotal(BigDecimal.ZERO);
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
    final SalesReturn pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])
          && pEntity.getReversedId() != null) {
      //reverse none-reversed lines:
      List<SalesReturnLine> reversedLines = getSrvOrm().
        retrieveEntityOwnedlist(SalesReturnLine.class,
          SalesReturn.class, pEntity.getReversedId());
      for (SalesReturnLine reversedLine : reversedLines) {
        if (reversedLine.getReversedId() == null) {
          if (!reversedLine.getItsQuantity()
            .equals(reversedLine.getTheRest())) {
            throw new ExceptionWithCode(PurchaseInvoice.SOURSE_IS_IN_USE,
              "There is withdrawals from this source!");
          }
          SalesReturnLine reversingLine = new SalesReturnLine();
          reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
          reversingLine.setReversedId(reversedLine.getItsId());
          reversingLine.setWarehouseSite(reversedLine.getWarehouseSite());
          reversingLine.setInvItem(reversedLine.getInvItem());
          reversingLine.setUnitOfMeasure(reversedLine.getUnitOfMeasure());
          reversingLine.setItsCost(reversedLine.getItsCost());
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
          getSrvWarehouseEntry().load(pAddParam, reversingLine,
            reversingLine.getWarehouseSite());
          String descr;
          if (reversedLine.getDescription() == null) {
            descr = "";
          } else {
            descr = reversedLine.getDescription();
          }
          reversedLine.setDescription(descr
            + " reversing ID: " + reversingLine.getItsId());
          reversedLine.setReversedId(reversingLine.getItsId());
          reversedLine.setTheRest(BigDecimal.ZERO);
          getSrvOrm().updateEntity(reversedLine);
        }
      }
      List<SalesReturnTaxLine> reversedTaxLines = getSrvOrm().
        retrieveEntityOwnedlist(SalesReturnTaxLine.class,
          SalesReturn.class, pEntity.getReversedId());
      for (SalesReturnTaxLine reversedLine : reversedTaxLines) {
        if (reversedLine.getReversedId() == null) {
          SalesReturnTaxLine reversingLine = new SalesReturnTaxLine();
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
  }

  /**
   * <p>Additional check document for ready to account (make acc.entries).</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void addCheckIsReadyToAccount(final Map<String, ?> pAddParam,
    final SalesReturn pEntity) throws Exception {
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
    final SalesReturn pEntity,
      final SalesReturn pOldEntity) throws Exception {
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
    final SalesReturn pEntity) throws Exception {
    //nothing
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
