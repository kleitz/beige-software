package org.beigesoft.accounting.persistable.base;

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

/**
 * <pre>
 * Abstract model of purchase/manufacture.
 * </pre>
 *
 * @author Yury Demidenko
 */
public abstract class AInvItemMovementCost extends AInvItemMovement {

  /**
   * <p>Cost.</p>
   **/
  private BigDecimal itsCost = new BigDecimal("0.00");

  /**
   * <p>Total.</p>
   **/
  private BigDecimal itsTotal = new BigDecimal("0.00");

  //Simple getters and setters:
  /**
   * <p>Geter for itsCost.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsCost() {
    return this.itsCost;
  }

  /**
   * <p>Setter for itsCost.</p>
   * @param pItsCost reference
   **/
  public final void setItsCost(final BigDecimal pItsCost) {
    this.itsCost = pItsCost;
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
   * @param pItsTotal reference
   **/
  public final void setItsTotal(final BigDecimal pItsTotal) {
    this.itsTotal = pItsTotal;
  }
}
