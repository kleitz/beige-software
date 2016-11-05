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
 * Model of used Purchased Service Category in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ServicePurchasedCategoryUsed
  extends ASubaccountUsed<ServicePurchasedCategory> {

  /**
   * <p>ServicePurchasedCategory.</p>
   **/
  private ServicePurchasedCategory subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return ServicePurchasedCategory
   **/
  @Override
  public final ServicePurchasedCategory getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final ServicePurchasedCategory pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
