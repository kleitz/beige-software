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
 * <p>Converter of Float
 * to/from string representation, null represents as "".</p>
 *
 * @author Yury Demidenko
 */
public class CnvTfsFloat implements IConverterToFromString<Float> {

  /**
   * <p>Convert to string, null represents as "".</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pModel a bean
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String toString(final Map<String, Object> pAddParam,
    final Float pModel) throws Exception {
    if (pModel == null) {
      return "";
    }
    return pModel.toString();
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return Float value
   * @throws Exception - an exception
   **/
  @Override
  public final Float fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    return Float.valueOf(pStrVal);
  }
}
