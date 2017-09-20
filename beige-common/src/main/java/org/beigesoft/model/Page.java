package org.beigesoft.model;

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

/**
 * <p>Page model for pagination.</p>
 *
 * @author Yury Demidenko
 */
public class Page {

  /**
   * <p>Page number.</p>
   **/
  private String value;

  /**
   * <p>is page current.</p>
   **/
  private boolean isCurrent;

  /**
   * <p>Default constructor.</p>
   **/
  public Page() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pValue page #
   * @param pIsCurrent Is current?
   **/
  public Page(final String pValue, final boolean pIsCurrent) {
    this.value = pValue;
    this.isCurrent = pIsCurrent;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for value.</p>
   * @return String
   **/
  public final String getValue() {
    return this.value;
  }

  /**
   * <p>Setter for value.</p>
   * @param pValue reference
   **/
  public final void setValue(final String pValue) {
    this.value = pValue;
  }

  /**
   * <p>Geter for isCurrent.</p>
   * @return boolean
   **/
  public final boolean getIsCurrent() {
    return this.isCurrent;
  }

  /**
   * <p>Setter for isCurrent.</p>
   * @param pIsCurrent reference
   **/
  public final void setIsCurrent(final boolean pIsCurrent) {
    this.isCurrent = pIsCurrent;
  }
}
