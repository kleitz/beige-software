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
 * <p>Abstraction of generic converter from a type to another one.</p>
 *
 * @author Yury Demidenko
 * @param <FR> type of original
 * @param <TO> type of converted
 */
public interface IConverter<FR, TO> {

  /**
   * <p>Convert parameter.</p>
   * @param pAddParam additional params, e.g. "isOnlyId"-true for
   * entity converter for converting only ID field.
   * @param pFrom a bean
   * @return TO required type
   * @throws Exception - an exception
   **/
  TO convert(Map<String, Object> pAddParam, FR pFrom) throws Exception;
}
