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
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.accounting.model.WarehouseRestLine;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Trial balance service.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvWarehouseRests<RS> implements ISrvWarehouseRests {

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
  public SrvWarehouseRests() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvDatabase database service
   **/
  public SrvWarehouseRests(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Retrieve Trial Balance.</p>
   * @param pAddParam additional param
   * @param pDate date
   * @throws Exception - an exception
   **/
  @Override
  public final List<WarehouseRestLine> retrieveWarehouseRests(
    final Map<String, Object> pAddParam) throws Exception {
    List<WarehouseRestLine> result = new ArrayList<WarehouseRestLine>();
    if (this.queryMain == null) {
      String flName = "/" + "accounting" + "/"
        + "warehouse" + "/" + "rests-in-warehouses.sql";
      this.queryMain = loadString(flName);
    }
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(this.queryMain);
      if (recordSet.moveToFirst()) {
        do {
          WarehouseRestLine wrl = new WarehouseRestLine();
          wrl.setWarehouse(recordSet.getString("WAREHOUSE"));
          wrl.setInvItemId(recordSet.getLong("INVITEMID"));
          wrl.setInvItem(recordSet.getString("INVITEM"));
          wrl.setUnitOfMeasure(recordSet.getString("UNITOFMEASURE"));
          wrl.setTheRest(BigDecimal
              .valueOf(recordSet.getDouble("THEREST")));
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
    URL urlFile = SrvWarehouseRests.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvWarehouseRests.class.getResourceAsStream(pFileName);
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
