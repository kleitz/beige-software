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
import java.math.BigDecimal;

import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.converter.IConverterIntoByName;

/**
 * <p>Converter from a BigDecimal type to ColumnsValues
 * with transformation into Double.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnBigDecimalToCv
  implements IConverterIntoByName<BigDecimal, ColumnsValues> {

  /**
   * <p>Put BigDecimal object to ColumnsValues with transformation
   * into Double.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
   * @param pFrom from a BigDecimal object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final BigDecimal pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    Double value;
    if (pFrom == null) {
      value = null;
    } else {
      value = pFrom.doubleValue();
    }
    pTo.put(pName, value);
  }
}
