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
 * <p>Converter from String to Long value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterStringLong implements IConverter<String, Long> {

  /**
   * <p>Convert String to Long value.</p>
   * @param String value
   * @return Long value
   **/
  @Override
  public final Long convert(final String from) {
    return Long.valueOf(from);
  }
}
