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

import java.util.Map;
import java.util.Date;

import org.beigesoft.accounting.model.BalanceSheet;

/**
 * <p>Balanse Sheet service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvBalanceSheet {

  /**
   * <p>Retrieve Balance.</p>
   * @param pAddParam additional param
   * @param pDate date
   * @return Balance sheet
   * @throws Exception - an exception
   **/
  BalanceSheet retrieveBalance(Map<String, Object> pAddParam,
    Date pDate) throws Exception;
}
