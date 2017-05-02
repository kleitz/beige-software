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
 * Model of ID for Goods Advise Category.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class GoodsAdviseCategoriesId {

  /**
   * <p>Advise Category Of Goods, not null.</p>
   **/
  private AdviseCategoryOfGs adviseCategory;

  /**
   * <p>Goods, not null.</p>
   **/
  private InvItem goods;

  /**
   * <p>Minimal constructor.</p>
   **/
  public GoodsAdviseCategoriesId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pAdviseCategory reference
   * @param pGoods reference
   **/
  public GoodsAdviseCategoriesId(final AdviseCategoryOfGs pAdviseCategory,
    final InvItem pGoods) {
    this.goods = pGoods;
    this.adviseCategory = pAdviseCategory;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for adviseCategory.</p>
   * @return AdviseCategoryOfGs
   **/
  public final AdviseCategoryOfGs getAdviseCategory() {
    return this.adviseCategory;
  }

  /**
   * <p>Getter for goods.</p>
   * @return InvItem
   **/
  public final InvItem getGoods() {
    return this.goods;
  }

  /**
   * <p>Setter for pAdviseCategory.</p>
   * @param pAdviseCategory reference
   **/
  public final void setAdviseCategory(
    final AdviseCategoryOfGs pAdviseCategory) {
    this.adviseCategory = pAdviseCategory;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final InvItem pGoods) {
    this.goods = pGoods;
  }
}
