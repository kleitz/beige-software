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
 * <p>Fake converter thet return nonconverted value.</p>
 *
 * @param <T> type of converted value
 * @author Yury Demidenko
 */
public class ConverterStub<T> implements IConverter<T, T> {

  /**
   * <p>Convert parameter.</p>
   * @param <T> a type
   * @return <T> same value
   **/
  @Override
  public final T convert(final T from) {
    return from;
  }
}
