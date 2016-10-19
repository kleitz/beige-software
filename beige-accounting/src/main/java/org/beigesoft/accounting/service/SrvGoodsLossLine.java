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
import org.beigesoft.accounting.persistable.GoodsLoss;
import org.beigesoft.accounting.persistable.GoodsLossLine;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for Customer Invoice Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvGoodsLossLine<RS>
  extends ASrvAccEntityImmutable<RS, GoodsLossLine>
    implements ISrvEntityOwned<GoodsLossLine, GoodsLoss> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>Business service for draw any item to sale/loss/stole.</p>
   **/
  private ISrvDrawItemEntry<CogsEntry> srvCogsEntry;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvGoodsLossLine() {
    super(GoodsLossLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw material service
   **/
  public SrvGoodsLossLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvWarehouseEntry pSrvWarehouseEntry,
        final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(GoodsLossLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
    this.srvWarehouseEntry = pSrvWarehouseEntry;
    this.srvCogsEntry = pSrvCogsEntry;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final GoodsLossLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    GoodsLossLine entity = new GoodsLossLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    GoodsLoss itsOwner = new GoodsLoss();
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
  public final GoodsLossLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    GoodsLossLine entity = getSrvOrm().retrieveCopyEntity(
      GoodsLossLine.class, pId);
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
    } else {
      entity.setItsQuantity(BigDecimal.ZERO);
    }
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
    final GoodsLossLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      if (pEntity.getItsQuantity().doubleValue() == 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Quantity is 0!" + pAddParam.get("user"));
      }
      if (pEntity.getItsQuantity().doubleValue() < 0
        && pEntity.getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Reversed Line is null!" + pAddParam.get("user"));
      }
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      getSrvOrm().insertEntity(pEntity);
      GoodsLoss itsOwner = getSrvOrm().retrieveEntityById(
        GoodsLoss.class, pEntity.getItsOwner().getItsId());
      pEntity.setItsOwner(itsOwner);
      if (pEntity.getReversedId() != null) {
        GoodsLossLine reversed = getSrvOrm().retrieveEntityById(
          GoodsLossLine.class, pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "Attempt to double reverse" + pAddParam.get("user"));
        }
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(reversed);
        srvWarehouseEntry.reverseDraw(pAddParam, pEntity);
        srvCogsEntry.reverseDraw(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      } else {
        srvWarehouseEntry.withdrawal(pAddParam, pEntity);
        srvCogsEntry.withdrawal(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      }
      String query =
        "select  sum(ITSTOTAL) as ITSTOTAL from COGSENTRY where REVERSEDID"
          + " is null and DRAWINGTYPE=1005 and DRAWINGOWNERID="
            + pEntity.getItsOwner().getItsId();
      Double total = getSrvDatabase().evalDoubleResult(query, "ITSTOTAL");
      itsOwner.setItsTotal(BigDecimal.valueOf(total).setScale(
        getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      getSrvOrm().updateEntity(itsOwner);
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "Attempt to update sales invoice line by " + pAddParam.get("user"));
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
  public final GoodsLossLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    GoodsLossLine entity = new GoodsLossLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    GoodsLoss itsOwner = new GoodsLoss();
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
  public final GoodsLossLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final GoodsLoss pEntityItsOwner) throws Exception {
    GoodsLossLine entity = new GoodsLossLine();
    entity.setIsNew(true);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsOwner(pEntityItsOwner);
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
  public final List<GoodsLossLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(GoodsLossLine.class,
      GoodsLoss.class, pIdEntityItsOwner);
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
  public final List<GoodsLossLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final GoodsLoss pEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(GoodsLossLine.class,
      GoodsLoss.class, pEntityItsOwner.getItsId());
  }

  //Simple getters and setters:
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
   * <p>Getter for srvCogsEntry.</p>
   * @return ISrvDrawItemEntry<CogsEntry>
   **/
  public final ISrvDrawItemEntry<CogsEntry> getSrvCogsEntry() {
    return this.srvCogsEntry;
  }

  /**
   * <p>Setter for srvCogsEntry.</p>
   * @param pSrvCogsEntry reference
   **/
  public final void setSrvCogsEntry(
    final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    this.srvCogsEntry = pSrvCogsEntry;
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
