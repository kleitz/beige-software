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
import java.text.DateFormat;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.PurchaseReturn;
import org.beigesoft.accounting.persistable.PurchaseReturnLine;
import org.beigesoft.accounting.persistable.PurchaseReturnTaxLine;
import org.beigesoft.accounting.persistable.PurchaseInvoiceLine;
import org.beigesoft.accounting.persistable.Tax;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Business service for Purchase Return Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvPurchaseReturnLine<RS>
  extends ASrvAccEntityImmutable<RS, PurchaseReturnLine>
    implements ISrvEntityOwned<PurchaseReturnLine, PurchaseReturn> {

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Date Formatter.</p>
   **/
  private DateFormat dateFormatter;

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
  private ISrvDrawItemEntry<UseMaterialEntry> srvUseMaterialEntry;

  /**
   * <p>Query Customer Invoice Line Taxes.</p>
   **/
  private String queryPurchaseReturnLineTaxes;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvPurchaseReturnLine() {
    super(PurchaseReturnLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvPurchaseReturnLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvWarehouseEntry pSrvWarehouseEntry,
        final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry,
          final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    super(PurchaseReturnLine.class, pSrvOrm, pSrvAccSettings);
    this.srvDatabase = pSrvDatabase;
    this.srvWarehouseEntry = pSrvWarehouseEntry;
    this.srvUseMaterialEntry = pSrvUseMaterialEntry;
    this.srvI18n = pSrvI18n;
    this.dateFormatter = pDateFormatter;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final PurchaseReturnLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    PurchaseReturnLine entity = new PurchaseReturnLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    PurchaseReturn itsOwner = new PurchaseReturn();
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
  public final PurchaseReturnLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    PurchaseReturnLine entity = getSrvOrm().retrieveCopyEntity(
      PurchaseReturnLine.class, pId);
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
      entity.setPurchaseInvoiceLine(null);
      entity.setPurchInvLnAppearance(null);
      entity.setItsQuantity(BigDecimal.ZERO);
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
    final PurchaseReturnLine pEntity,
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
      if (pEntity.getPurchaseInvoiceLine() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "wrong_purchaseInvoiceLine");
      }
      pEntity.setItsQuantity(pEntity.getItsQuantity().setScale(
        getSrvAccSettings().lazyGetAccSettings().getQuantityPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      //BeigeORM refresh:
      pEntity.setPurchaseInvoiceLine(getSrvOrm().retrieveEntity(pEntity
        .getPurchaseInvoiceLine()));
      if (pEntity.getPurchaseInvoiceLine() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "wrong_purchaseInvoiceLine");
      }
      pEntity.setPurchInvLnAppearance(getSrvI18n().getMsg(PurchaseInvoiceLine
        .class.getSimpleName() + "short") + " #"
          + pEntity.getPurchaseInvoiceLine().getItsId() + ", " + pEntity
            .getPurchaseInvoiceLine().getInvItem().getItsName() + ", " + pEntity
              .getPurchaseInvoiceLine().getUnitOfMeasure().getItsName() + ", "
            + getSrvI18n().getMsg("itsCost") + "=" + pEntity
          .getPurchaseInvoiceLine().getItsCost() + ", " + getSrvI18n()
        .getMsg("rest_was") + "=" + pEntity.getPurchaseInvoiceLine()
      .getTheRest());
      pEntity.setSubtotal(pEntity.getItsQuantity().multiply(pEntity
        .getPurchaseInvoiceLine().getItsCost()).setScale(getSrvAccSettings()
          .lazyGetAccSettings().getPricePrecision(), getSrvAccSettings()
            .lazyGetAccSettings().getRoundingMode()));
      BigDecimal totalTaxes = BigDecimal.ZERO;
      if (getSrvAccSettings().lazyGetAccSettings()
        .getIsExtractSalesTaxFromPurchase()) {
        String taxesDescription = "";
        if (pEntity.getPurchaseInvoiceLine().getInvItem()
          .getTaxCategory() != null) {
          List<InvItemTaxCategoryLine> pstl = getSrvOrm()
            .retrieveListWithConditions(
              InvItemTaxCategoryLine.class, "where ITSOWNER="
                + pEntity.getPurchaseInvoiceLine().getInvItem()
                  .getTaxCategory().getItsId());
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
      PurchaseReturn itsOwner = getSrvOrm().retrieveEntityById(
        PurchaseReturn.class, pEntity.getItsOwner().getItsId());
      pEntity.setItsOwner(itsOwner);
      if (pEntity.getReversedId() != null) {
        PurchaseReturnLine reversed = getSrvOrm().retrieveEntityById(
          PurchaseReturnLine.class, pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "Attempt to double reverse" + pAddParam.get("user"));
        }
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(reversed);
        srvWarehouseEntry.reverseDraw(pAddParam, pEntity);
        srvUseMaterialEntry.reverseDraw(pAddParam, pEntity,
          pEntity.getItsOwner().getItsDate(),
            pEntity.getItsOwner().getItsId());
      } else {
        srvWarehouseEntry.withdrawal(pAddParam, pEntity);
        srvUseMaterialEntry.withdrawalFrom(pAddParam, pEntity,
          pEntity.getPurchaseInvoiceLine(), pEntity.getItsQuantity());
      }
      String query =
      "select sum(SUBTOTAL) as SUBTOTAL, sum(TOTALTAXES) as TOTALTAXES from"
        + " PURCHASERETURNLINE where ITSOWNER="
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
  public final PurchaseReturnLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    PurchaseReturnLine entity = new PurchaseReturnLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    //line use its owner to get purchase invoice ID
    PurchaseReturn itsOwner = getSrvOrm()
      .retrieveEntityById(PurchaseReturn.class, pIdEntityItsOwner);
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
  public final PurchaseReturnLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final PurchaseReturn pEntityItsOwner) throws Exception {
    PurchaseReturnLine entity = new PurchaseReturnLine();
    entity.setIsNew(true);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    //BeigeORM refresh:
    PurchaseReturn entityItsOwner = getSrvOrm()
      .retrieveEntity(pEntityItsOwner);
    entity.setItsOwner(entityItsOwner);
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
  public final List<PurchaseReturnLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(PurchaseReturnLine.class,
      PurchaseReturn.class, pIdEntityItsOwner);
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
  public final List<PurchaseReturnLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final PurchaseReturn pEntityItsOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(PurchaseReturnLine.class,
      PurchaseReturn.class, pEntityItsOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Update invoice Tax Lines.</p>
   * @param pOwnerId Owner Id
   * @throws Exception - an exception
   **/
  public final void updateTaxLines(final Long pOwnerId) throws Exception {
    List<PurchaseReturnTaxLine> citl = getSrvOrm().retrieveListWithConditions(
        PurchaseReturnTaxLine.class, "where ITSOWNER="
          + pOwnerId);
    String query = lazyGetQueryPurchaseReturnLineTaxes().replace(":ITSOWNER",
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
          PurchaseReturnTaxLine cit;
          if (citl.size() > countUpdatedSitl) {
            cit = citl.get(countUpdatedSitl);
            countUpdatedSitl++;
          } else {
            cit = getSrvOrm().createEntityWithOwner(
              PurchaseReturnTaxLine.class, PurchaseReturn.class,
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
   * <p>Lazy Get queryPurchaseReturnLineTaxes.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String
    lazyGetQueryPurchaseReturnLineTaxes() throws Exception {
    if (this.queryPurchaseReturnLineTaxes == null) {
      String flName = File.separator + "accounting" + File.separator + "trade"
        + File.separator + "purchaseReturnLineTaxes.sql";
      this.queryPurchaseReturnLineTaxes = loadString(flName);
    }
    return this.queryPurchaseReturnLineTaxes;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = SrvPurchaseReturnLine.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvPurchaseReturnLine.class
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

  //Simple getters and setters:

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

  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
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

  /**
   * <p>Getter for srvWarehouseEntry.</p>
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
   * <p>Getter for srvUseMaterialEntry.</p>
   * @return ISrvDrawItemEntry<UseMaterialEntry>
   **/
  public final ISrvDrawItemEntry<UseMaterialEntry> getSrvUseMaterialEntry() {
    return this.srvUseMaterialEntry;
  }

  /**
   * <p>Setter for srvUseMaterialEntry.</p>
   * @param pSrvUseMaterialEntry reference
   **/
  public final void setSrvUseMaterialEntry(
    final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry) {
    this.srvUseMaterialEntry = pSrvUseMaterialEntry;
  }

  /**
   * <p>Getter for queryPurchaseReturnLineTaxes.</p>
   * @return String
   **/
  public final String getQueryPurchaseReturnLineTaxes() {
    return this.queryPurchaseReturnLineTaxes;
  }

  /**
   * <p>Setter for queryPurchaseReturnLineTaxes.</p>
   * @param pQueryPurchaseReturnLineTaxes reference
   **/
  public final void setQueryPurchaseReturnLineTaxes(
    final String pQueryPurchaseReturnLineTaxes) {
    this.queryPurchaseReturnLineTaxes = pQueryPurchaseReturnLineTaxes;
  }
}
