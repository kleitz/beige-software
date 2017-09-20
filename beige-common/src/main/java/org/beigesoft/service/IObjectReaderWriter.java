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
