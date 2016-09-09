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
 * <p>Converter that return Object.toString() value.</p>
 *
 * @param <T> type of converted value
 * @author Yury Demidenko
 */
public class ConverterSimpleToString<T> implements IConverter<T, String> {

  /**
   * <p>Convert parameter to toString() representation.</p>
   * @param <T> a type
   * @return toString() value
   **/
  @Override
  public final String convert(final T from) {
    return from.toString();
  }
}
