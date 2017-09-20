package org.beigesoft.converter;

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
 * <p>Abstraction of generic converter a model to string representation
 * and vice versa. It's mostly used as converter to from HTML.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model
 */
public interface IConverterToFromString<M> {

  /**
   * <p>Convert to string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pModel a bean
   * @return string representation
   * @throws Exception - an exception
   **/
  String toString(Map<String, Object> pAddParam, M pModel) throws Exception;

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return M model
   * @throws Exception - an exception
   **/
  M fromString(Map<String, Object> pAddParam, String pStrVal) throws Exception;
}
