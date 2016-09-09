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
import java.util.ArrayList;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.accounting.model.WarehouseSiteRestLine;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Trial balance service.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvWarehouseSiteRests<RS> implements ISrvWarehouseSiteRests {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Query Main.</p>
   **/
  private String queryMain;

  /**
   * <p>Minimal constructor.</p>
   **/
  public SrvWarehouseSiteRests() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvDatabase database service
   **/
  public SrvWarehouseSiteRests(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Retrieve Trial Balance.</p>
   * @param pAddParam additional param
   * @param pDate date
   * @throws Exception - an exception
   **/
  @Override
  public final List<WarehouseSiteRestLine> retrieveWarehouseSiteRests(
    final Map<String, ?> pAddParam) throws Exception {
    List<WarehouseSiteRestLine> result = new ArrayList<WarehouseSiteRestLine>();
    if (this.queryMain == null) {
      String flName = File.separator + "accounting" + File.separator
        + "warehouse" + File.separator + "rests-in-sites.sql";
      this.queryMain = loadString(flName);
    }
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(this.queryMain);
      if (recordSet.moveToFirst()) {
        do {
          WarehouseSiteRestLine wrl = new WarehouseSiteRestLine();
          wrl.setWarehouse(getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "WAREHOUSE"));
          wrl.setWarehouseSite(getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "WAREHOUSESITE"));
          wrl.setInvItemId(getSrvDatabase().getSrvRecordRetriever()
            .getLong(recordSet.getRecordSet(), "INVITEMID"));
          wrl.setInvItem(getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "INVITEM"));
          wrl.setUnitOfMeasure(getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "UNITOFMEASURE"));
          wrl.setTheRest(getSrvDatabase().getSrvRecordRetriever()
            .getBigDecimal(recordSet.getRecordSet(), "THEREST"));
          result.add(wrl);
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = SrvWarehouseSiteRests.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvWarehouseSiteRests.class
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
   * <p>Geter for srvDatabase.</p>
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
   * <p>Geter for queryMain.</p>
   * @return String
   **/
  public final String getQueryMain() {
    return this.queryMain;
  }

  /**
   * <p>Setter for queryMain.</p>
   * @param pQueryMain reference
   **/
  public final void setQueryMain(final String pQueryMain) {
    this.queryMain = pQueryMain;
  }
}
