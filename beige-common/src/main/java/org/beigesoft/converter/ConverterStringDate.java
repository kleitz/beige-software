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
 * <p>Converter from String milliseconds to Date value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterStringDate implements IConverter<String, Date> {

  /**
   * <p>Convert String milliseconds to Date value.</p>
   * @param String value
   * @return Date value
   **/
  @Override
  public final Date convert(final String from) {
    return new Date(Long.parseLong(from));
  }
}
