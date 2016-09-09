package org.beigesoft.accounting.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <pre>
 * Entries SQL Source Accounting Type - DEBIT, DEBITCREDIT, CREDIT.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public enum EEntriesAccountingType {

  /**
   * <p>If make debit.</p>
   **/
  DEBIT,

  /**
   * <p>If make debit and credit.</p>
   **/
  DEBITCREDIT,

  /**
   * <p>If make credit.</p>
   **/
  CREDIT;
}
