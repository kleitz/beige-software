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

import org.beigesoft.accounting.persistable.DebtorCreditor;
import org.beigesoft.accounting.persistable.InvItem;

/**
 * <pre>
 * Model of ID for Goods that just seen by Customer.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class CustomerGoodsSeenId {

  /**
   * <p>Price Category.</p>
   **/
  private InvItem goods;

  /**
   * <p>Customer, not null.</p>
   **/
  private DebtorCreditor customer;

  /**
   * <p>Minimal constructor.</p>
   **/
  public CustomerGoodsSeenId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pGoods reference
   * @param pCustomer reference
   **/
  public CustomerGoodsSeenId(final InvItem pGoods,
    final DebtorCreditor pCustomer) {
    this.customer = pCustomer;
    this.goods = pGoods;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for goods.</p>
   * @return InvItem
   **/
  public final InvItem getGoods() {
    return this.goods;
  }

  /**
   * <p>Getter for customer.</p>
   * @return DebtorCreditor
   **/
  public final DebtorCreditor getCustomer() {
    return this.customer;
  }

  /**
   * <p>Setter for pGoods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final InvItem pGoods) {
    this.goods = pGoods;
  }

  /**
   * <p>Setter for customer.</p>
   * @param pCustomer reference
   **/
  public final void setCustomer(final DebtorCreditor pCustomer) {
    this.customer = pCustomer;
  }
}
