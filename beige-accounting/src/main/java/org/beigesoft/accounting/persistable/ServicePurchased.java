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

import org.beigesoft.persistable.APersistableBaseNameVersion;

/**
 * <pre>
 * Model of service purchased, e.g. "Shipping to NY",
 * "Repair carburetor MZX567G".
 * Version changed time algorithm.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ServicePurchased extends APersistableBaseNameVersion {

  /**
   * <p>Its category.
   * It used to filter list of services and as subaccount.</p>
   **/
  private ServicePurchasedCategory itsCategory;

  /**
   * <p>Tax category e.g. "NY: tax1 10%, tax2 5%".</p>
   **/
  private InvItemTaxCategory taxCategory;

  //Simple getters and setters:
  /**
   * <p>Geter for itsCategory.</p>
   * @return ServicePurchasedCategory
   **/
  public final ServicePurchasedCategory getItsCategory() {
    return this.itsCategory;
  }

  /**
   * <p>Setter for itsCategory.</p>
   * @param pItsCategory reference
   **/
  public final void setItsCategory(
    final ServicePurchasedCategory pItsCategory) {
    this.itsCategory = pItsCategory;
  }

  /**
   * <p>Geter for taxCategory.</p>
   * @return InvItemTaxCategory
   **/
  public final InvItemTaxCategory getTaxCategory() {
    return this.taxCategory;
  }

  /**
   * <p>Setter for taxCategory.</p>
   * @param pTaxCategory reference
   **/
  public final void setTaxCategory(final InvItemTaxCategory pTaxCategory) {
    this.taxCategory = pTaxCategory;
  }
}
