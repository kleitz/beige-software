package org.beigesoft.service;

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

/**
 * <p>Abstraction of service that fill object (entity)
 * from a source, e.g. from SQL result-set.</p>
 *
 * @param <S> source type
 * @author Yury Demidenko
 */
public interface IFillerObjectsFrom<S> {

  /**
   * <p>Fill object's field.</p>
   * @param <T> object (entity) type
   * @param pAddParam additional param
   * @param pObject Object to fill
   * @param pSource Source
   * @throws Exception - an exception
   **/
  <T> void fill(Map<String, Object> pAddParam, T pObject,
    S pSource) throws Exception;
}
