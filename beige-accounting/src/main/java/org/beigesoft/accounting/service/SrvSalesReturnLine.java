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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.SalesReturn;
import org.beigesoft.accounting.persistable.SalesReturnLine;
import org.beigesoft.accounting.persistable.SalesReturnTaxLine;
import org.beigesoft.accounting.persistable.Tax;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Business service for Sales Return Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvSalesReturnLine<RS>
  extends ASrvAccEntityImmutable<RS, SalesReturnLine>
    implements ISrvEntityOwned<SalesReturnLine, SalesReturn> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>Query Vendor Invoice Line Taxes.</p>
   **/
  private String querySalesReturnLineTaxes;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvSalesReturnLine() {
    super(SalesReturnLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvSalesReturnLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvWarehouseEntry pSrvWarehouseEntry) {
    super(SalesReturnLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
    this.srvWarehouseEntry = pSrvWarehouseEntry;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final SalesReturnLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    SalesReturnLine entity = new SalesReturnLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    SalesReturn itsOwner = new SalesReturn();
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
  public final SalesReturnLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    SalesReturnLine entity = getSrvOrm().retrieveCopyEntity(
      SalesReturnLine.class, pId);
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
      entity.setSubtotal(entity.getSubtotal().negate());
      entity.setTotalTaxes(entity.getTotalTaxes().negate());
      entity.setItsTotal(entity.getItsTotal().negate());
    } else {
      entity.setItsQuantity(BigDecimal.ZERO);
      entity.setItsCost(BigDecimal.ZERO);
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
    final SalesReturnLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      if (pEntity.getItsQuantity().doubleValue() == 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "quantity_is_0");
      }
      if (pEntity.getItsQuantity().doubleValue() < 0
        && pEntity.getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Reversed_Line_is_null");
      }
      if (pEntity.getItsCost().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "cost_less_or_eq_zero" + pAddParam.get("user"));
      }
      if (pEntity.getItsPrice().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "price_less_eq_0");
      }
      //SQL refresh:
      pEntity.setInvItem(getSrvOrm().retrieveEntity(pEntity.getInvItem()));
      SalesReturn itsOwner = getSrvOrm().retrieveEntityById(
        SalesReturn.class, pEntity.getItsOwner().getItsId());
      pEntity.setItsOwner(itsOwner);
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      pEntity.setItsPrice(pEntity.getItsPrice().setScale(getSrvAccSettings()
          .lazyGetAccSettings().getPricePrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
      pEntity.setItsCost(pEntity.getItsCost().setScale(getSrvAccSettings()
          .lazyGetAccSettings().getCostPrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
      //round to price precision
      pEntity.setSubtotal(pEntity.getItsTotal().setScale(getSrvAccSettings()
          .lazyGetAccSettings().getPricePrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
      pEntity.setTheRest(pEntity.getItsQuantity());
      BigDecimal totalTaxes = BigDecimal.ZERO;
      String taxesDescription = "";
      if (getSrvAccSettings().lazyGetAccSettings()
        .getIsExtractSalesTaxFromPurchase()
          && pEntity.getInvItem().getTaxCategory() != null) {
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
      pEntity.setItsTotal(pEntity.getSubtotal().add(totalTaxes));
      if (pEntity.getReversedId() != null) {
        pEntity.setTheRest(BigDecimal.ZERO);
      }
      getSrvOrm().insertEntity(pEntity);
      if (pEntity.getReversedId() != null) {
        SalesReturnLine reversed = getSrvOrm().retrieveEntityById(
          SalesReturnLine.class, pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "Attempt to double reverse" + pAddParam.get("user"));
        }
        if (!reversed.getItsQuantity().equals(reversed.getTheRest())) {
          throw new ExceptionWithCode(ExceptionWithCode
            .WRONG_PARAMETER, "where_is_withdrawals_from_this_source");
        }
        reversed.setTheRest(BigDecimal.ZERO);
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(reversed);
      }
      srvWarehouseEntry.load(pAddParam, pEntity, pEntity.getWarehouseSite());
      String query =
        "select sum(SUBTOTAL) as SUBTOTAL, sum(TOTALTAXES) as TOTALTAXES from"
        + " SALESRETURNLINE where ITSOWNER=" + itsOwner.getItsId();
      String[] columns = new String[]{"SUBTOTAL", "TOTALTAXES"};
      Double[] totals = getSrvDatabase().evalDoubleResults(query, columns);
      itsOwner.setSubtotal(BigDecimal.valueOf(totals[0]).setScale(
        getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      itsOwner.setTotalTaxes(BigDecimal.valueOf(totals[1]).setScale(
        getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      itsOwner.setItsTotal(itsOwner.getSubtotal().
        add(itsOwner.getTotalTaxes()));
      getSrvOrm().updateEntity(itsOwner);
      if (getSrvAccSettings().lazyGetAccSettings()
        .getIsExtractSalesTaxFromPurchase()) {
        updateTaxLines(itsOwner.getItsId());
      }
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "Attempt to update Sales Return line by " + pAddParam.get("user"));
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
  public final SalesReturnLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    SalesReturnLine entity = new SalesReturnLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    SalesReturn itsOwner = new SalesReturn();
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
  public final SalesReturnLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final SalesReturn pEntityItsOwner) throws Exception {
    SalesReturnLine entity = new SalesReturnLine();
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
  public final List<SalesReturnLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(SalesReturnLine.class,
      SalesReturn.class, pIdEntityItsOwner);
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
  public final List<SalesReturnLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final SalesReturn pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(SalesReturnLine.class,
      SalesReturn.class, pEntityItsOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Lazy get querySalesReturnLineTaxes.</p>
   * @return querySalesReturnLineTaxes
   * @throws Exception - an exception
   **/
  public final String lazyGetQuerySalesReturnLineTaxes() throws Exception {
    if (this.querySalesReturnLineTaxes == null) {
      String flName = "/" + "accounting" + "/" + "trade"
        + "/" + "salesReturnLineTaxes.sql";
      this.querySalesReturnLineTaxes = loadString(flName);
    }
    return this.querySalesReturnLineTaxes;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = SrvSalesReturnLine.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvSalesReturnLine.class
          .getResourceAsStream(pFileName);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return null;
  }

  /**
   * <p>Update invoice Tax Lines.</p>
   * @param pOwnerId Owner Id
   * @throws Exception - an exception
   **/
  public final void updateTaxLines(final Long pOwnerId) throws Exception {
    List<SalesReturnTaxLine> sitl = getSrvOrm().retrieveListWithConditions(
        SalesReturnTaxLine.class, "where ITSOWNER="
          + pOwnerId);
    String query = lazyGetQuerySalesReturnLineTaxes().replace(":ITSOWNER",
      pOwnerId.toString());
    int countUpdatedSitl = 0;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          Long taxId = getSrvDatabase().getSrvRecordRetriever()
            .getLong(recordSet.getRecordSet(), "TAXID");
          Double totalTax = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "TOTALTAX");
          SalesReturnTaxLine sit;
          if (sitl.size() > countUpdatedSitl) {
            sit = sitl.get(countUpdatedSitl);
            countUpdatedSitl++;
          } else {
            sit = getSrvOrm().createEntityWithOwner(
              SalesReturnTaxLine.class, SalesReturn.class,
                pOwnerId);
            sit.setIsNew(true);
          }
          Tax tax = new Tax();
          tax.setItsId(taxId);
          sit.setTax(tax);
          sit.setItsTotal(BigDecimal.valueOf(totalTax).setScale(
            getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
              getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
          if (sit.getIsNew()) {
            getSrvOrm().insertEntity(sit);
          } else {
            getSrvOrm().updateEntity(sit);
          }
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    if (countUpdatedSitl < sitl.size()) {
      for (int j = countUpdatedSitl; j < sitl.size(); j++) {
        getSrvOrm().deleteEntity(sitl.get(j));
      }
    }
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
   * <p>Setter for querySalesReturnLineTaxes.</p>
   * @param pQuerySalesReturnLineTaxes reference
   **/
  public final void setQuerySalesReturnLineTaxes(
    final String pQuerySalesReturnLineTaxes) {
    this.querySalesReturnLineTaxes = pQuerySalesReturnLineTaxes;
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
