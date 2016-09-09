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

import java.util.Date;

/**
 * <p>Converter that return miliseconds .toString() value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterDateToString implements IConverter<Date, String> {

  /**
   * <p>Convert entity to its ID string.</p>
   * @param a Date
   * @return String value in milliseconds
   **/
  @Override
  public final String convert(final Date from) {
    return Long.valueOf(from.getTime()).toString();
  }
}
