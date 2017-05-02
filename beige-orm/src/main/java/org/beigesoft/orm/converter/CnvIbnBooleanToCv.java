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
 * <p>Converter from a Boolean type to ColumnsValues
 * with transformation into Integer.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnBooleanToCv
  implements IConverterIntoByName<Boolean, ColumnsValues> {

  /**
   * <p>Put Boolean object to ColumnsValues with transformation
   * into Integer.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
   * @param pFrom from a Boolean object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final Boolean pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    Integer value;
    if (pFrom == null || !pFrom) { // Boolean is non-nullable
      value = 0;
    } else {
      value = 1;
    }
    pTo.put(pName, value);
  }
}
