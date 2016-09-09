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

import org.beigesoft.holder.IAttributes;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.ManufacturingProcess;
import org.beigesoft.accounting.persistable.AdditionCostLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for Additional Cost Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvAdditionCostLine<RS>
  extends ASrvAccEntitySimple<RS, AdditionCostLine>
    implements ISrvEntityOwned<AdditionCostLine, ManufacturingProcess> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Type Codes of sub-accounts service.</p>
   **/
  private ISrvTypeCode srvTypeCode;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvAdditionCostLine() {
    super(AdditionCostLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvTypeCodeSubacc Type Codes of sub-accounts service
   **/
  public SrvAdditionCostLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvTypeCode pSrvTypeCodeSubacc) {
    super(AdditionCostLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
    this.srvTypeCode = pSrvTypeCodeSubacc;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final AdditionCostLine createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    AdditionCostLine entity = new AdditionCostLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    ManufacturingProcess itsOwner = new ManufacturingProcess();
    entity.setItsOwner(itsOwner);
    addTypeCodeIntoAttrs(pAddParam);
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
  public final AdditionCostLine retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    AdditionCostLine entity = getSrvOrm().retrieveCopyEntity(
      AdditionCostLine.class, pId);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final AdditionCostLine retrieveEntity(final Map<String, ?> pAddParam,
    final AdditionCostLine pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final AdditionCostLine retrieveEntityById(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(AdditionCostLine.class, pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final AdditionCostLine pEntity) throws Exception {
    if (pEntity.getItsOwner().getInvItem() == null) {
      ManufacturingProcess itsOwner = getSrvOrm().retrieveEntityById(
        ManufacturingProcess.class, pEntity.getItsOwner().getItsId());
      pEntity.setItsOwner(itsOwner);
    }
    if (pEntity.getItsOwner().getIsComplete()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "attempt_to_change_completed_manufacturing_process");
    }
    getSrvOrm().deleteEntity(AdditionCostLine.class, pEntity.getItsId());
    updateOwner(pEntity);
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    AdditionCostLine entity = getSrvOrm()
      .retrieveEntityById(AdditionCostLine.class, pId);
    if (entity.getItsOwner().getIsComplete()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "attempt_to_change_completed_manufacturing_process");
    }
    getSrvOrm().deleteEntity(AdditionCostLine.class, entity.getItsId());
    updateOwner(entity);
  }

  /**
   * <p>Insert immutable line into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, ?> pAddParam,
    final AdditionCostLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getItsTotal().doubleValue() == 0d) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "total_is_0");
    }
    ManufacturingProcess itsOwner = getSrvOrm().retrieveEntityById(
      ManufacturingProcess.class, pEntity.getItsOwner().getItsId());
    if (itsOwner.getIsComplete()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "attempt_to_change_completed_manufacturing_process");
    }
    pEntity.setItsOwner(itsOwner);
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
    updateOwner(pEntity);
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
  public final AdditionCostLine createEntityWithOwnerById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    AdditionCostLine entity = new AdditionCostLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    ManufacturingProcess itsOwner = new ManufacturingProcess();
    itsOwner.setItsId(Long.valueOf(pIdEntityItsOwner.toString()));
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
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
  public final AdditionCostLine createEntityWithOwner(
    final Map<String, ?> pAddParam,
      final ManufacturingProcess pEntityItsOwner) throws Exception {
    AdditionCostLine entity = new AdditionCostLine();
    entity.setIsNew(true);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsOwner(pEntityItsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
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
  public final List<AdditionCostLine> retrieveOwnedListById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(AdditionCostLine.class,
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
  public final List<AdditionCostLine> retrieveOwnedList(
    final Map<String, ?> pAddParam,
      final ManufacturingProcess pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(AdditionCostLine.class,
      ManufacturingProcess.class, pEntityItsOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Insert immutable line into DB.</p>
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  public final void updateOwner(
    final AdditionCostLine pEntity) throws Exception {
    String query =
    "select sum(ITSTOTAL) as ITSTOTAL from"
      + " ADDITIONCOSTLINE where ITSOWNER="
        + pEntity.getItsOwner().getItsId();
    Double total = getSrvDatabase().evalDoubleResult(query, "ITSTOTAL");
    pEntity.getItsOwner().setTotalAdditionCost(BigDecimal.valueOf(total));
    pEntity.getItsOwner().setItsTotal(pEntity.getItsOwner()
      .getTotalMaterialsCost().add(pEntity.getItsOwner()
        .getTotalAdditionCost()));
    pEntity.getItsOwner().setItsCost(pEntity.getItsOwner().getItsTotal()
      .divide(pEntity.getItsOwner().getItsQuantity(),
        getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    getSrvOrm().updateEntity(pEntity.getItsOwner());
  }

  /**
   * <p>Added source types.</p>
   * @param pAddParam additional param
   */
  public final void addTypeCodeIntoAttrs(final Map<String, ?> pAddParam) {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("typeCodeSubaccMap", srvTypeCode.getTypeCodeMap());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvTypeCode.</p>
   * @return ISrvTypeCode
   **/
  public final ISrvTypeCode getSrvTypeCode() {
    return this.srvTypeCode;
  }

  /**
   * <p>Setter for srvTypeCode.</p>
   * @param pSrvTypeCode reference
   **/
  public final void setSrvTypeCode(final ISrvTypeCode pSrvTypeCode) {
    this.srvTypeCode = pSrvTypeCode;
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
