package org.beigesoft.orm.service;

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
 * <p>Abstraction of SQL Escape service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvSqlEscape {

  /**
   * <p>Load string from file (usually SQL query).</p>
   * @param pSource string
   * @return String escaped
   * @throws Exception - an exception
   **/
  String escape(String pSource) throws Exception;
}
