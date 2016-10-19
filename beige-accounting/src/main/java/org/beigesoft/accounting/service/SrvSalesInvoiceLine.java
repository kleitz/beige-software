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
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.SalesInvoiceLine;
import org.beigesoft.accounting.persistable.SalesInvoiceTaxLine;
import org.beigesoft.accounting.persistable.Tax;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

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
   * <p>Query Customer Invoice Line Taxes.</p>
   **/
  private String querySalesInvoiceLineTaxes;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvSalesInvoiceLine() {
    super(SalesInvoiceLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw material service
   **/
  public SrvSalesInvoiceLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvWarehouseEntry pSrvWarehouseEntry,
        final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(SalesInvoiceLine.class, pSrvOrm, pSrvAccSettings);
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
          "Attempt to double reverse" + pAddParam.get("user"));
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
      if (pEntity.getItsQuantity().doubleValue() == 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Quantity is 0!" + pAddParam.get("user"));
      }
      if (pEntity.getItsQuantity().doubleValue() < 0
        && pEntity.getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Reversed Line is null!" + pAddParam.get("user"));
      }
      if (pEntity.getItsPrice().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Price <= 0!" + pAddParam.get("user"));
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
      "select sum(SUBTOTAL) as SUBTOTAL, sum(TOTALTAXES) as TOTALTAXES from"
        + " SALESINVOICELINE where ITSOWNER="
          + pEntity.getItsOwner().getItsId();
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
        .getIsExtractSalesTaxFromSales()) {
        updateTaxLines(pEntity.getItsOwner().getItsId());
      }
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

  //Utils:
  /**
   * <p>Update invoice Tax Lines.</p>
   * @param pOwnerId Owner Id
   * @throws Exception - an exception
   **/
  public final void updateTaxLines(final Long pOwnerId) throws Exception {
    List<SalesInvoiceTaxLine> citl = getSrvOrm().retrieveListWithConditions(
        SalesInvoiceTaxLine.class, "where ITSOWNER="
          + pOwnerId);
    String query = lazyGetQuerySalesInvoiceLineTaxes().replace(":ITSOWNER",
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
          SalesInvoiceTaxLine cit;
          if (citl.size() > countUpdatedSitl) {
            cit = citl.get(countUpdatedSitl);
            countUpdatedSitl++;
          } else {
            cit = getSrvOrm().createEntityWithOwner(
              SalesInvoiceTaxLine.class, SalesInvoice.class,
                pOwnerId);
            cit.setIsNew(true);
          }
          Tax tax = new Tax();
          tax.setItsId(taxId);
          cit.setTax(tax);
          cit.setItsTotal(BigDecimal.valueOf(totalTax).setScale(
            getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
              getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
          if (cit.getIsNew()) {
            getSrvOrm().insertEntity(cit);
          } else {
            getSrvOrm().updateEntity(cit);
          }
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    if (countUpdatedSitl < citl.size()) {
      for (int j = countUpdatedSitl; j < citl.size(); j++) {
        getSrvOrm().deleteEntity(citl.get(j));
      }
    }
  }

  /**
   * <p>Lazy Get querySalesInvoiceLineTaxes.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String lazyGetQuerySalesInvoiceLineTaxes() throws Exception {
    if (this.querySalesInvoiceLineTaxes == null) {
      String flName = File.separator + "accounting" + File.separator + "trade"
        + File.separator + "salesInvoiceLineTaxes.sql";
      this.querySalesInvoiceLineTaxes = loadString(flName);
    }
    return this.querySalesInvoiceLineTaxes;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = SrvSalesInvoiceLine.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvSalesInvoiceLine.class.getResourceAsStream(pFileName);
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
   * <p>Setter for querySalesInvoiceLineTaxes.</p>
   * @param pQuerySalesInvoiceLineTaxes reference
   **/
  public final void setQuerySalesInvoiceLineTaxes(
    final String pQuerySalesInvoiceLineTaxes) {
    this.querySalesInvoiceLineTaxes = pQuerySalesInvoiceLineTaxes;
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
