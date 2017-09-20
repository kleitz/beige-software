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
 * <p>Abstraction of generic converter from a type to another one
 * with using a name e.g. get field value from JDBC result-set.</p>
 *
 * @author Yury Demidenko
 * @param <FR> type of original
 * @param <TO> type of converted
 */
public interface IConverterByName<FR, TO> {

  /**
   * <p>Convert parameter with using name.</p>
   * @param pAddParam additional params, e.g. entity class UserRoleTomcat
   * to reveal derived columns for its composite ID, or field Enum class
   * to reveal Enum value by index.
   * @param pFrom from a bean
   * @param pName by a name, e.g. field name "itsName"
   * @return pTo to a bean
   * @throws Exception - an exception
   **/
  TO convert(Map<String, Object> pAddParam, FR pFrom,
    String pName) throws Exception;
}
