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

import java.util.Map;
import java.util.Date;

import org.beigesoft.accounting.model.LedgerPrevious;
import org.beigesoft.accounting.model.LedgerDetail;
import org.beigesoft.accounting.persistable.Account;

/**
 * <p>Ledger service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvLedger {

  /**
   * <p>Retrieve previous totals.</p>
   * @param pAddParam additional param
   * @param pAccount account
   * @param pDate1 date start
   * @param pSubaccName Subaccount Name or null
   * @return LedgerPrevious data
   * @throws Exception - an exception
   **/
  LedgerPrevious retrievePrevious(Map<String, ?> pAddParam,
    Account pAccount, Date pDate1, String pSubaccName) throws Exception;

  /**
   * <p>Retrieve detail entries for period.</p>
   * @param pAddParam additional param
   * @param pAccount account
   * @param pDate1 date start
   * @param pDate2 date end
   * @return LedgerDetail data
   * @param pSubaccName Subaccount Name or null
   * @param ledgerPrevious ledger previous
   * @throws Exception - an exception
   **/
  LedgerDetail retrieveDetail(Map<String, ?> pAddParam,
    Account pAccount, Date pDate1, Date pDate2,
      String pSubaccName, LedgerPrevious ledgerPrevious) throws Exception;
}
