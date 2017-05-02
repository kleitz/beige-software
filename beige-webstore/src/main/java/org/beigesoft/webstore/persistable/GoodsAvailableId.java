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
 * Model of ID of GoodsAvailable.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class GoodsAvailableId {

  /**
   * <p>Goods pickUpPlace.</p>
   **/
  private PickUpPlace pickUpPlace;

  /**
   * <p>Goods, not null.</p>
   **/
  private InvItem goods;


  /**
   * <p>Minimal constructor.</p>
   **/
  public GoodsAvailableId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pPickUpPlace reference
   * @param pGoods reference
   **/
  public GoodsAvailableId(final PickUpPlace pPickUpPlace,
    final InvItem pGoods) {
    this.goods = pGoods;
    this.pickUpPlace = pPickUpPlace;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for pickUpPlace.</p>
   * @return PickUpPlace
   **/
  public final PickUpPlace getPickUpPlace() {
    return this.pickUpPlace;
  }

  /**
   * <p>Setter for pickUpPlace.</p>
   * @param pPickUpPlace reference
   **/
  public final void setPickUpPlace(final PickUpPlace pPickUpPlace) {
    this.pickUpPlace = pPickUpPlace;
  }

  /**
   * <p>Getter for goods.</p>
   * @return InvItem
   **/
  public final InvItem getGoods() {
    return this.goods;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final InvItem pGoods) {
    this.goods = pGoods;
  }
}
