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

import org.beigesoft.accounting.persistable.base.ASubaccountUsed;

/**
 * <pre>
 * Model of used Service To Sale Category in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ServiceToSaleCategoryUsed
  extends ASubaccountUsed<ServiceToSaleCategory> {

  /**
   * <p>ServiceToSaleCategory.</p>
   **/
  private ServiceToSaleCategory subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return ServiceToSaleCategory
   **/
  @Override
  public final ServiceToSaleCategory getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final ServiceToSaleCategory pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
