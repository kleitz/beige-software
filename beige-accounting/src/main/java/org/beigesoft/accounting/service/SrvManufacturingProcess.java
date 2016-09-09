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

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Map;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.ManufacturingProcess;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.accounting.persistable.UsedMaterialLine;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for ManufacturingProcess that manufactures product.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvManufacturingProcess<RS>
  extends ASrvDocumentUseMaterial<RS, ManufacturingProcess> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvManufacturingProcess() {
    super(ManufacturingProcess.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   **/
  public SrvManufacturingProcess(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry) {
    super(ManufacturingProcess.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvWarehouseEntry, pSrvUseMaterialEntry);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final ManufacturingProcess createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    ManufacturingProcess entity = new ManufacturingProcess();
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
    final ManufacturingProcess pEntity) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "reverse".equals(parameterMap.get("actionAdd")[0])) {
      pEntity.setItsQuantity(pEntity.getItsQuantity().negate());
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "ManufacturingProcess copy action not supported! "
          + pAddParam.get("user"));
    }
    pEntity.setTheRest(BigDecimal.ZERO);
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
    final ManufacturingProcess pEntity, final boolean pIsNew) throws Exception {
    if (pEntity.getReversedId() != null) {
      pEntity.setTheRest(BigDecimal.ZERO);
      //reverse none-reversed lines:
      ManufacturingProcess reversed = getSrvOrm()
        .retrieveEntityById(ManufacturingProcess.class,
          pEntity.getReversedId());
      reversed.setTheRest(BigDecimal.ZERO);
      getSrvOrm().updateEntity(reversed);
      List<UsedMaterialLine> reversedMaterials = getSrvOrm().
        retrieveEntityOwnedlist(UsedMaterialLine.class,
          ManufacturingProcess.class, reversed.getItsId());
      for (UsedMaterialLine reversedLine : reversedMaterials) {
        if (reversedLine.getReversedId() == null) {
          UsedMaterialLine reversingLine = new UsedMaterialLine();
          reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
          reversingLine.setReversedId(reversedLine.getItsId());
          reversingLine.setItsCost(reversedLine.getItsCost());
          reversingLine.setInvItem(reversedLine.getInvItem());
          reversingLine.setUnitOfMeasure(reversedLine.getUnitOfMeasure());
          reversingLine.setItsQuantity(reversedLine.getItsQuantity().negate());
          reversingLine.setItsTotal(reversedLine.getItsTotal().negate());
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
      //Addition cost lines make only acc.entries, so nothing to do
    }
    if (pEntity.getIsComplete()) { //need to make warehouse entries
      if (pEntity.getItsCost().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "cost_less_or_eq_zero");
      }
      //load or reverse load:
      getSrvWarehouseEntry().load(pAddParam, pEntity,
        pEntity.getWarehouseSite());
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
    final ManufacturingProcess pEntity) throws Exception {
    if (!pEntity.getIsComplete()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "manufacturing_must_be_completed");
    }
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
    final ManufacturingProcess pEntity,
      final ManufacturingProcess pOldEntity) throws Exception {
    if (pOldEntity.getIsComplete()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_change_completed_manufacturing_process");
    }
  }

  /**
   * <p>Make save preparations before insert/update block if it's need.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeFirstPrepareForSave(final Map<String, ?> pAddParam,
    final ManufacturingProcess pEntity) throws Exception {
    if (pEntity.getItsQuantity().doubleValue() == 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "Quantity is 0! " + pAddParam.get("user"));
    }
    InvItem invItem = getSrvOrm().retrieveEntityById(
        InvItem.class, pEntity.getInvItem().getItsId());
    if (!InvItem.WORK_IN_PROGRESS_ID.equals(invItem.getItsType().getItsId())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "type_must_be_work_in_progress");
    }
    if (pEntity.getReversedId() != null) {
      pEntity.setIsComplete(true);
      pEntity.setTheRest(BigDecimal.ZERO);
    } else {
      pEntity.setTheRest(pEntity.getItsQuantity());
    }
    pEntity.setItsCost(pEntity.getItsTotal().divide(
      pEntity.getItsQuantity(), getSrvAccSettings()
        .lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
  }
}
