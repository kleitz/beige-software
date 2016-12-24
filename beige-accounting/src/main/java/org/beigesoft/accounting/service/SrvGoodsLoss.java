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

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;

import org.beigesoft.accounting.persistable.GoodsLoss;
import org.beigesoft.accounting.persistable.GoodsLossLine;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.service.ISrvI18n;

/**
 * <p>Business service for goods loss document.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers,
 * and here it not used yet.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvGoodsLoss<RS>
  extends ASrvDocumentCogs<RS, GoodsLoss> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvGoodsLoss() {
    super(GoodsLoss.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw merchandise service
   **/
  public SrvGoodsLoss(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings, final ISrvAccEntry pSrvAccEntry,
      final ISrvI18n pSrvI18n, final DateFormat pDateFormatter,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(GoodsLoss.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvI18n, pDateFormatter,
        pSrvWarehouseEntry, pSrvCogsEntry);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final GoodsLoss createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    GoodsLoss entity = new GoodsLoss();
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
    final GoodsLoss pEntity) throws Exception {
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
    final GoodsLoss pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])
          && pEntity.getReversedId() != null) {
      //reverse none-reversed lines:
      List<GoodsLossLine> reversedLines = getSrvOrm().
        retrieveEntityOwnedlist(GoodsLossLine.class,
          GoodsLoss.class, pEntity.getReversedId());
      for (GoodsLossLine reversedLine : reversedLines) {
        if (reversedLine.getReversedId() == null) {
          GoodsLossLine reversingLine = new GoodsLossLine();
          reversingLine.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
          reversingLine.setReversedId(reversedLine.getItsId());
          reversingLine.setInvItem(reversedLine.getInvItem());
          reversingLine.setUnitOfMeasure(reversedLine.getUnitOfMeasure());
          reversingLine.setItsQuantity(reversedLine.getItsQuantity()
            .negate());
          reversingLine.setIsNew(true);
          reversingLine.setItsOwner(pEntity);
          reversingLine.setDescription(getSrvI18n().getMsg("reversed_n")
            + reversedLine.getIdDatabaseBirth() + "-"
              + reversedLine.getItsId()); //local
          getSrvOrm().insertEntity(reversingLine);
          getSrvWarehouseEntry().reverseDraw(pAddParam, reversingLine);
          getSrvCogsEntry().reverseDraw(pAddParam, reversingLine,
            pEntity.getItsDate(), pEntity.getItsId());
          String descr;
          if (reversedLine.getDescription() == null) {
            descr = "";
          } else {
            descr = reversedLine.getDescription();
          }
          reversedLine.setDescription(descr + " " + getSrvI18n()
            .getMsg("reversing_n") + reversingLine.getIdDatabaseBirth()
              + "-" + reversingLine.getItsId());
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
      final GoodsLoss pEntity) throws Exception {
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
    final GoodsLoss pEntity,
      final GoodsLoss pOldEntity) throws Exception {
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
    final GoodsLoss pEntity) throws Exception {
    //nothing
  }
}
