package org.beigesoft.orm.service;

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
 * <p>Standard SQL Escape service.</p>
 *
 * @author Yury Demidenko
 */
public class SrvSqlEscape implements ISrvSqlEscape {

  /**
   * <p>Load string from file (usually SQL query).</p>
   * @param pSource string
   * @return String escaped
   * @throws Exception - an exception
   **/
  @Override
  public final String escape(final String pSource) throws Exception {
    return pSource.replace("'", "''");
  }
}
