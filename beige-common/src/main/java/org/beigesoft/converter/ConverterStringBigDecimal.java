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

import java.math.BigDecimal;

/**
 * <p>Converter from String to BigDecimal value.</p>
 *
 * @author Yury Demidenko
 */
public class ConverterStringBigDecimal
  implements IConverter<String, BigDecimal> {

  /**
   * <p>Convert String to BigDecimal value.</p>
   * @param String value
   * @return BigDecimal value
   **/
  @Override
  public final BigDecimal convert(final String from) {
    return new BigDecimal(from);
  }
}
