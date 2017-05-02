package org.beigesoft.converter;

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
