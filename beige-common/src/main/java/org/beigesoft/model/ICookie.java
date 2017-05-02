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
 * <p>Abstraction of cookie
 * that usually wrap javax.servlet.http.Cookie.</p>
 *
 * @author Yury Demidenko
 */
public interface ICookie {

  /**
   * <p>Get name.</p>
   * @return name
   **/
  String getName();

  /**
   * <p>Get value.</p>
   * @return value
   **/
  String getValue();

  /**
   * <p>Set value.</p>
   * @param pValue reference
   **/
  void setValue(String pValue);

  /**
   * <p>Set maximum age in second.</p>
   * @param pMaxAge maximum age in second
   **/
  void setMaxAge(int pMaxAge);

  /**
   * <p>Get maximum age in second.</p>
   * @return maximum age in second
   **/
  int getMaxAge();
}
