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
