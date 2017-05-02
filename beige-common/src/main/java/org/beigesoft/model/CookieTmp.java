package org.beigesoft.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <p>Temporary cookie to transfer into javax.servlet.http.Cookie.</p>
 *
 * @author Yury Demidenko
 */
public class CookieTmp implements ICookie {

  /**
   * <p>Name.</p>
   **/
  private String name;

  /**
   * <p>Value.</p>
   **/
  private String value;

  /**
   * <p>Maximum age in second, it's more than 60 years.</p>
   **/
  private int maxAge = Integer.MAX_VALUE;

  /**
   * <p>Get name.</p>
   * @return name
   **/
  @Override
  public final String getName() {
    return this.name;
  }

  /**
   * <p>Get value.</p>
   * @return value
   **/
  @Override
  public final String getValue() {
    return this.value;
  }

  /**
   * <p>Set value.</p>
   * @param pValue reference
   **/
  @Override
  public final void setValue(final String pValue) {
    this.value = pValue;
  }

  /**
   * <p>Set maximum age in second.</p>
   * @param pMaxAge maximum age in second
   **/
  @Override
  public final void setMaxAge(final int pMaxAge) {
    this.maxAge = pMaxAge;
  }

  /**
   * <p>Get maximum age in second.</p>
   * @return maximum age in second
   **/
  @Override
  public final int getMaxAge() {
    return this.maxAge;
  }

  //Simple getters and setters:
  /**
   * <p>Setter for name.</p>
   * @param pName reference
   **/
  public final void setName(final String pName) {
    this.name = pName;
  }
}
