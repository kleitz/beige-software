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
import java.util.Map;
import java.math.BigDecimal;

import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.persistable.PurchaseInvoiceServiceLine;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Vendor Invoice Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvPurchaseInvoiceServiceLine<RS>
  extends ASrvAccEntitySimple<RS, PurchaseInvoiceServiceLine>
    implements ISrvEntityOwned<PurchaseInvoiceServiceLine, PurchaseInvoice> {

  /**
   * <p>It makes total for owner.</p>
   **/
  private UtlPurchaseGoodsServiceLine<RS> utlPurchaseGoodsServiceLine;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvPurchaseInvoiceServiceLine() {
    super(PurchaseInvoiceServiceLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pUtlPurchaseGoodsServiceLine UtlPurchaseGoodsServiceLine
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvPurchaseInvoiceServiceLine(final ISrvOrm<RS> pSrvOrm,
    final UtlPurchaseGoodsServiceLine<RS> pUtlPurchaseGoodsServiceLine,
      final ISrvAccSettings pSrvAccSettings) {
    super(PurchaseInvoiceServiceLine.class, pSrvOrm, pSrvAccSettings);
    this.utlPurchaseGoodsServiceLine = pUtlPurchaseGoodsServiceLine;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final PurchaseInvoiceServiceLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    PurchaseInvoiceServiceLine entity = new PurchaseInvoiceServiceLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    PurchaseInvoice itsOwner = new PurchaseInvoice();
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
  public final PurchaseInvoiceServiceLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    PurchaseInvoiceServiceLine entity = getSrvOrm().retrieveCopyEntity(
      PurchaseInvoiceServiceLine.class, pId);
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
    final PurchaseInvoiceServiceLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getItsCost().doubleValue() <= 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "cost_less_or_eq_zero::" + pAddParam.get("user"));
    }
    //SQL refresh:
    pEntity.setService(getSrvOrm().retrieveEntity(pEntity.getService()));
    PurchaseInvoice itsOwner = getSrvOrm().retrieveEntityById(
      PurchaseInvoice.class, pEntity.getItsOwner().getItsId());
    pEntity.setItsOwner(itsOwner);
    //rounding:
    pEntity.setItsCost(pEntity.getItsCost().setScale(getSrvAccSettings()
        .lazyGetAccSettings().getCostPrecision(), getSrvAccSettings()
          .lazyGetAccSettings().getRoundingMode()));
    BigDecimal totalTaxes = BigDecimal.ZERO;
    String taxesDescription = "";
    if (getSrvAccSettings().lazyGetAccSettings()
      .getIsExtractSalesTaxFromPurchase()
        && pEntity.getService().getTaxCategory() != null) {
      List<InvItemTaxCategoryLine> pstl = getSrvOrm()
        .retrieveListWithConditions(
          InvItemTaxCategoryLine.class, "where ITSOWNER="
            + pEntity.getService().getTaxCategory().getItsId());
      BigDecimal bigDecimal100 = new BigDecimal("100.00");
      StringBuffer sb = new StringBuffer();
      int i = 0;
      for (InvItemTaxCategoryLine pst : pstl) {
        if (ETaxType.SALES_TAX_OUTITEM.equals(pst.getTax().getItsType())
          || ETaxType.SALES_TAX_INITEM.equals(pst.getTax().getItsType())) {
          BigDecimal addTx = pEntity.getItsCost().multiply(pst
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
    pEntity.setItsTotal(pEntity.getItsCost().add(totalTaxes));
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
    this.utlPurchaseGoodsServiceLine.updateOwner(pEntity.getItsOwner());
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final PurchaseInvoiceServiceLine retrieveEntity(
    final Map<String, Object> pAddParam,
      final PurchaseInvoiceServiceLine pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
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
  public final PurchaseInvoiceServiceLine retrieveEntityById(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final PurchaseInvoiceServiceLine pEntity) throws Exception {
    deleteEntity(pAddParam, pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    PurchaseInvoiceServiceLine entity = retrieveEntityById(pAddParam, pId);
    getSrvOrm().deleteEntity(getEntityClass(), pId);
    this.utlPurchaseGoodsServiceLine.updateOwner(entity.getItsOwner());
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
  public final PurchaseInvoiceServiceLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    PurchaseInvoiceServiceLine entity = new PurchaseInvoiceServiceLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    PurchaseInvoice itsOwner = new PurchaseInvoice();
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
  public final PurchaseInvoiceServiceLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final PurchaseInvoice pEntityItsOwner) throws Exception {
    PurchaseInvoiceServiceLine entity = new PurchaseInvoiceServiceLine();
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
  public final List<PurchaseInvoiceServiceLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(PurchaseInvoiceServiceLine.class,
      PurchaseInvoice.class, pIdEntityItsOwner);
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
  public final List<PurchaseInvoiceServiceLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final PurchaseInvoice pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(PurchaseInvoiceServiceLine.class,
      PurchaseInvoice.class, pEntityItsOwner.getItsId());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for utlPurchaseGoodsServiceLine.</p>
   * @return UtlPurchaseGoodsServiceLine<RS>
   **/
  public final UtlPurchaseGoodsServiceLine<RS>
    getUtlPurchaseGoodsServiceLine() {
    return this.utlPurchaseGoodsServiceLine;
  }

  /**
   * <p>Setter for utlPurchaseGoodsServiceLine.</p>
   * @param pUtlPurchaseGoodsServiceLine reference
   **/
  public final void setUtlPurchaseGoodsServiceLine(
    final UtlPurchaseGoodsServiceLine<RS> pUtlPurchaseGoodsServiceLine) {
    this.utlPurchaseGoodsServiceLine = pUtlPurchaseGoodsServiceLine;
  }
}
