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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <p>Converter of Date without time
 * to/from string representation, null represents as "".</p>
 *
 * @author Yury Demidenko
 */
public class CnvTfsDate implements IConverterToFromString<Date> {

  /**
   * <p>Format date ISO8601 no time zone,
   * e.g. 2001-07-04.</p>
   **/
  private DateFormat dateNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd");

  /**
   * <p>Convert to string, null represents as "".</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pModel date
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String toString(final Map<String, Object> pAddParam,
    final Date pModel) throws Exception {
    if (pModel == null) {
      return "";
    }
    return this.dateNoTzFormatIso8601.format(pModel);
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return date without time
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    return this.dateNoTzFormatIso8601.parse(pStrVal);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for dateNoTzFormatIso8601.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateNoTzFormatIso8601() {
    return this.dateNoTzFormatIso8601;
  }

  /**
   * <p>Setter for dateNoTzFormatIso8601.</p>
   * @param pDateNoTzFormatIso8601 reference
   **/
  public final void setDateNoTzFormatIso8601(
    final DateFormat pDateNoTzFormatIso8601) {
    this.dateNoTzFormatIso8601 = pDateNoTzFormatIso8601;
  }
}
