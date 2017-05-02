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

import org.beigesoft.accounting.persistable.InvItem;

/**
 * <pre>
 * Model of ID for Price for Goods.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class GoodsPriceId {

  /**
   * <p>Price Category.</p>
   **/
  private PriceCategory priceCategory;

  /**
   * <p>Goods, not null.</p>
   **/
  private InvItem goods;

  /**
   * <p>Minimal constructor.</p>
   **/
  public GoodsPriceId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pPriceCategory reference
   * @param pGoods reference
   **/
  public GoodsPriceId(final PriceCategory pPriceCategory,
    final InvItem pGoods) {
    this.goods = pGoods;
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
   * <p>Getter for goods.</p>
   * @return InvItem
   **/
  public final InvItem getGoods() {
    return this.goods;
  }

  /**
   * <p>Setter for pPriceCategory.</p>
   * @param pPriceCategory reference
   **/
  public final void setPriceCategory(final PriceCategory pPriceCategory) {
    this.priceCategory = pPriceCategory;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final InvItem pGoods) {
    this.goods = pGoods;
  }
}
