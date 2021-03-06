package org.beigesoft.test.persistable;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import org.beigesoft.model.AEditable;
import org.beigesoft.model.IHasId;

/**
 * <pre>
 * Model of average goods rating.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class GoodsRating extends AEditable
  implements IHasId<GoodVersionTime> {

  /**
   * <p>Goods, PK.</p>
   **/
  private GoodVersionTime goods;

  /**
   * <p>Average rating, 0..10, if exist.</p>
   **/
  private Integer averageRating;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final GoodVersionTime getItsId() {
    return this.goods;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pId model ID
   **/
  @Override
  public final void setItsId(final GoodVersionTime pItsId) {
    this.goods = pItsId;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for goods.</p>
   * @return InvItem
   **/
  public final GoodVersionTime getGoods() {
    return this.goods;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final GoodVersionTime pGoods) {
    this.goods = pGoods;
  }

  /**
   * <p>Getter for averageRating.</p>
   * @return Integer
   **/
  public final Integer getAverageRating() {
    return this.averageRating;
  }

  /**
   * <p>Setter for averageRating.</p>
   * @param pAverageRating reference
   **/
  public final void setAverageRating(final Integer pAverageRating) {
    this.averageRating = pAverageRating;
  }
}
