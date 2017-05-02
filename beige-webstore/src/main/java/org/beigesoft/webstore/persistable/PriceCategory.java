package org.beigesoft.webstore.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.persistable.AHasNameIdLongVersion;

/**
 * <pre>
 * Model of Price Category for Goods/Service and Customer.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class PriceCategory extends AHasNameIdLongVersion {

  /**
   * <p>Price Category for Goods/Service.</p>
   **/
  private PriceCategoryOfItems priceCategoryOfItems;

  /**
   * <p>Price Category for Customer.</p>
   **/
  private PriceCategoryOfBuyers priceCategoryOfBuyers;

  /**
   * <p>Description.</p>
   **/
  private String description;

  //Simple getters and setters:
  /**
   * <p>Getter for priceCategoryOfItems.</p>
   * @return PriceCategoryOfItems
   **/
  public final PriceCategoryOfItems getPriceCategoryOfItems() {
    return this.priceCategoryOfItems;
  }

  /**
   * <p>Setter for priceCategoryOfItems.</p>
   * @param pPriceCategoryOfItems reference
   **/
  public final void setPriceCategoryOfItems(
    final PriceCategoryOfItems pPriceCategoryOfItems) {
    this.priceCategoryOfItems = pPriceCategoryOfItems;
  }

  /**
   * <p>Getter for priceCategoryOfBuyers.</p>
   * @return PriceCategoryOfBuyers
   **/
  public final PriceCategoryOfBuyers getPriceCategoryOfBuyers() {
    return this.priceCategoryOfBuyers;
  }

  /**
   * <p>Setter for priceCategoryOfBuyers.</p>
   * @param pPriceCategoryOfBuyers reference
   **/
  public final void setPriceCategoryOfBuyers(
    final PriceCategoryOfBuyers pPriceCategoryOfBuyers) {
    this.priceCategoryOfBuyers = pPriceCategoryOfBuyers;
  }

  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }
}
