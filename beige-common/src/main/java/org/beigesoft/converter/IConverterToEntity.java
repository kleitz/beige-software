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

/**
 * <p>Abstraction of generic converter from ID of type String to Entity.</p>
 *
 * @author Yury Demidenko
 * @param <T> type of Entity
 */
public interface IConverterToEntity<T> {

  /**
   * <p>Convert parameter ID to Entity.</p>
   * @param pItsIdStr ID string value
   * @param pClazz Entity class
   * @return TO Entity
   * @throws Exception an exception
   **/
  T convert(String pItsIdStr, Class<T> pClazz) throws Exception;
}
