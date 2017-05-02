package org.beigesoft.test.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.math.BigDecimal;

import org.beigesoft.persistable.APersistableBase;

/**
 * <pre>
 * Model for test ORM.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public class PersistableLine extends APersistableBase {

  private PersistableHead persistableHead;

  /**
   * <p>Version, autoincrement algorithm.</p>
   **/
  private Long itsVersion;
  
  /**
   * <p>User from foreign table.</p>
   **/
  private GoodVersionTime itsProduct;

  /**
   * <p>Price.</p>
   **/
  private BigDecimal itsPrice;

  /**
   * <p>Quantity.</p>
   **/
  private BigDecimal itsQuantity;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal itsTotal;

  //Simple getters and setters:
  /**
   * <p>Geter for persistableHead.</p>
   * @return PersistableHead
   **/
  public final PersistableHead getPersistableHead() {
    return this.persistableHead;
  }

  /**
   * <p>Setter for persistableHead.</p>
   * @param pPersistableHead reference/value
   **/
  public final void setPersistableHead(final PersistableHead pPersistableHead) {
    this.persistableHead = pPersistableHead;
  }

  /**
   * <p>Geter for itsVersion.</p>
   * @return Long
   **/
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference/value
   **/
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
  }

  /**
   * <p>Geter for itsProduct.</p>
   * @return GoodVersionTime
   **/
  public final GoodVersionTime getItsProduct() {
    return this.itsProduct;
  }

  /**
   * <p>Setter for itsProduct.</p>
   * @param pItsProduct reference/value
   **/
  public final void setItsProduct(final GoodVersionTime pItsProduct) {
    this.itsProduct = pItsProduct;
  }

  /**
   * <p>Geter for itsPrice.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsPrice() {
    return this.itsPrice;
  }

  /**
   * <p>Setter for itsPrice.</p>
   * @param pItsPrice reference/value
   **/
  public final void setItsPrice(final BigDecimal pItsPrice) {
    this.itsPrice = pItsPrice;
  }

  /**
   * <p>Geter for itsQuantity.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsQuantity() {
    return this.itsQuantity;
  }

  /**
   * <p>Setter for itsQuantity.</p>
   * @param pItsQuantity reference/value
   **/
  public final void setItsQuantity(final BigDecimal pItsQuantity) {
    this.itsQuantity = pItsQuantity;
  }

  /**
   * <p>Geter for itsTotal.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsTotal() {
    return this.itsTotal;
  }

  /**
   * <p>Setter for itsTotal.</p>
   * @param pItsTotal reference/value
   **/
  public final void setItsTotal(final BigDecimal pItsTotal) {
    this.itsTotal = pItsTotal;
  }
}
