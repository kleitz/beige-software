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

import org.beigesoft.model.AEditableHasVersion;
import org.beigesoft.model.IHasId;
import org.beigesoft.accounting.persistable.InvItem;

/**
 * <pre>
 * Model of Goods Advise Category.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class GoodsAdviseCategories extends AEditableHasVersion
  implements IHasId<GoodsAdviseCategoriesId> {

  /**
   * <p>Complex ID. Must be initialized cause reflection use.</p>
   **/
  private GoodsAdviseCategoriesId itsId = new GoodsAdviseCategoriesId();

  /**
   * <p>Advise Category Of Goods, not null.</p>
   **/
  private AdviseCategoryOfGs adviseCategory;

  /**
   * <p>Goods, not null.</p>
   **/
  private InvItem goods;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final GoodsAdviseCategoriesId getItsId() {
    return this.itsId;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pId model ID
   **/
  @Override
  public final void setItsId(final GoodsAdviseCategoriesId pItsId) {
    this.itsId = pItsId;
    if (this.itsId != null) {
      this.adviseCategory = this.itsId.getAdviseCategory();
      this.goods = this.itsId.getGoods();
    } else {
      this.adviseCategory = null;
      this.goods = null;
    }
  }

  /**
   * <p>Setter for pAdviseCategory.</p>
   * @param pAdviseCategory reference
   **/
  public final void setAdviseCategory(
    final AdviseCategoryOfGs pAdviseCategory) {
    this.adviseCategory = pAdviseCategory;
    if (this.itsId == null) {
      this.itsId = new GoodsAdviseCategoriesId();
    }
    this.itsId.setAdviseCategory(this.adviseCategory);
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final InvItem pGoods) {
    this.goods = pGoods;
    if (this.itsId == null) {
      this.itsId = new GoodsAdviseCategoriesId();
    }
    this.itsId.setGoods(this.goods);
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
}
