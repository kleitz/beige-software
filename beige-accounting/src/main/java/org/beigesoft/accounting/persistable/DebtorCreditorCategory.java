package org.beigesoft.accounting.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.accounting.persistable.base.ASubaccount;

/**
 * <pre>
 * Model of Debtor/Creditor category.
 * It used for subaccount/filter/business intelligence.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class DebtorCreditorCategory extends ASubaccount {

  /**
   * <p>OOP friendly Constant of code type.</p>
   * @return 2005
   **/
  @Override
  public final Integer constTypeCode() {
    return 2005;
  }
}
