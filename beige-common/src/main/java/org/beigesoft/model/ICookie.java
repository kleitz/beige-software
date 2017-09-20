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
