package org.beigesoft.accounting.model;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
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
