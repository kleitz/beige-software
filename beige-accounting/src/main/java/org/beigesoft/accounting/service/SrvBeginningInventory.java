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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.BeginningInventory;
import org.beigesoft.accounting.persistable.BeginningInventoryLine;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Beginning Inventory.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvBeginningInventory<RS>
  extends ASrvDocumentFull<RS, BeginningInventory> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvBeginningInventory() {
    super(BeginningInventory.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   * @param pSrvCogsEntry Draw material service
   **/
  public SrvBeginningInventory(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry,
            final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(BeginningInventory.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvWarehouseEntry, pSrvUseMaterialEntry, pSrvCogsEntry);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final BeginningInventory createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    BeginningInventory entity = new BeginningInventory();
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
    final BeginningInventory pEntity) throws Exception {
    //nothing
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
    final BeginningInventory pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])
          && pEntity.getReversedId() != null) {
      //reverse none-reversed lines:
      List<BeginningInventoryLine> reversedLines = getSrvOrm().
        retrieveEntityOwnedlist(BeginningInventoryLine.class,
          BeginningInventory.class, pEntity.getReversedId());
      for (BeginningInventoryLine reversedLine : reversedLines) {
        if (reversedLine.getReversedId() == null) {
          if (!reversedLine.getItsQuantity()
            .equals(reversedLine.getTheRest())) {
            throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
              "where_is_withdrawals_from_this_source");
          }
          BeginningInventoryLine reversingLine = new BeginningInventoryLine();
          reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
          reversingLine.setReversedId(reversedLine.getItsId());
          reversingLine.setWarehouseSite(reversedLine.getWarehouseSite());
          reversingLine.setInvItem(reversedLine.getInvItem());
          reversingLine.setUnitOfMeasure(reversedLine.getUnitOfMeasure());
          reversingLine.setItsCost(reversedLine.getItsCost());
          reversingLine.setItsQuantity(reversedLine.getItsQuantity()
            .negate());
          reversingLine.setItsTotal(reversedLine.getItsTotal().negate());
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
      final BeginningInventory pEntity) throws Exception {
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
    final BeginningInventory pEntity,
      final BeginningInventory pOldEntity) throws Exception {
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
    final BeginningInventory pEntity) throws Exception {
    //nothing
  }
}
