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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <p>Converter of Date-Time without seconds
 * to/from string representation, null represents as "".</p>
 *
 * @author Yury Demidenko
 */
public class CnvTfsDateTime implements IConverterToFromString<Date> {

  /**
   * <p>Format date-time ISO8601 no time zone,
   * e.g. 2001-07-04T21:55.</p>
   **/
  private DateFormat dateTimeNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

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
    return this.dateTimeNoTzFormatIso8601.format(pModel);
  }

  /**
   * <p>Convert from string.</p>
   * @param pAddParam additional params, e.g. IRequestData
   * to fill owner itsVersion.
   * @param pStrVal string representation
   * @return date-time value
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromString(final Map<String, Object> pAddParam,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    return this.dateTimeNoTzFormatIso8601.parse(pStrVal);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for dateTimeNoTzFormatIso8601.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeNoTzFormatIso8601() {
    return this.dateTimeNoTzFormatIso8601;
  }

  /**
   * <p>Setter for dateTimeNoTzFormatIso8601.</p>
   * @param pDateTimeNoTzFormatIso8601 reference
   **/
  public final void setDateTimeNoTzFormatIso8601(
    final DateFormat pDateTimeNoTzFormatIso8601) {
    this.dateTimeNoTzFormatIso8601 = pDateTimeNoTzFormatIso8601;
  }
}
