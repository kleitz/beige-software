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
 * <p>Converter from String to Integer value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterStringInteger implements IConverter<String, Integer> {

  /**
   * <p>Convert String to Integer value.</p>
   * @param String value
   * @return Integer value
   **/
  @Override
  public final Integer convert(final String from) {
    return Integer.valueOf(from);
  }
}
