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

import java.util.Map;

/**
 * <p>Converter of Boolean
 * to/from string representation, can't be null.</p>
 *
 * @author Yury Demidenko
 */
public class CnvTfsBoolean implements IConverterToFromString<Boolean> {

  /**
   * <p>Convert to string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pModel a bean
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String toString(final Map<String, Object> pAddParam,
    final Boolean pModel) throws Exception {
    if (pModel == null) {
      return "false";
    }
    return pModel.toString();
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return Boolean value
   * @throws Exception - an exception
   **/
  @Override
  public final Boolean fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null // HTTP checkbox return nothing if unchecked
      || "".equals(pStrVal) || "false".equals(pStrVal)
        || "off".equals(pStrVal)) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }
}
