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
 * <p>Business service for subaccounts that hold used type map.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvSubaccCode extends ISrvTypeCode {

  /**
   * <p>Getter for code - used type map.</p>
   * @return Map<Integer, Class<?>> map og used subacc classes
   **/
  Map<Integer, Class<?>> getSubaccUsedCodeMap();

  /**
   * <p>Setter for code - used type map.</p>
   * @param pSubaccUsedCodeMap reference
   **/
  void setSubaccUsedCodeMap(Map<Integer, Class<?>> pSubaccUsedCodeMap);
}
