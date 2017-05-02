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

import java.util.Set;
import java.util.Map;

/**
 * <p>Abstraction of service that read/write object fields.</p>
 *
 * @param <T> object (entity) type
 * @author Yury Demidenko
 */
public interface IObjectReaderWriter<T> {

  /**
   * <p>Fill object's field.</p>
   * @param pAddParam additional param
   * @param pObject Object to fill
   * @param pFieldVal Field Value
   * @param pFieldName Field name
   * @throws Exception - an exception
   **/
  void write(Map<String, Object> pAddParam, T pObject,
    Object pFieldVal, String pFieldName) throws Exception;

  /**
   * <p>Read object's field.</p>
   * @param pAddParam additional param
   * @param pObject Object to fill
   * @param pFieldName Field name
   * @return object's field value
   * @throws Exception - an exception
   **/
  Object read(Map<String, Object> pAddParam, T pObject,
    String pFieldName) throws Exception;

  /**
   * <p>Get object's fields names.</p>
   * @return Object's fields names
   **/
  Set<String> getFieldsNames();
}
