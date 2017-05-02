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
 * <p>Converter of Date-Time with seconds
 * to/from string representation, null represents as "".</p>
 *
 * @author Yury Demidenko
 */
public class CnvTfsDateTimeSec implements IConverterToFromString<Date> {

  /**
   * <p>Format date-time-sec ISO8601 no time zone,
   * e.g. 2001-07-04'T'21:55:12.</p>
   **/
  private DateFormat dateTimeSecNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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
    return this.dateTimeSecNoTzFormatIso8601.format(pModel);
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return date-time-sec value
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    return this.dateTimeSecNoTzFormatIso8601.parse(pStrVal);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for dateTimeSecNoTzFormatIso8601.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeSecNoTzFormatIso8601() {
    return this.dateTimeSecNoTzFormatIso8601;
  }

  /**
   * <p>Setter for dateTimeSecNoTzFormatIso8601.</p>
   * @param pDateTimeSecNoTzFormatIso8601 reference
   **/
  public final void setDateTimeSecNoTzFormatIso8601(
    final DateFormat pDateTimeSecNoTzFormatIso8601) {
    this.dateTimeSecNoTzFormatIso8601 = pDateTimeSecNoTzFormatIso8601;
  }
}
