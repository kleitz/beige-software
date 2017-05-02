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
import org.beigesoft.holder.IAttributes;

/**
 * <p>Abstraction of request data (get/set param, attribute)
 * that usually wrap HttpServletRequest/HttpServletRresponse.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IRequestData extends IAttributes {

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
   * <p>Getter of user name.</p>
   * @return User name if he/she logged
   **/
  String getUserName();

  /**
   * <p>Getter of cookies.</p>
   * @return cookies
   **/
  ICookie[] getCookies();

  /**
   * <p>Add cookie.</p>
   * @param pCookie Cookie
   **/
  void addCookie(ICookie pCookie);
}
