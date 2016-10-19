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
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import java.math.BigDecimal;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.IDocWarehouse;
import org.beigesoft.accounting.persistable.IMakingWarehouseEntry;
import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.persistable.WarehouseSite;
import org.beigesoft.accounting.persistable.WarehouseRest;
import org.beigesoft.accounting.persistable.WarehouseEntry;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for warehouse.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvWarehouseEntry<RS> implements ISrvWarehouseEntry {

  /**
   * <p>Business service for code - java type document map.</p>
   **/
  private ISrvTypeCode srvTypeCodeDocuments;

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Date Formatter.</p>
   **/
  private DateFormat dateFormatter;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvWarehouseEntry() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvTypeCodeDocuments Type Code Documents service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvWarehouseEntry(final ISrvOrm<RS> pSrvOrm,
    final ISrvTypeCode pSrvTypeCodeDocuments,
      final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    this.srvOrm = pSrvOrm;
    this.srvTypeCodeDocuments = pSrvTypeCodeDocuments;
    this.srvI18n = pSrvI18n;
    this.dateFormatter = pDateFormatter;
  }

  /**
   * <p>Load warehouse from outside with item or reverse a load.</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @param pWhSiteTo Site To
   * @throws Exception - an exception
   **/
  @Override
  public final void load(final Map<String, Object> pAddParam,
    final IMakingWarehouseEntry pEntity,
      final WarehouseSite pWhSiteTo) throws Exception {
    WarehouseEntry wms = null;
    if (pEntity.getReversedId() != null) {
      wms = getSrvOrm().retrieveEntityWithConditions(WarehouseEntry.class,
        " where SOURCETYPE=" + pEntity.constTypeCode()
        + " and SOURCEID=" + pEntity.getReversedId() + " and WAREHOUSESITETO="
          + pWhSiteTo.getItsId()  + " and INVITEM=" + pEntity.getInvItem()
        .getItsId());
      if (wms == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "cant_find_reverced_source");
      }
    }
    WarehouseEntry wm = new WarehouseEntry();
    wm.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    wm.setSourceId(pEntity.getItsId());
    wm.setSourceType(pEntity.constTypeCode());
    wm.setWarehouseSiteTo(pWhSiteTo);
    wm.setInvItem(pEntity.getInvItem());
    wm.setUnitOfMeasure(pEntity.getUnitOfMeasure());
    wm.setItsQuantity(pEntity.getItsQuantity());
    wm.setSourceOwnerId(pEntity.getOwnerId());
    wm.setSourceOwnerType(pEntity.getOwnerType());
    if (wms != null) {
      wm.setReversedId(wms.getItsId());
      wm.setDescription(makeDescription(pEntity) + " reversed entry ID: "
        + wms.getItsId());
    } else {
      wm.setDescription(makeDescription(pEntity));
    }
    getSrvOrm().insertEntity(wm);
    makeWarehouseRest(pAddParam, pEntity, pWhSiteTo, pEntity.getItsQuantity());
    if (wms != null) {
      wms.setReversedId(wm.getItsId());
      wms.setDescription(wms.getDescription() + " reversing entry ID: "
        + wm.getItsId());
      getSrvOrm().updateEntity(wms);
    }
  }

  /**
   * <p>Move item between warehouses/sites or reverse a move.</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @param pWhSiteFrom Site From
   * @param pWhSiteTo Site To
   * @throws Exception - an exception
   **/
  @Override
  public final void move(final Map<String, Object> pAddParam,
    final IMakingWarehouseEntry pEntity, final WarehouseSite pWhSiteFrom,
      final WarehouseSite pWhSiteTo) throws Exception {
    WarehouseEntry wms = null;
    if (pEntity.getReversedId() != null) {
      wms = getSrvOrm().retrieveEntityWithConditions(WarehouseEntry.class,
        " where SOURCETYPE=" + pEntity.constTypeCode()
        + " and SOURCEID=" + pEntity.getReversedId());
    }
    WarehouseEntry wm = new WarehouseEntry();
    wm.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    wm.setSourceId(pEntity.getItsId());
    wm.setSourceType(pEntity.constTypeCode());
    wm.setWarehouseSiteFrom(pWhSiteFrom);
    wm.setWarehouseSiteTo(pWhSiteTo);
    wm.setInvItem(pEntity.getInvItem());
    wm.setUnitOfMeasure(pEntity.getUnitOfMeasure());
    wm.setItsQuantity(pEntity.getItsQuantity());
    wm.setSourceOwnerId(pEntity.getOwnerId());
    wm.setSourceOwnerType(pEntity.getOwnerType());
    if (wms != null) {
      wm.setReversedId(wms.getItsId());
      wm.setDescription(makeDescription(pEntity) + " reversed entry ID: "
        + wms.getItsId());
    } else {
      wm.setDescription(makeDescription(pEntity));
    }
    getSrvOrm().insertEntity(wm);
    makeWarehouseRest(pAddParam, pEntity, pWhSiteFrom,
      pEntity.getItsQuantity());
    makeWarehouseRest(pAddParam, pEntity, pWhSiteTo, pEntity.getItsQuantity());
    if (wms != null) {
      wms.setReversedId(wm.getItsId());
      wms.setDescription(wms.getDescription() + " reversing entry ID: "
        + wm.getItsId());
      getSrvOrm().updateEntity(wms);
    }
  }

  /**
   * <p>Make warehouse rest (load/draw/reverse).</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @param pWhSite Site
   * @param pQuantity Quantity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeWarehouseRest(final Map<String, Object> pAddParam,
    final IMakingWarehouseEntry pEntity,
      final WarehouseSite pWhSite,
        final BigDecimal pQuantity) throws Exception {
    WarehouseRest wr = getSrvOrm().retrieveEntityWithConditions(
      WarehouseRest.class, "where WAREHOUSESITE="
        + pWhSite.getItsId() + " and INVITEM="
          + pEntity.getInvItem().getItsId() + " and UNITOFMEASURE="
            + pEntity.getUnitOfMeasure().getItsId());
    if (wr == null) {
      if (pQuantity.doubleValue() < 0) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Attempt to reverse non-existent good in wh-site!"
            + pAddParam.get("user"));
      }
      wr = new WarehouseRest();
      wr.setIsNew(true);
      wr.setWarehouseSite(pWhSite);
      wr.setUnitOfMeasure(pEntity.getUnitOfMeasure());
      wr.setInvItem(pEntity.getInvItem());
    }
    wr.setTheRest(wr.getTheRest().add(pQuantity));
    if (wr.getTheRest().doubleValue() < 0) {
      throw new ExceptionWithCode(PurchaseInvoice.THERE_IS_NO_GOODS,
        "there_is_no_goods_in_stock");
    }
    if (wr.getIsNew()) {
      getSrvOrm().insertEntity(wr);
    } else {
      String where = "INVITEM=" + wr.getInvItem().getItsId()
        + " and WAREHOUSESITE=" + wr.getWarehouseSite().getItsId()
           + " and UNITOFMEASURE=" + wr.getUnitOfMeasure().getItsId();
      getSrvOrm().updateEntityWhere(wr, where);
    }
  }

  /**
   * <p>Withdrawal warehouse item to outside (or use/loss).</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @throws Exception - an exception
   **/
  @Override
  public final void withdrawal(final Map<String, Object> pAddParam,
    final IMakingWarehouseEntry pEntity) throws Exception {
    List<WarehouseRest> wrl = getSrvOrm().retrieveListWithConditions(
      WarehouseRest.class, "where THEREST>0 and INVITEM="
        + pEntity.getInvItem().getItsId() + " and UNITOFMEASURE="
            + pEntity.getUnitOfMeasure().getItsId());
    BigDecimal theRest = BigDecimal.ZERO;
    for (WarehouseRest wr : wrl) {
      theRest = theRest.add(wr.getTheRest());
      if (theRest.compareTo(pEntity.getItsQuantity()) >= 0) {
        break;
      }
    }
    if (theRest.compareTo(pEntity.getItsQuantity()) < 0) {
      throw new ExceptionWithCode(PurchaseInvoice.THERE_IS_NO_GOODS,
        "there_is_no_goods_in_stock");
    }
    BigDecimal quantityToLeaveRest = pEntity.getItsQuantity();
    for (WarehouseRest wr : wrl) {
      if (quantityToLeaveRest.doubleValue() == 0) {
        break;
      }
      BigDecimal quantityToLeave;
      if (wr.getTheRest().compareTo(quantityToLeaveRest) <= 0) {
        quantityToLeave = wr.getTheRest();
      } else {
        quantityToLeave = quantityToLeaveRest;
      }
      wr.setTheRest(wr.getTheRest().subtract(quantityToLeave));
      String where = "INVITEM=" + wr.getInvItem().getItsId()
        + " and WAREHOUSESITE=" + wr.getWarehouseSite().getItsId()
          + " and UNITOFMEASURE=" + wr.getUnitOfMeasure().getItsId();
      getSrvOrm().updateEntityWhere(wr, where);
      WarehouseEntry wm = new WarehouseEntry();
      wm.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
      wm.setSourceId(pEntity.getItsId());
      wm.setSourceType(pEntity.constTypeCode());
      wm.setWarehouseSiteFrom(wr.getWarehouseSite());
      wm.setUnitOfMeasure(wr.getUnitOfMeasure());
      wm.setInvItem(wr.getInvItem());
      wm.setItsQuantity(quantityToLeave);
      wm.setSourceOwnerId(pEntity.getOwnerId());
      wm.setSourceOwnerType(pEntity.getOwnerType());
      wm.setDescription(makeDescription(pEntity));
      getSrvOrm().insertEntity(wm);
      quantityToLeaveRest = quantityToLeaveRest.subtract(quantityToLeave);
    }
  }

  /**
   * <p>Reverse a withdrawal warehouse.</p>
   * @param pAddParam additional param
   * @param pEntity movement
   * @throws Exception - an exception
   **/
  @Override
  public final void reverseDraw(final Map<String, Object> pAddParam,
    final IMakingWarehouseEntry pEntity) throws Exception {
    if (pEntity.getItsQuantity().doubleValue() > 0) {
      throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
        "Reversing source must has negative quantity!");
    }
    List<WarehouseEntry> wml = getSrvOrm().retrieveListWithConditions(
      WarehouseEntry.class, "where SOURCETYPE="
        + pEntity.constTypeCode() + " and SOURCEID="
          + pEntity.getReversedId() + " and INVITEM=" + pEntity.getInvItem()
        .getItsId() +  " and WAREHOUSESITEFROM is not null");
    BigDecimal quantityToLeaveRst = pEntity.getItsQuantity();
    for (WarehouseEntry wms : wml) {
      if (wms.getItsQuantity().doubleValue() < 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "attempt_to_reverse_reversed");
      }
      WarehouseEntry wm = new WarehouseEntry();
      wm.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
      wm.setSourceId(pEntity.getItsId());
      wm.setSourceType(pEntity.constTypeCode());
      wm.setWarehouseSiteFrom(wms.getWarehouseSiteFrom());
      wm.setUnitOfMeasure(wms.getUnitOfMeasure());
      wm.setInvItem(wms.getInvItem());
      wm.setItsQuantity(wms.getItsQuantity().negate());
      quantityToLeaveRst = quantityToLeaveRst.add(wms.getItsQuantity());
      if (quantityToLeaveRst.doubleValue() > 0) {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "Reversing source has different quantity against movement entries! "
            + pAddParam.get("user"));
      }
      wm.setSourceOwnerId(pEntity.getOwnerId());
      wm.setSourceOwnerType(pEntity.getOwnerType());
      wm.setReversedId(wms.getItsId());
      wm.setDescription(makeDescription(pEntity) + " reversed entry ID: "
        + wms.getItsId());
      getSrvOrm().insertEntity(wm);
      makeWarehouseRest(pAddParam, pEntity, wm.getWarehouseSiteFrom(),
        wms.getItsQuantity());
      wms.setReversedId(wm.getItsId());
      wms.setDescription(wms.getDescription() + " reversing entry ID: "
        + wm.getItsId());
      getSrvOrm().updateEntity(wms);
    }
    if (quantityToLeaveRst.doubleValue() != 0) {
      throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
        "Reversing source has different quantity against movement entries! "
          + pAddParam.get("user"));
    }
  }

  /**
   * <p>Retrieve entries for document.</p>
   * @param pAddParam additional param
   * @param pEntity a document
   * @return warehouse entries made by this document
   * @throws Exception - an exception
   **/
  @Override
  public final List<WarehouseEntry> retrieveEntriesFor(
    final Map<String, Object> pAddParam,
      final IDocWarehouse pEntity) throws Exception {
    String where = null;
    if (pEntity instanceof IMakingWarehouseEntry) {
      //e.g. Manufacture
      where = " where SOURCETYPE=" + pEntity.constTypeCode()
        + " and SOURCEID=" + pEntity.getItsId();
    }
    List<WarehouseEntry> result = null;
    if (where != null) {
      result = getSrvOrm().retrieveListWithConditions(WarehouseEntry.class,
        where);
    }
    if (pEntity.getLinesWarehouseType() != null) {
      //e.g. PurchaseInvoice
      where = " where SOURCEOWNERTYPE=" + pEntity.constTypeCode()
        + " and SOURCEOWNERID=" + pEntity.getItsId();
      if (result == null) {
        result = getSrvOrm().retrieveListWithConditions(WarehouseEntry.class,
          where);
      } else {
        result.addAll(getSrvOrm().retrieveListWithConditions(
          WarehouseEntry.class, where));
      }
    }
    return result;
  }

  //Utils:
  /**
   * <p>Make description for warehouse entry.</p>
   * @param pEntity movement
   * @return description
   **/
  public final String makeDescription(final IMakingWarehouseEntry pEntity) {
    String strWho = getSrvI18n().getMsg(pEntity.getClass().getSimpleName()
      + "short") + " ID: " + pEntity.getItsId();
    if (pEntity.getOwnerId() == null) {
      strWho += ", date: " + getDateFormatter().format(pEntity
        .getDocumentDate());
    } else {
      strWho += " in " + getSrvI18n().getMsg(getSrvTypeCodeDocuments()
        .getTypeCodeMap().get(pEntity.getOwnerType()).getSimpleName()
        + "short") + " ID, date: " + pEntity.getOwnerId() + ", "
        + getDateFormatter().format(pEntity.getDocumentDate());
    }
    return "Made at " + getDateFormatter().format(
      new Date()) + " by " + strWho;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvTypeCodeDocuments.</p>
   * @return ISrvTypeCode
   **/
  public final ISrvTypeCode getSrvTypeCodeDocuments() {
    return this.srvTypeCodeDocuments;
  }

  /**
   * <p>Setter for srvTypeCodeDocuments.</p>
   * @param pSrvTypeCodeDocuments reference
   **/
  public final void setSrvTypeCodeDocuments(
    final ISrvTypeCode pSrvTypeCodeDocuments) {
    this.srvTypeCodeDocuments = pSrvTypeCodeDocuments;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

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
