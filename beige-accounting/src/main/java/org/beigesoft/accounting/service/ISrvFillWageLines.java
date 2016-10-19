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

import org.beigesoft.accounting.persistable.Wage;

/**
 * <pre>
 * Abstraction that fill wage lines according a method,
 * e.g. Wage Table Percentage.
 * </pre>
 *
 * @author Yury Demidenko
 */
public interface ISrvFillWageLines {

  /**
   * <p>Fill wage lines according a method.</p>
   * @param pAddParam additional param
   * @param pWage Wage document
   * @throws Exception - an exception
   **/
  void fillWageLines(Map<String, Object> pAddParam,
    Wage pWage) throws Exception;
}
