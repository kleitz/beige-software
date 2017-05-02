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

/**
 * <pre>
 * Model of ID for price category for buyer.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class BuyerPriceCategoryId {

  /**
   * <p>Price Category.</p>
   **/
  private PriceCategory priceCategory;

  /**
   * <p>Buyer, not null.</p>
   **/
  private OnlineBuyer buyer;

  /**
   * <p>Minimal constructor.</p>
   **/
  public BuyerPriceCategoryId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pPriceCategory reference
   * @param pBuyer reference
   **/
  public BuyerPriceCategoryId(final PriceCategory pPriceCategory,
    final OnlineBuyer pBuyer) {
    this.buyer = pBuyer;
    this.priceCategory = pPriceCategory;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for priceCategory.</p>
   * @return PriceCategory
   **/
  public final PriceCategory getPriceCategory() {
    return this.priceCategory;
  }

  /**
   * <p>Getter for buyer.</p>
   * @return OnlineBuyer
   **/
  public final OnlineBuyer getBuyer() {
    return this.buyer;
  }

  /**
   * <p>Setter for pPriceCategory.</p>
   * @param pPriceCategory reference
   **/
  public final void setPriceCategory(final PriceCategory pPriceCategory) {
    this.priceCategory = pPriceCategory;
  }

  /**
   * <p>Setter for buyer.</p>
   * @param pBuyer reference
   **/
  public final void setBuyer(final OnlineBuyer pBuyer) {
    this.buyer = pBuyer;
  }
}
