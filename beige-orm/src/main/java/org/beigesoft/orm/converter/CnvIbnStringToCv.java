package org.beigesoft.orm.converter;

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

import java.util.Map;

import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.converter.IConverterIntoByName;
import org.beigesoft.orm.service.ISrvSqlEscape;

/**
 * <p>Converter from a String type to ColumnsValues
 * with SQL escaping for JDBC.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnStringToCv
  implements IConverterIntoByName<String, ColumnsValues> {

  /**
   * <p>If need to SQL escape for value string.
   * Android do it itself.</p>
   **/
  private boolean isNeedsToSqlEscape = true;

  /**
   * <p>SQL Escape service.</p>
   **/
  private ISrvSqlEscape srvSqlEscape;

  /**
   * <p>Put String object to ColumnsValues with SQL escaping
   * for JDBC.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
    * @param pFrom from a String object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final String pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    String value;
    if (this.isNeedsToSqlEscape && pFrom != null) {
      value = this.srvSqlEscape.escape(pFrom);
    } else {
      value = pFrom;
    }
    pTo.put(pName, value);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for isNeedsToSqlEscape.</p>
   * @return boolean
   **/
  public final boolean getIsNeedsToSqlEscape() {
    return this.isNeedsToSqlEscape;
  }

  /**
   * <p>Setter for isNeedsToSqlEscape.</p>
   * @param pIsNeedsToSqlEscape reference
   **/
  public final void setIsNeedsToSqlEscape(final boolean pIsNeedsToSqlEscape) {
    this.isNeedsToSqlEscape = pIsNeedsToSqlEscape;
  }

  /**
   * <p>Getter for srvSqlEscape.</p>
   * @return ISrvSqlEscape
   **/
  public final ISrvSqlEscape getSrvSqlEscape() {
    return this.srvSqlEscape;
  }

  /**
   * <p>Setter for srvSqlEscape.</p>
   * @param pSrvSqlEscape reference
   **/
  public final void setSrvSqlEscape(final ISrvSqlEscape pSrvSqlEscape) {
    this.srvSqlEscape = pSrvSqlEscape;
  }
}
