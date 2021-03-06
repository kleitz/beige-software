package org.beigesoft.web.model;

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

import javax.servlet.http.Cookie;

import org.beigesoft.model.ICookie;

/**
 * <p>Adapter (wrapper) of javax.servlet.http.Cookie.</p>
 *
 * @author Yury Demidenko
 */
public class HttpCookie implements ICookie {

  /**
   * <p>Cookie.</p>
   **/
  private Cookie cookie;

  /**
   * <p>Only constructor.</p>
   * @param pCookie reference
   **/
  public HttpCookie(final Cookie pCookie) {
    this.cookie = pCookie;
  }

  /**
   * <p>Get name.</p>
   * @return name
   **/
  @Override
  public final String getName() {
    return this.cookie.getName();
  }

  /**
   * <p>Get value.</p>
   * @return value
   **/
  @Override
  public final String getValue() {
    return this.cookie.getValue();
  }

  /**
   * <p>Set value.</p>
   * @param pValue reference
   **/
  @Override
  public final void setValue(final String pValue) {
    this.cookie.setValue(pValue);
  }

  /**
   * <p>Set maximum age in second.</p>
   * @param pMaxAge maximum age in second
   **/
  @Override
  public final void setMaxAge(final int pMaxAge) {
    this.cookie.setMaxAge(pMaxAge);
  }

  /**
   * <p>Get maximum age in second.</p>
   * @return maximum age in second
   **/
  @Override
  public final int getMaxAge() {
    return this.cookie.getMaxAge();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cookie.</p>
   * @return Cookie
   **/
  public final Cookie getCookie() {
    return this.cookie;
  }
}
