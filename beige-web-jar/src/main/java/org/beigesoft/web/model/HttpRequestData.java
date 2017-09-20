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

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beigesoft.model.IRequestData;
import org.beigesoft.model.ICookie;

/**
 * <p>Wrapper (adapter) of HttpServletRequest/HttpServletResponse.
 * </p>
 *
 * @author Yury Demidenko
 */
public class HttpRequestData implements IRequestData {

  /**
   * <p>Http Servlet Request to adapt.</p>
   **/
  private final HttpServletRequest httpReq;

  /**
   * <p>Http Servlet Request to adapt.</p>
   **/
  private final HttpServletResponse httpResp;

  /**
   * <p>Only constructor.</p>
   * @param pHttpReq reference
   * @param pHttpResp reference
   **/
  public HttpRequestData(final HttpServletRequest pHttpReq,
    final HttpServletResponse pHttpResp) {
    this.httpReq = pHttpReq;
    this.httpResp = pHttpResp;
  }

  /**
   * <p>Getter for parameter.</p>
   * @param pParamName Parameter Name
   * @return parameter
   **/
  @Override
  public final String getParameter(final String pParamName) {
    return httpReq.getParameter(pParamName);
  }


  /**
   * <p>Getter for Parameters Map.</p>
   * @return parameters map
   **/
  @Override
  public final Map<String, String[]> getParameterMap() {
    return httpReq.getParameterMap();
  }

  /**
   * <p>Getter of user name.</p>
   * @return User name if he/she logged
   **/
  @Override
  public final String getUserName() {
    if (httpReq.getUserPrincipal() != null) {
      return httpReq.getUserPrincipal().getName();
    }
    return null;
  }

  /**
   * <p>Getter for attribute.</p>
   * @param pAttrName Attribute name
   * @return Attribute
   **/
  @Override
  public final Object getAttribute(final String pAttrName) {
    return httpReq.getAttribute(pAttrName);
  }

  /**
   * <p>Setter for attribute.</p>
   * @param pAttrName Attribute name
   * @param pAttribute reference
   **/
  @Override
  public final void setAttribute(final String pAttrName,
    final Object pAttribute) {
    httpReq.setAttribute(pAttrName, pAttribute);
  }

  /**
   * <p>Getter of cookies.</p>
   * @return cookies or null
   **/
  @Override
  public final HttpCookie[] getCookies() {
    if (this.httpReq.getCookies() == null) {
      return null;
    }
    HttpCookie[] httpCookies = new HttpCookie[this.httpReq.getCookies().length];
    for (int i = 0; i < this.httpReq.getCookies().length; i++) {
      Cookie cookie = this.httpReq.getCookies()[i];
      httpCookies[i] = new HttpCookie(cookie);
    }
    return httpCookies;
  }

  /**
   * <p>Add cookie.</p>
   * @param pCookie Cookie
   **/
  @Override
  public final void addCookie(final ICookie pCookie) {
    Cookie cookie = new Cookie(pCookie.getName(),
      pCookie.getValue());
    cookie.setMaxAge(pCookie.getMaxAge());
    this.httpResp.addCookie(cookie);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for httpReq.</p>
   * @return HttpServletRequest
   **/
  public final HttpServletRequest getHttpReq() {
    return this.httpReq;
  }

  /**
   * <p>Getter for httpResp.</p>
   * @return HttpServletResponse
   **/
  public final HttpServletResponse getHttpResp() {
    return this.httpResp;
  }
}
