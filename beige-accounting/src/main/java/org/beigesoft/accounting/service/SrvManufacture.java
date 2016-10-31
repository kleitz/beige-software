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
import java.math.BigDecimal;
import java.text.DateFormat;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.ManufacturingProcess;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.persistable.Manufacture;
import org.beigesoft.accounting.model.ManufactureForDraw;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for manufacture that just transfers completed
 * product in progress into finished product.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvManufacture<RS>
  extends ASrvDocumentUseMaterial<RS, Manufacture> {

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
  public SrvManufacture() {
    super(Manufacture.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvManufacture(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry,
            final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    super(Manufacture.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvWarehouseEntry, pSrvUseMaterialEntry);
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
  public final Manufacture createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    Manufacture entity = new Manufacture();
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
    final Manufacture pEntity) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "reverse".equals(parameterMap.get("actionAdd")[0])) {
      pEntity.setItsQuantity(pEntity.getItsQuantity().negate());
    } else {
      pEntity.setItsQuantity(BigDecimal.ZERO);
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
  public final void makeOtherEntries(final Map<String, Object> pAddParam,
    final Manufacture pEntity, final boolean pIsNew) throws Exception {
    //always new
    ManufactureForDraw manufactureForDraw = new ManufactureForDraw(pEntity);
    if (pEntity.getReversedId() != null) {
      //reverse draw product in process from warehouse
      getSrvWarehouseEntry().reverseDraw(pAddParam, manufactureForDraw);
      //reverse draw product in process from manufacturing process
      useMaterialReverse(pEntity);
      //reverse acc.entries already done
    } else {
      //draw product in process from warehouse
      getSrvWarehouseEntry().withdrawal(pAddParam, manufactureForDraw,
        pEntity.getWarehouseSiteFo());
      //draw product in process from manufacturing process
      useMaterial(pEntity);
      //it will update this doc:
      getSrvAccEntry().makeEntries(pAddParam, pEntity);
    }
    //load(put) or reverse product or created material on warehouse
    getSrvWarehouseEntry().load(pAddParam, pEntity, pEntity.getWarehouseSite());
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
      final Manufacture pEntity) throws Exception {
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
    final Manufacture pEntity,
      final Manufacture pOldEntity) throws Exception {
    //never update
  }

  /**
   * <p>Make save preparations before insert/update block if it's need.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeFirstPrepareForSave(final Map<String, Object> pAddParam,
    final Manufacture pEntity) throws Exception {
    ManufacturingProcess mnfProcess = getSrvOrm().retrieveEntityById(
      ManufacturingProcess.class, pEntity.getManufacturingProcess().getItsId());
    InvItem invItem = getSrvOrm().retrieveEntityById(
        InvItem.class, pEntity.getInvItem().getItsId());
    pEntity.setManufacturingProcess(mnfProcess);
    pEntity.setInvItem(invItem);
    if (!(InvItem.FINISHED_PRODUCT_ID
      .equals(invItem.getItsType().getItsId()) || InvItem.MATERIAL_ID
      .equals(invItem.getItsType().getItsId()))) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "type_must_be_product_material");
    }
    if (pEntity.getItsQuantity().compareTo(pEntity.getManufacturingProcess()
      .getItsQuantity()) > 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "source_has_no_enough_item");
    }
    if (!pEntity.getUnitOfMeasure().getItsId().equals(pEntity
      .getManufacturingProcess().getUnitOfMeasure().getItsId())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "UnitOfMeasure_fiffer_with_source");
    }
    if (pEntity.getItsQuantity().doubleValue() < 0
      && pEntity.getReversedId() == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "reversed_manufacture_is_null");
    }
    pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
      getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
        getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    if (pEntity.getReversedId() != null) {
      Manufacture reversed = getSrvOrm().retrieveEntityById(
        Manufacture.class, pEntity.getReversedId());
      if (reversed.getReversedId() != null) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Attempt to double reverse" + pAddParam.get("user"));
      }
      if (!reversed.getItsQuantity().equals(reversed.getTheRest())) {
        throw new ExceptionWithCode(ExceptionWithCode
          .WRONG_PARAMETER, "where_is_withdrawals_from_this_source");
      }
      pEntity.setTheRest(BigDecimal.ZERO);
    } else {
      pEntity.setTheRest(pEntity.getItsQuantity());
    }
    pEntity.setItsCost(pEntity.getManufacturingProcess().getItsCost());
    pEntity.setItsTotal(pEntity.getItsCost()
      .multiply(pEntity.getItsQuantity()).setScale(
        getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
  }

  //Utils:
  /**
   * <p>Make description for warehouse entry.</p>
   * @param pEntity movement
   * @return description
   **/
  public final String makeDescription(final Manufacture pEntity) {
    String strWho = getSrvI18n().getMsg(pEntity.getClass().getSimpleName()
      + "short") + " ID: " + pEntity.getItsId() + ", date: "
        + getDateFormatter().format(pEntity.getItsDate());
    String strFrom = " from " + getSrvI18n().getMsg(ManufacturingProcess
      .class.getSimpleName()) + " ID: " + pEntity.getManufacturingProcess()
        .getItsId();
    return "Made at " + getDateFormatter().format(new Date()) + " by "
      + strWho + strFrom;
  }

  /**
   * <p>Make use material.</p>
   * @param pEntity Manufacture
   * @throws Exception - an exception
   **/
  public final void useMaterial(final Manufacture pEntity)
    throws Exception {
    //draw product in process from manufacturing process
    pEntity.getManufacturingProcess().setTheRest(pEntity
      .getManufacturingProcess().getTheRest()
        .subtract(pEntity.getItsQuantity()));
    getSrvOrm().updateEntity(pEntity.getManufacturingProcess());
    UseMaterialEntry die = new UseMaterialEntry();
    die.setItsDate(pEntity.getItsDate());
    die.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    die.setSourceType(pEntity.getManufacturingProcess().constTypeCode());
    die.setSourceId(pEntity.getManufacturingProcess().getItsId());
    die.setDrawingType(pEntity.constTypeCode());
    die.setDrawingId(pEntity.getItsId());
    die.setDrawingOwnerId(null);
    die.setDrawingOwnerType(null);
    die.setSourceOwnerId(null);
    die.setSourceOwnerType(null);
    die.setItsQuantity(pEntity.getItsQuantity());
    die.setItsCost(pEntity.getManufacturingProcess().getItsCost());
    die.setInvItem(pEntity.getManufacturingProcess().getInvItem());
    die.setUnitOfMeasure(pEntity.getManufacturingProcess().getUnitOfMeasure());
    die.setItsTotal(die.getItsCost().
      multiply(die.getItsQuantity()));
    die.setDescription(makeDescription(pEntity));
    getSrvOrm().insertEntity(die);
  }

  /**
   * <p>Make use material reverse.</p>
   * @param pEntity Manufacture
   * @throws Exception - an exception
   **/
  public final void useMaterialReverse(final Manufacture pEntity)
    throws Exception {
    //reverse draw product in process from manufacturing process
    UseMaterialEntry dies = getSrvOrm()
      .retrieveEntityWithConditions(UseMaterialEntry.class,
        " where DRAWINGTYPE=" + pEntity.constTypeCode()
          + " and DRAWINGID=" + pEntity.getReversedId());
    UseMaterialEntry die = new UseMaterialEntry();
    die.setItsDate(pEntity.getItsDate());
    die.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    die.setSourceType(dies.getSourceType());
    die.setSourceId(dies.getSourceId());
    die.setDrawingType(pEntity.constTypeCode());
    die.setDrawingId(pEntity.getItsId());
    die.setDrawingOwnerId(null);
    die.setDrawingOwnerType(null);
    die.setSourceOwnerId(dies.getSourceOwnerId());
    die.setSourceOwnerType(dies.getSourceOwnerType());
    die.setItsCost(dies.getItsCost());
    die.setItsTotal(dies.getItsTotal().negate());
    die.setUnitOfMeasure(dies.getUnitOfMeasure());
    die.setInvItem(dies.getInvItem());
    die.setItsQuantity(dies.getItsQuantity().negate());
    die.setReversedId(die.getItsId());
    die.setDescription(makeDescription(pEntity) + " reversed entry ID: "
      + dies.getItsId());
    getSrvOrm().insertEntity(die);
    pEntity.getManufacturingProcess().setTheRest(pEntity
      .getManufacturingProcess().getTheRest().add(dies.getItsQuantity()));
    getSrvOrm().updateEntity(pEntity.getManufacturingProcess());
    dies.setReversedId(die.getItsId());
    dies.setDescription(dies.getDescription() + " reversing entry ID: "
      + die.getItsId());
    getSrvOrm().updateEntity(dies);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvI18n.</p>
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
