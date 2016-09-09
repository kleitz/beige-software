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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.persistable.PurchaseInvoiceLine;
import org.beigesoft.accounting.persistable.PurchaseInvoiceTaxLine;
import org.beigesoft.accounting.persistable.Tax;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Business service for Vendor Invoice Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvPurchaseInvoiceLine<RS>
  extends ASrvAccEntityImmutable<RS, PurchaseInvoiceLine>
    implements ISrvEntityOwned<PurchaseInvoiceLine, PurchaseInvoice> {

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
  private String queryPurchaseInvoiceLineTaxes;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvPurchaseInvoiceLine() {
    super(PurchaseInvoiceLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvPurchaseInvoiceLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvWarehouseEntry pSrvWarehouseEntry) {
    super(PurchaseInvoiceLine.class, pSrvOrm, pSrvAccSettings);
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
  public final PurchaseInvoiceLine createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    PurchaseInvoiceLine entity = new PurchaseInvoiceLine();
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
  public final PurchaseInvoiceLine retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    PurchaseInvoiceLine entity = getSrvOrm().retrieveCopyEntity(
      PurchaseInvoiceLine.class, pId);
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
  public final void saveEntity(final Map<String, ?> pAddParam,
    final PurchaseInvoiceLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      if (pEntity.getItsQuantity().doubleValue() == 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Quantity is 0! " + pAddParam.get("user"));
      }
      if (pEntity.getItsQuantity().doubleValue() < 0
        && pEntity.getReversedId() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Reversed Line is null! " + pAddParam.get("user"));
      }
      if (pEntity.getItsCost().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Cost <= 0! " + pAddParam.get("user"));
      }
      //SQL refresh:
      pEntity.setInvItem(getSrvOrm().retrieveEntity(pEntity.getInvItem()));
      if (!(InvItem.MATERIAL_ID.equals(pEntity.getInvItem().getItsType()
        .getItsId()) || InvItem.MERCHANDISE_ID.equals(pEntity.getInvItem()
          .getItsType().getItsId()))) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Type must be material or merchandise!");
      }
      PurchaseInvoice itsOwner = getSrvOrm().retrieveEntityById(
        PurchaseInvoice.class, pEntity.getItsOwner().getItsId());
      pEntity.setItsOwner(itsOwner);
      //rounding:
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      pEntity.setItsCost(pEntity.getItsCost().setScale(getSrvAccSettings()
          .lazyGetAccSettings().getCostPrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
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
        PurchaseInvoiceLine reversed = getSrvOrm().retrieveEntityById(
          PurchaseInvoiceLine.class, pEntity.getReversedId());
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
        + " PURCHASEINVOICELINE where ITSOWNER=" + itsOwner.getItsId();
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
        "Attempt to update purchase invoice line by " + pAddParam.get("user"));
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
  public final PurchaseInvoiceLine createEntityWithOwnerById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    PurchaseInvoiceLine entity = new PurchaseInvoiceLine();
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
  public final PurchaseInvoiceLine createEntityWithOwner(
    final Map<String, ?> pAddParam,
      final PurchaseInvoice pEntityItsOwner) throws Exception {
    PurchaseInvoiceLine entity = new PurchaseInvoiceLine();
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
  public final List<PurchaseInvoiceLine> retrieveOwnedListById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(PurchaseInvoiceLine.class,
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
  public final List<PurchaseInvoiceLine> retrieveOwnedList(
    final Map<String, ?> pAddParam,
      final PurchaseInvoice pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(PurchaseInvoiceLine.class,
      PurchaseInvoice.class, pEntityItsOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Lazy get queryPurchaseInvoiceLineTaxes.</p>
   * @return queryPurchaseInvoiceLineTaxes
   * @throws Exception - an exception
   **/
  public final String lazyGetQueryPurchaseInvoiceLineTaxes() throws Exception {
    if (this.queryPurchaseInvoiceLineTaxes == null) {
      String flName = File.separator + "accounting" + File.separator + "trade"
        + File.separator + "purchaseInvoiceLineTaxes.sql";
      this.queryPurchaseInvoiceLineTaxes = loadString(flName);
    }
    return this.queryPurchaseInvoiceLineTaxes;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = SrvPurchaseInvoiceLine.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvPurchaseInvoiceLine.class
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
    List<PurchaseInvoiceTaxLine> sitl = getSrvOrm().retrieveListWithConditions(
        PurchaseInvoiceTaxLine.class, "where ITSOWNER="
          + pOwnerId);
    String query = lazyGetQueryPurchaseInvoiceLineTaxes().replace(":ITSOWNER",
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
          PurchaseInvoiceTaxLine sit;
          if (sitl.size() > countUpdatedSitl) {
            sit = sitl.get(countUpdatedSitl);
            countUpdatedSitl++;
          } else {
            sit = getSrvOrm().createEntityWithOwner(
              PurchaseInvoiceTaxLine.class, PurchaseInvoice.class,
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
   * <p>Setter for queryPurchaseInvoiceLineTaxes.</p>
   * @param pQueryPurchaseInvoiceLineTaxes reference
   **/
  public final void setQueryPurchaseInvoiceLineTaxes(
    final String pQueryPurchaseInvoiceLineTaxes) {
    this.queryPurchaseInvoiceLineTaxes = pQueryPurchaseInvoiceLineTaxes;
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
