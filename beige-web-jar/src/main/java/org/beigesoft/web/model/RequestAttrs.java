package org.beigesoft.web.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import javax.servlet.http.HttpServletRequest;

import org.beigesoft.holder.IAttributes;

/**
 * <p>Http Servlet Request attributes holder adapter.</p>
 *
 * @author Yury Demidenko
 */
public class RequestAttrs implements IAttributes {

  /**
   * <p>Http Servlet Request to adapt.</p>
   **/
  private final HttpServletRequest httpReq;

  /**
   * <p>Onky constructor.</p>
   * @param pHttpReq reference
   **/
  public RequestAttrs(final HttpServletRequest pHttpReq) {
    this.httpReq = pHttpReq;
  }

  /**
   * <p>Get attribute.</p>
   * @param pName attribute name
   * @return attribute
   **/
  @Override
  public final Object getAttribute(final String pName) {
    return httpReq.getAttribute(pName);
  }

  /**
   * <p>Set attribute.</p>
   * @param pName attribute name
   * @param pAttr attribute
   **/
  @Override
  public final void setAttribute(final String pName,
    final Object pAttr) {
    httpReq.setAttribute(pName, pAttr);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for httpReq.</p>
   * @return HttpServletRequest
   **/
  public final HttpServletRequest getHttpReq() {
    return this.httpReq;
  }
}
