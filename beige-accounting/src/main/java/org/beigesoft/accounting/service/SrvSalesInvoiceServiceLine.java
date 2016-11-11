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
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.SalesInvoiceServiceLine;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Sales Invoice Service Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvSalesInvoiceServiceLine<RS>
  extends ASrvAccEntitySimple<RS, SalesInvoiceServiceLine>
    implements ISrvEntityOwned<SalesInvoiceServiceLine, SalesInvoice> {

  /**
   * <p>It makes total for owner.</p>
   **/
  private UtlSalesGoodsServiceLine<RS> utlSalesGoodsServiceLine;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvSalesInvoiceServiceLine() {
    super(SalesInvoiceServiceLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pUtlSalesGoodsServiceLine UtlSalesGoodsServiceLine
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvSalesInvoiceServiceLine(final ISrvOrm<RS> pSrvOrm,
    final UtlSalesGoodsServiceLine<RS> pUtlSalesGoodsServiceLine,
      final ISrvAccSettings pSrvAccSettings) {
    super(SalesInvoiceServiceLine.class, pSrvOrm, pSrvAccSettings);
    this.utlSalesGoodsServiceLine = pUtlSalesGoodsServiceLine;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final SalesInvoiceServiceLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    SalesInvoiceServiceLine entity = new SalesInvoiceServiceLine();
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
  public final SalesInvoiceServiceLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    SalesInvoiceServiceLine entity = getSrvOrm().retrieveCopyEntity(
      SalesInvoiceServiceLine.class, pId);
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
    final SalesInvoiceServiceLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getItsPrice().doubleValue() <= 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "price_less_or_eq_zero::" + pAddParam.get("user"));
    }
    //SQL refresh:
    pEntity.setService(getSrvOrm().retrieveEntity(pEntity.getService()));
    SalesInvoice itsOwner = getSrvOrm().retrieveEntityById(
      SalesInvoice.class, pEntity.getItsOwner().getItsId());
    pEntity.setItsOwner(itsOwner);
    //rounding:
    pEntity.setItsPrice(pEntity.getItsPrice().setScale(getSrvAccSettings()
        .lazyGetAccSettings().getPricePrecision(), getSrvAccSettings()
          .lazyGetAccSettings().getRoundingMode()));
    BigDecimal totalTaxes = BigDecimal.ZERO;
    String taxesDescription = "";
    if (getSrvAccSettings().lazyGetAccSettings()
      .getIsExtractSalesTaxFromSales()
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
          BigDecimal addTx = pEntity.getItsPrice().multiply(pst
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
    pEntity.setItsTotal(pEntity.getItsPrice().add(totalTaxes));
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
    this.utlSalesGoodsServiceLine.updateOwner(pEntity.getItsOwner());
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final SalesInvoiceServiceLine retrieveEntity(
    final Map<String, Object> pAddParam,
      final SalesInvoiceServiceLine pEntity) throws Exception {
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
  public final SalesInvoiceServiceLine retrieveEntityById(
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
    final SalesInvoiceServiceLine pEntity) throws Exception {
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
    SalesInvoiceServiceLine entity = retrieveEntityById(pAddParam, pId);
    getSrvOrm().deleteEntity(getEntityClass(), pId);
    this.utlSalesGoodsServiceLine.updateOwner(entity.getItsOwner());
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
  public final SalesInvoiceServiceLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    SalesInvoiceServiceLine entity = new SalesInvoiceServiceLine();
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
  public final SalesInvoiceServiceLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final SalesInvoice pEntityItsOwner) throws Exception {
    SalesInvoiceServiceLine entity = new SalesInvoiceServiceLine();
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
  public final List<SalesInvoiceServiceLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(SalesInvoiceServiceLine.class,
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
  public final List<SalesInvoiceServiceLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final SalesInvoice pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(SalesInvoiceServiceLine.class,
      SalesInvoice.class, pEntityItsOwner.getItsId());
  }

  //Simple getters and setters:
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
