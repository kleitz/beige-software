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

import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.SalesInvoiceLine;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Customer Invoice Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvSalesInvoiceLine<RS>
  extends ASrvAccEntityImmutable<RS, SalesInvoiceLine>
    implements ISrvEntityOwned<SalesInvoiceLine, SalesInvoice> {

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>Business service for draw any item to sale/loss/stole.</p>
   **/
  private ISrvDrawItemEntry<CogsEntry> srvCogsEntry;

  /**
   * <p>It makes total for owner.</p>
   **/
  private UtlSalesGoodsServiceLine<RS> utlSalesGoodsServiceLine;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvSalesInvoiceLine() {
    super(SalesInvoiceLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pUtlSalesGoodsServiceLine UtlSalesGoodsServiceLine
   * @param pSrvAccSettings AccSettings service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw material service
   **/
  public SrvSalesInvoiceLine(final ISrvOrm<RS> pSrvOrm,
    final UtlSalesGoodsServiceLine<RS> pUtlSalesGoodsServiceLine,
      final ISrvAccSettings pSrvAccSettings,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(SalesInvoiceLine.class, pSrvOrm, pSrvAccSettings);
    this.utlSalesGoodsServiceLine = pUtlSalesGoodsServiceLine;
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
  public final SalesInvoiceLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    SalesInvoiceLine entity = new SalesInvoiceLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    SalesInvoice itsOwner = new SalesInvoice();
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
  public final SalesInvoiceLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    SalesInvoiceLine entity = getSrvOrm().retrieveCopyEntity(
      SalesInvoiceLine.class, pId);
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "reverse".equals(parameterMap.get("actionAdd")[0])) {
      if (entity.getReversedId() != null) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "attempt_to_reverse_reversed::" + pAddParam.get("user"));
      }
      entity.setReversedId(Long.valueOf(pId.toString()));
      entity.setItsQuantity(entity.getItsQuantity().negate());
      entity.setSubtotal(entity.getSubtotal().negate());
      entity.setTotalTaxes(entity.getTotalTaxes().negate());
      entity.setItsTotal(entity.getItsTotal().negate());
    } else {
      entity.setItsQuantity(BigDecimal.ZERO);
      entity.setItsPrice(BigDecimal.ZERO);
      entity.setItsTotal(BigDecimal.ZERO);
      entity.setTotalTaxes(BigDecimal.ZERO);
      entity.setSubtotal(BigDecimal.ZERO);
      entity.setTaxesDescription(null);
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
    final SalesInvoiceLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      if (pEntity.getItsQuantity().doubleValue() <= 0
        && pEntity.getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "quantity_less_or_equal_zero::" + pAddParam.get("user"));
      }
      if (pEntity.getItsPrice().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "price_less_eq_0::" + pAddParam.get("user"));
      }
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      pEntity.setItsPrice(pEntity.getItsPrice().setScale(getSrvAccSettings()
          .lazyGetAccSettings().getPricePrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
      pEntity.setSubtotal(pEntity.getItsTotal().setScale(getSrvAccSettings()
          .lazyGetAccSettings().getPricePrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
      BigDecimal totalTaxes = BigDecimal.ZERO;
      //SQL refresh:
      pEntity.setInvItem(getSrvOrm().retrieveEntity(pEntity.getInvItem()));
      if (getSrvAccSettings().lazyGetAccSettings()
        .getIsExtractSalesTaxFromSales()) {
        String taxesDescription = "";
        if (pEntity.getInvItem().getTaxCategory() != null) {
          List<InvItemTaxCategoryLine> pstl = getSrvOrm()
            .retrieveListWithConditions(
              InvItemTaxCategoryLine.class, "where ITSOWNER="
                + pEntity.getInvItem().getTaxCategory().getItsId());
          BigDecimal bigDecimal100 = new BigDecimal("100.00");
          StringBuffer sb = new StringBuffer();
          int i = 0;
          for (InvItemTaxCategoryLine pst : pstl) {
            if (ETaxType.SALES_TAX_OUTITEM.equals(pst.getTax().getItsType())
              || ETaxType.SALES_TAX_INITEM.equals(pst.getTax().getItsType())) {
              BigDecimal addTx = pEntity.getSubtotal().multiply(pst
                .getItsPercentage()).divide(bigDecimal100, getSrvAccSettings()
                  .lazyGetAccSettings().getPricePrecision(),
                    getSrvAccSettings().lazyGetAccSettings().getRoundingMode());
              totalTaxes = totalTaxes.add(addTx);
              if (i++ > 0) {
                sb.append(", ");
              }
              sb.append(pst.getTax().getItsName() + " " + pst.getItsPercentage()
                + "%=" + addTx);
            }
          }
          taxesDescription = sb.toString();
        }
        pEntity.setTaxesDescription(taxesDescription);
        pEntity.setTotalTaxes(totalTaxes);
      }
      pEntity.setItsTotal(pEntity.getSubtotal().add(totalTaxes));
      getSrvOrm().insertEntity(pEntity);
      SalesInvoice itsOwner = getSrvOrm().retrieveEntityById(
        SalesInvoice.class, pEntity.getItsOwner().getItsId());
      pEntity.setItsOwner(itsOwner);
      if (pEntity.getReversedId() != null) {
        SalesInvoiceLine reversed = getSrvOrm().retrieveEntityById(
          SalesInvoiceLine.class, pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "attempt_to_reverse_reversed::" + pAddParam.get("user"));
        }
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(reversed);
        srvWarehouseEntry.reverseDraw(pAddParam, pEntity);
        srvCogsEntry.reverseDraw(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      } else {
        srvWarehouseEntry.withdrawal(pAddParam, pEntity,
          pEntity.getWarehouseSiteFo());
        srvCogsEntry.withdrawal(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      }
      this.utlSalesGoodsServiceLine.updateOwner(pEntity.getItsOwner());
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "edit_not_allowed::" + pAddParam.get("user"));
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
  public final SalesInvoiceLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    SalesInvoiceLine entity = new SalesInvoiceLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    SalesInvoice itsOwner = new SalesInvoice();
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
  public final SalesInvoiceLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final SalesInvoice pEntityItsOwner) throws Exception {
    SalesInvoiceLine entity = new SalesInvoiceLine();
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
  public final List<SalesInvoiceLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(SalesInvoiceLine.class,
      SalesInvoice.class, pIdEntityItsOwner);
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
  public final List<SalesInvoiceLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final SalesInvoice pEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(SalesInvoiceLine.class,
      SalesInvoice.class, pEntityItsOwner.getItsId());
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
   * <p>Getter for utlSalesGoodsServiceLine.</p>
   * @return UtlSalesGoodsServiceLine<RS>
   **/
  public final UtlSalesGoodsServiceLine<RS>
    getUtlSalesGoodsServiceLine() {
    return this.utlSalesGoodsServiceLine;
  }

  /**
   * <p>Setter for utlSalesGoodsServiceLine.</p>
   * @param pUtlSalesGoodsServiceLine reference
   **/
  public final void setUtlSalesGoodsServiceLine(
    final UtlSalesGoodsServiceLine<RS> pUtlSalesGoodsServiceLine) {
    this.utlSalesGoodsServiceLine = pUtlSalesGoodsServiceLine;
  }
}
