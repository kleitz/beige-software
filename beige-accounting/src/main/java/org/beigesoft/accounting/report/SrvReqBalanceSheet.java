package org.beigesoft.accounting.report;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Date;

import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.accounting.model.BalanceSheet;
import org.beigesoft.model.IRequestData;
import org.beigesoft.service.ISrvHandleRequest;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Transactional business service that handle request
 * for balance sheet report.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class SrvReqBalanceSheet<RS> implements ISrvHandleRequest {

  /**
   * <p>Balance Sheet service.</p>
   */
  private ISrvBalanceSheet srvBalanceSheet;

  /**
   * <p>Database service.</p>
   */
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Handle request.</p>
   * @param pRequestData Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handleRequest(
    final IRequestData pRequestData) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      Date date2 = new Date(Long.parseLong(pRequestData
        .getParameter("date2")));
      BalanceSheet balanceSheet = getSrvBalanceSheet()
        .retrieveBalance(null, date2);
      pRequestData.setAttribute("balanceSheet", balanceSheet);
      pRequestData.setAttribute("accSettings", srvAccSettings
        .lazyGetAccSettings());
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvBalanceSheet.</p>
   * @return ISrvBalanceSheet
   **/
  public final ISrvBalanceSheet getSrvBalanceSheet() {
    return this.srvBalanceSheet;
  }

  /**
   * <p>Setter for srvBalanceSheet.</p>
   * @param pSrvBalanceSheet reference
   **/
  public final void setSrvBalanceSheet(
    final ISrvBalanceSheet pSrvBalanceSheet) {
    this.srvBalanceSheet = pSrvBalanceSheet;
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
   * <p>Geter for srvAccSettings.</p>
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
