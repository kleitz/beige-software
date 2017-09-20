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

import java.util.Map;

import org.beigesoft.model.IRequestData;

/**
 * <p>Abstraction of service that retrieve entities page
 * or filter data according request.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvEntitiesPage {

  /**
   * <p>Retrieve entities page - entities list, pages, filter map etc.</p>
   * @param pAddParam additional param
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  void retrievePage(Map<String, Object> pAddParam,
    IRequestData pRequestData) throws Exception;

  /**
   * <p>Retrieve page filter data like SQL where and filter map.</p>
   * @param pAddParam additional param to return revealed data
   * @param pRequestData - Request Data
   * @param pEntityClass Entity Class
   * @throws Exception - an Exception
   **/
  void revealPageFilterData(Map<String, Object> pAddParam,
    IRequestData pRequestData, Class<?> pEntityClass) throws Exception;
}
