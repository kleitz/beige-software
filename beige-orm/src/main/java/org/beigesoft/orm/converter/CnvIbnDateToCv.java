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
import java.util.Date;

import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.converter.IConverterIntoByName;

/**
 * <p>Converter from a Date type to ColumnsValues
 * with transformation into Long.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnDateToCv
  implements IConverterIntoByName<Date, ColumnsValues> {

  /**
   * <p>Put Date object to ColumnsValues with transformation
   * into Long.</p>
   * @param pAddParam additional params, e.g. version algorithm or
   * bean source class for generic converter that consume set of subtypes.
   * @param pFrom from a Date object
   * @param pTo to ColumnsValues
   * @param pName by a name
   * @throws Exception - an exception
   **/
  @Override
  public final void convert(final Map<String, Object> pAddParam,
    final Date pFrom, final ColumnsValues pTo,
      final String pName) throws Exception {
    Long value;
    if (pFrom == null) {
      value = null;
    } else {
      value = pFrom.getTime();
    }
    pTo.put(pName, value);
  }
}
