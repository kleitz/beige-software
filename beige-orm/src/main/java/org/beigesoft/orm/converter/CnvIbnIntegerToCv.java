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
 * <p>Converter from a Integer type to ColumnsValues
 * without transformation.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnIntegerToCv
  implements IConverterIntoByName<Integer, ColumnsValues> {

  /**
   * <p>Put Integer object to ColumnsValues without transformation.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
   * @param pFrom from a Integer object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final Integer pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    pTo.put(pName, pFrom);
  }
}
