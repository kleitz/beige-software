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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.persistable.PurchaseInvoiceTaxLine;
import org.beigesoft.accounting.persistable.Tax;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Utility for Vendor Invoice Goods/Service Line.
 * It makes total for owner.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class UtlPurchaseGoodsServiceLine<RS> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>File with Query Vendor Invoice Taxes.</p>
   **/
  private String fileQueryPurchaseInvoiceTaxes = "purchaseInvoiceTaxes.sql";

  /**
   * <p>Query Vendor Invoice Taxes.</p>
   **/
  private String queryPurchaseInvoiceTaxes;

  /**
   * <p>File with Query Vendor Invoice Totals.</p>
   **/
  private String fileQueryPurchaseInvoiceTotals = "purchaseInvoiceTotals.sql";

  /**
   * <p>Query Vendor Invoice Totals.</p>
   **/
  private String queryPurchaseInvoiceTotals;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Insert immutable line into DB.</p>
   * @param pItsOwner PurchaseInvoice
   * @throws Exception - an exception
   **/
  public final void updateOwner(
    final PurchaseInvoice pItsOwner) throws Exception {
    String query = lazyGetQueryPurchaseInvoiceTotals();
    query = query.replace(":ITSOWNER", pItsOwner.getItsId().toString());
    String[] columns = new String[]{"SUBTOTAL", "TOTALTAXES"};
    Double[] totals = getSrvDatabase().evalDoubleResults(query, columns);
    if (totals[0] == null) {
      totals[0] = 0d;
    }
    if (totals[1] == null) {
      totals[1] = 0d;
    }
    pItsOwner.setSubtotal(BigDecimal.valueOf(totals[0]).setScale(
      getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
        getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    pItsOwner.setTotalTaxes(BigDecimal.valueOf(totals[1]).setScale(
      getSrvAccSettings().lazyGetAccSettings().getPricePrecision(),
        getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
    pItsOwner.setItsTotal(pItsOwner.getSubtotal().
      add(pItsOwner.getTotalTaxes()));
    getSrvOrm().updateEntity(pItsOwner);
    if (getSrvAccSettings().lazyGetAccSettings()
      .getIsExtractSalesTaxFromPurchase()) {
      updateTaxLines(pItsOwner.getItsId());
    }
  }

  /**
   * <p>Lazy get queryPurchaseInvoiceTaxes.</p>
   * @return queryPurchaseInvoiceTaxes
   * @throws Exception - an exception
   **/
  public final String lazyGetQueryPurchaseInvoiceTaxes() throws Exception {
    if (this.queryPurchaseInvoiceTaxes == null) {
      String flName = "/" + "accounting" + "/" + "trade"
        + "/" + this.fileQueryPurchaseInvoiceTaxes;
      this.queryPurchaseInvoiceTaxes = loadString(flName);
    }
    return this.queryPurchaseInvoiceTaxes;
  }

  /**
   * <p>Lazy get queryPurchaseInvoiceTotals.</p>
   * @return queryPurchaseInvoiceTotals
   * @throws Exception - an exception
   **/
  public final String lazyGetQueryPurchaseInvoiceTotals() throws Exception {
    if (this.queryPurchaseInvoiceTotals == null) {
      String flName = "/" + "accounting" + "/" + "trade"
        + "/" + this.fileQueryPurchaseInvoiceTotals;
      this.queryPurchaseInvoiceTotals = loadString(flName);
    }
    return this.queryPurchaseInvoiceTotals;
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
    String query = lazyGetQueryPurchaseInvoiceTaxes().replace(":ITSOWNER",
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
   * <p>Getter for fileQueryPurchaseInvoiceTaxes.</p>
   * @return String
   **/
  public final String getFileQueryPurchaseInvoiceTaxes() {
    return this.fileQueryPurchaseInvoiceTaxes;
  }

  /**
   * <p>Setter for fileQueryPurchaseInvoiceTaxes.</p>
   * @param pFileQueryPurchaseInvoiceTaxes reference
   **/
  public final void setFileQueryPurchaseInvoiceTaxes(
    final String pFileQueryPurchaseInvoiceTaxes) {
    this.fileQueryPurchaseInvoiceTaxes = pFileQueryPurchaseInvoiceTaxes;
  }

  /**
   * <p>Getter for queryPurchaseInvoiceTaxes.</p>
   * @return String
   **/
  public final String getQueryPurchaseInvoiceTaxes() {
    return this.queryPurchaseInvoiceTaxes;
  }

  /**
   * <p>Setter for queryPurchaseInvoiceTaxes.</p>
   * @param pQueryPurchaseInvoiceTaxes reference
   **/
  public final void setQueryPurchaseInvoiceTaxes(
    final String pQueryPurchaseInvoiceTaxes) {
    this.queryPurchaseInvoiceTaxes = pQueryPurchaseInvoiceTaxes;
  }

  /**
   * <p>Getter for fileQueryPurchaseInvoiceTotals.</p>
   * @return String
   **/
  public final String getFileQueryPurchaseInvoiceTotals() {
    return this.fileQueryPurchaseInvoiceTotals;
  }

  /**
   * <p>Setter for fileQueryPurchaseInvoiceTotals.</p>
   * @param pFileQueryPurchaseInvoiceTotals reference
   **/
  public final void setFileQueryPurchaseInvoiceTotals(
    final String pFileQueryPurchaseInvoiceTotals) {
    this.fileQueryPurchaseInvoiceTotals = pFileQueryPurchaseInvoiceTotals;
  }

  /**
   * <p>Getter for queryPurchaseInvoiceTotals.</p>
   * @return String
   **/
  public final String getQueryPurchaseInvoiceTotals() {
    return this.queryPurchaseInvoiceTotals;
  }

  /**
   * <p>Setter for queryPurchaseInvoiceTotals.</p>
   * @param pQueryPurchaseInvoiceTotals reference
   **/
  public final void setQueryPurchaseInvoiceTotals(
    final String pQueryPurchaseInvoiceTotals) {
    this.queryPurchaseInvoiceTotals = pQueryPurchaseInvoiceTotals;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final void setSrvAccSettings(final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }
}
