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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.beigesoft.model.IRequestData;

/**
 * <p>Abstraction of request data (get/set param, attribute)
 * that usually wrap HttpServletRequest(parameters/attributes).
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
   * <p>Only constructor.</p>
   * @param pHttpReq reference
   **/
  public HttpRequestData(final HttpServletRequest pHttpReq) {
    this.httpReq = pHttpReq;
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

  //Simple getters and setters:
  /**
   * <p>Geter for httpReq.</p>
   * @return HttpServletRequest
   **/
  public final HttpServletRequest getHttpReq() {
    return this.httpReq;
  }
}
