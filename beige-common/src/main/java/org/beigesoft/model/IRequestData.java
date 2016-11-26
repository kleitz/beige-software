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

import java.util.Map;

/**
 * <p>Abstraction of request data (get/set param, attribute)
 * that usually wrap HttpServletRequest(parameters/attributes).
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IRequestData {

  /**
   * <p>Getter for parameter.</p>
   * @param pParamName Parameter Name
   * @return parameter
   **/
  String getParameter(String pParamName);


  /**
   * <p>Getter for Parameters Map.</p>
   * @return parameters map
   **/
  Map<String, String[]> getParameterMap();

  /**
   * <p>Getter for attribute.</p>
   * @param pAttrName Attribute name
   * @return Attribute
   **/
  Object getAttribute(String pAttrName);

  /**
   * <p>Setter for attribute.</p>
   * @param pAttrName Attribute name
   * @param pAttribute reference
   **/
  void setAttribute(String pAttrName, Object pAttribute);
}
