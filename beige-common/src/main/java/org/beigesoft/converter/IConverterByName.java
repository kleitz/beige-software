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
