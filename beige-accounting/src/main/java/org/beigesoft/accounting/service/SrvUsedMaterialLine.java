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
import java.math.BigDecimal;
import java.util.Map;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.ManufacturingProcess;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.persistable.UsedMaterialLine;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for Customer Invoice Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvUsedMaterialLine<RS>
  extends ASrvAccEntityImmutable<RS, UsedMaterialLine>
    implements ISrvEntityOwned<UsedMaterialLine, ManufacturingProcess> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Business service for draw warehouse item for manufacture product.</p>
   **/
  private ISrvDrawItemEntry<UseMaterialEntry> srvUseMaterialEntry;

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvUsedMaterialLine() {
    super(UsedMaterialLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry draw material service
   **/
  public SrvUsedMaterialLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvWarehouseEntry pSrvWarehouseEntry,
        final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry) {
    super(UsedMaterialLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
    this.srvWarehouseEntry = pSrvWarehouseEntry;
    this.srvUseMaterialEntry = pSrvUseMaterialEntry;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final UsedMaterialLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    UsedMaterialLine entity = new UsedMaterialLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    ManufacturingProcess itsOwner = new ManufacturingProcess();
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final UsedMaterialLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    UsedMaterialLine entity = getSrvOrm().retrieveCopyEntity(
      UsedMaterialLine.class, pId);
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "reverse".equals(parameterMap.get("actionAdd")[0])) {
      if (entity.getReversedId() != null) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Attempt to double reverse" + pAddParam.get("user"));
      }
      entity.setReversedId(Long.valueOf(pId.toString()));
      entity.setItsQuantity(entity.getItsQuantity().negate());
      entity.setItsTotal(entity.getItsTotal().negate());
    } else {
      entity.setItsQuantity(BigDecimal.ZERO);
      entity.setItsCost(BigDecimal.ZERO);
      entity.setItsTotal(BigDecimal.ZERO);
    }
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Insert immutable line into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, Object> pAddParam,
    final UsedMaterialLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      if (pEntity.getItsQuantity().doubleValue() == 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Quantity is 0! " + pAddParam.get("user"));
      }
      if (pEntity.getItsQuantity().doubleValue() < 0 && pEntity
        .getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Reversed Line is null! " + pAddParam.get("user"));
      }
      ManufacturingProcess itsOwner = getSrvOrm().retrieveEntityById(
        ManufacturingProcess.class, pEntity.getItsOwner().getItsId());
      if (itsOwner.getIsComplete()) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Attempt to update completed manufacture " + pAddParam.get("user"));
      }
      InvItem invItem = getSrvOrm().retrieveEntityById(
          InvItem.class, pEntity.getInvItem().getItsId());
      if (!InvItem.MATERIAL_ID.equals(invItem.getItsType().getItsId())) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "type_must_be_material");
      }
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      getSrvOrm().insertEntity(pEntity);
      pEntity.setItsOwner(itsOwner);
      if (pEntity.getReversedId() != null) {
        UsedMaterialLine reversed = getSrvOrm().retrieveEntityById(
          UsedMaterialLine.class, pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "Attempt to double reverse" + pAddParam.get("user"));
        }
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(reversed);
        srvWarehouseEntry.reverseDraw(pAddParam, pEntity);
        srvUseMaterialEntry.reverseDraw(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      } else {
        srvWarehouseEntry.withdrawal(pAddParam, pEntity);
        srvUseMaterialEntry.withdrawal(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      }
      //Total line:
      String query =
      "select sum(ITSTOTAL) as ITSTOTAL from"
        + " USEMATERIALENTRY where DRAWINGID=" + pEntity.getItsId()
          + " and DRAWINGTYPE=" + pEntity.constTypeCode();
      Double total = getSrvDatabase().evalDoubleResult(query, "ITSTOTAL");
      pEntity.setItsTotal(BigDecimal.valueOf(total)
        .setScale(getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      pEntity.setItsCost(pEntity.getItsTotal().divide(pEntity
        .getItsQuantity(), getSrvAccSettings()
          .lazyGetAccSettings().getCostPrecision(),
            getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      getSrvOrm().updateEntity(pEntity);
      //Total document:
      query =
      "select sum(ITSTOTAL) as ITSTOTAL from"
        + " USEDMATERIALLINE where ITSOWNER="
          + pEntity.getItsOwner().getItsId();
      total = getSrvDatabase().evalDoubleResult(query, "ITSTOTAL");
      itsOwner.setTotalMaterialsCost(BigDecimal.valueOf(total)
        .setScale(getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      itsOwner.setItsTotal(itsOwner.getTotalMaterialsCost().
        add(itsOwner.getTotalAdditionCost()));
      itsOwner.setItsCost(itsOwner.getItsTotal().divide(
        itsOwner.getItsQuantity(), getSrvAccSettings()
          .lazyGetAccSettings().getCostPrecision(),
            getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      getSrvOrm().updateEntity(itsOwner);
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "Attempt to update used material line by " + pAddParam.get("user"));
    }
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner entity itsOwner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final UsedMaterialLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    UsedMaterialLine entity = new UsedMaterialLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    ManufacturingProcess itsOwner = new ManufacturingProcess();
    itsOwner.setItsId(Long.valueOf(pIdEntityItsOwner.toString()));
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final UsedMaterialLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final ManufacturingProcess pEntityItsOwner) throws Exception {
    UsedMaterialLine entity = new UsedMaterialLine();
    entity.setIsNew(true);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsOwner(pEntityItsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner ID itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<UsedMaterialLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(UsedMaterialLine.class,
      ManufacturingProcess.class, pIdEntityItsOwner);
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<UsedMaterialLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final ManufacturingProcess pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(UsedMaterialLine.class,
      ManufacturingProcess.class, pEntityItsOwner.getItsId());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvUseMaterialEntry.</p>
   * @return ISrvDrawItemEntry<UseMaterialEntry>
   **/
  public final ISrvDrawItemEntry<UseMaterialEntry> getSrvUseMaterialEntry() {
    return this.srvUseMaterialEntry;
  }

  /**
   * <p>Setter for srvUseMaterialEntry.</p>
   * @param pSrvUseMaterialEntry reference
   **/
  public final void setSrvUseMaterialEntry(
    final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry) {
    this.srvUseMaterialEntry = pSrvUseMaterialEntry;
  }

  /**
   * <p>Geter for srvWarehouseEntry.</p>
   * @return ISrvWarehouseEntry
   **/
  public final ISrvWarehouseEntry getSrvWarehouseEntry() {
    return this.srvWarehouseEntry;
  }

  /**
   * <p>Setter for srvWarehouseEntry.</p>
   * @param pSrvWarehouseEntry reference
   **/
  public final void setSrvWarehouseEntry(
    final ISrvWarehouseEntry pSrvWarehouseEntry) {
    this.srvWarehouseEntry = pSrvWarehouseEntry;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }
}
