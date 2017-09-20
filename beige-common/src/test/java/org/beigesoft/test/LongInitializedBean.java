package org.beigesoft.test;

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

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.model.EPeriod;


/**
 * <p>It's a long initialized bean to test Double Checked Locking.</p>
 *
 * @author Yury Demidenko
 */
public class LongInitializedBean {

  private EPeriod itsPeriod = null;
  
  private Date itsDate = null;

  private BigDecimal itsPrice = null;
  
  private Boolean isClosed = false;

  public LongInitializedBean() {
    // long initialization
    try {
      Thread.sleep(5);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.itsPeriod = EPeriod.DAILY;
    this.itsDate = new Date();
    try {
      Thread.sleep(5);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.itsPrice = new BigDecimal("12.11");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsPeriod.</p>
   * @return EPeriod
   **/
  public final EPeriod getItsPeriod() {
    return this.itsPeriod;
  }

  /**
   * <p>Setter for itsPeriod.</p>
   * @param pItsPeriod reference
   **/
  public final void setItsPeriod(final EPeriod pItsPeriod) {
    this.itsPeriod = pItsPeriod;
  }

  /**
   * <p>Getter for itsDate.</p>
   * @return Date
   **/
  public final Date getItsDate() {
    return this.itsDate;
  }

  /**
   * <p>Setter for itsDate.</p>
   * @param pItsDate reference
   **/
  public final void setItsDate(final Date pItsDate) {
    this.itsDate = pItsDate;
  }

  /**
   * <p>Getter for itsPrice.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsPrice() {
    return this.itsPrice;
  }

  /**
   * <p>Setter for itsPrice.</p>
   * @param pItsPrice reference
   **/
  public final void setItsPrice(final BigDecimal pItsPrice) {
    this.itsPrice = pItsPrice;
  }

  /**
   * <p>Getter for isClosed.</p>
   * @return Boolean
   **/
  public final Boolean getIsClosed() {
    return this.isClosed;
  }

  /**
   * <p>Setter for isClosed.</p>
   * @param pIsClosed reference
   **/
  public final void setIsClosed(final Boolean pIsClosed) {
    this.isClosed = pIsClosed;
  }
}
