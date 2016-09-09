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
 * <p>Converter from String to Double value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterStringDouble implements IConverter<String, Double> {

  /**
   * <p>Convert String to Double value.</p>
   * @param String value
   * @return Double value
   **/
  @Override
  public final Double convert(final String from) {
    return Double.valueOf(from);
  }
}
