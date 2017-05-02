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
 * <p>Generic converter of Enum
 * to/from string representation, null represents as "".</p>
 *
 * @param <E> type enum.
 * @author Yury Demidenko
 */
public class CnvTfsEnum<E extends Enum<E>>
  implements IConverterToFromString<E> {

  /**
   * <p>Enum class.</p>
   **/
  private Class<E> enumClass;

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
    final E pModel) throws Exception {
    if (pModel == null) {
      return "";
    }
    return pModel.name();
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal name
   * @return Enum value
   * @throws Exception - an exception
   **/
  @Override
  public final E fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    return Enum.valueOf(this.enumClass, pStrVal);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for enumClass.</p>
   * @return Class<E>
   **/
  public final Class<E> getEnumClass() {
    return this.enumClass;
  }

  /**
   * <p>Setter for enumClass.</p>
   * @param pEnumClass reference
   **/
  public final void setEnumClass(final Class<E> pEnumClass) {
    this.enumClass = pEnumClass;
  }
}
