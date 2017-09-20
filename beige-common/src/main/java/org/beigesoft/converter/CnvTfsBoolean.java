package org.beigesoft.converter;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
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
