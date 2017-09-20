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
 * <p>Abstraction of generic converter from a type into another one
 * with using a name e.g. fill ColumnsValues with field, and some sources
 * can transformed to set of another, e.g. foreign entity with composite ID
 * will be converted to set fieldId-valueId.</p>
 *
 * @author Yury Demidenko
 * @param <FR> type of original
 * @param <TO> type of converted
 */
public interface IConverterIntoByName<FR, TO> {

  /**
   * <p>Convert parameter with using name.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
   * @param pFrom from a bean source
   * @param pTo to a bean container
   * @param pName by a name
   * @throws Exception - an exception
   **/
  void convert(Map<String, Object> pAddParam, FR pFrom,
    TO pTo, String pName) throws Exception;
}
