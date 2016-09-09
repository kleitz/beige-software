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
 * Account Due Method ACCRUAL/CASH.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public enum EDueMethod {

  /**
   * <p>Due when recieve/send document.</p>
   **/
  ACCRUAL,

  /**
   * <p>Due when recieve/send money.</p>
   **/
  CASH;
}
