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
 * <p>Converter from String to Float value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterStringFloat implements IConverter<String, Float> {

  /**
   * <p>Convert String to Float value.</p>
   * @param String value
   * @return Float value
   **/
  @Override
  public final Float convert(final String from) {
    return Float.valueOf(from);
  }
}
