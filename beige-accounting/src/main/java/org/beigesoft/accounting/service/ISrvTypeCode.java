package org.beigesoft.accounting.service;

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
 * <p>Business service for code - java type map.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvTypeCode {

  /**
   * <p>Getter for code - java type map.</p>
   * @return Map<Integer, Class<?>>
   **/
  Map<Integer, Class<?>> getTypeCodeMap();

  /**
   * <p>Setter for code - java type map.</p>
   * @param pTypeCodeMap reference
   **/
  void setTypeCodeMap(Map<Integer, Class<?>> pTypeCodeMap);
}
