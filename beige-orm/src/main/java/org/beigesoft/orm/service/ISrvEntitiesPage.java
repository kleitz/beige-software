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
