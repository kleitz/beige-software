package org.beigesoft.orm.converter;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;

import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.converter.IConverterIntoByName;

/**
 * <p>Converter from a Enum type to ColumnsValues
 * with transformation into Integer.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnEnumToCv
  implements IConverterIntoByName<Enum<?>, ColumnsValues> {

  /**
   * <p>Put Enum object to ColumnsValues with transformation
   * into Integer.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
   * @param pFrom from a Enum object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final Enum<?> pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    Integer value;
    if (pFrom == null) {
      value = null;
    } else {
      value = pFrom.ordinal();
    }
    pTo.put(pName, value);
  }
}
