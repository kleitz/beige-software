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
import java.util.List;

import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.accounting.persistable.PurchaseReturn;
import org.beigesoft.accounting.persistable.PurchaseReturnTaxLine;
import org.beigesoft.accounting.persistable.PurchaseReturnLine;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Purchase Return.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvPurchaseReturn<RS>
  extends ASrvDocumentUseMaterial<RS, PurchaseReturn> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvPurchaseReturn() {
    super(PurchaseReturn.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   **/
  public SrvPurchaseReturn(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings, final ISrvAccEntry pSrvAccEntry,
      final ISrvWarehouseEntry pSrvWarehouseEntry,
        final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry) {
    super(PurchaseReturn.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvWarehouseEntry, pSrvUseMaterialEntry);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final PurchaseReturn createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    PurchaseReturn entity = new PurchaseReturn();
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
    final PurchaseReturn pEntity) throws Exception {
    // nothing
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
    final PurchaseReturn pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])
          && pEntity.getReversedId() != null) {
      //reverse none-reversed lines:
      List<PurchaseReturnLine> reversedLines = getSrvOrm().
        retrieveEntityOwnedlist(PurchaseReturnLine.class,
          PurchaseReturn.class, pEntity.getReversedId());
      for (PurchaseReturnLine reversedLine : reversedLines) {
        if (reversedLine.getReversedId() == null) {
          PurchaseReturnLine reversingLine = new PurchaseReturnLine();
          reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
          reversingLine.setReversedId(reversedLine.getItsId());
          reversingLine.setPurchaseInvoiceLine(reversedLine
            .getPurchaseInvoiceLine());
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
          getSrvUseMaterialEntry().reverseDraw(pAddParam, reversingLine,
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
      List<PurchaseReturnTaxLine> reversedTaxLines = getSrvOrm().
        retrieveEntityOwnedlist(PurchaseReturnTaxLine.class,
          PurchaseReturn.class, pEntity.getReversedId());
      for (PurchaseReturnTaxLine reversedLine : reversedTaxLines) {
        if (reversedLine.getReversedId() == null) {
          PurchaseReturnTaxLine reversingLine = new PurchaseReturnTaxLine();
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
  public final void addCheckIsReadyToAccount(
    final Map<String, Object> pAddParam,
      final PurchaseReturn pEntity) throws Exception {
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
    final PurchaseReturn pEntity,
      final PurchaseReturn pOldEntity) throws Exception {
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
    final PurchaseReturn pEntity) throws Exception {
    //nothing
  }
}
