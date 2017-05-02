package org.beigesoft.service;

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
