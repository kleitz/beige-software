package org.beigesoft.service;

import java.util.Date;
import java.util.Map;

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

/**
 * <p>Abstraction of date service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvDate {

  /**
   * <p>Format date to ISO8601 full string,
   * e.g. 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601Full(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to ISO8601 full string without TZ,
   * e.g. 2001-07-04T12:08:56.235.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601FullNoTz(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to ISO8601 date-time-sec string without TZ,
   * e.g. 2001-07-04T12:08:56.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601DateTimeSecNoTz(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to ISO8601 date-time string without TZ,
   * e.g. 2001-07-04T12:08.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601DateTimeNoTz(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to ISO8601 date string without TZ,
   * e.g. 2001-07-04.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601DateNoTz(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to localized string with or without seconds,
   * e.g. Jan 2, 2016 12:00 AM. Can be used for logging.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date string
   * @throws Exception - an exception
   **/
  String toLocalString(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to localized date string,
   * e.g. Jan 2, 2016".</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date string
   * @throws Exception - an exception
   **/
  String toDateString(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to localized date-time string,
   * e.g. Jan 2, 2016 12:00 AM.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date-time string
   * @throws Exception - an exception
   **/
  String toDateTimeString(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to localized date-time-sec string,
   * e.g. Jan 2, 2016 12:00:12 AM.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date-time-sec string
   * @throws Exception - an exception
   **/
  String toDateTimeSecString(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Format date to localized date-time-sec-ms string,
   * e.g. Jan 2, 2016 12:00:12.12 AM.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date-time-sec-ms string
   * @throws Exception - an exception
   **/
  String toDateTimeSecMsString(Date pDate,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Parse date from ISO8601 full string,
   * e.g. from 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601Full(String pDateStr,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Parse date from ISO8601 full string without TZ,
   * e.g. from 2001-07-04T12:08:56.235.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601FullNoTz(String pDateStr,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Parse date from ISO8601 date-time-sec string without TZ,
   * e.g. from 2001-07-04T12:08:56.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601DateTimeSecNoTz(String pDateStr,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Parse date from ISO8601 date-time string without TZ,
   * e.g. from 2001-07-04T12:08.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601DateTimeNoTz(String pDateStr,
    Map<String, Object> pAddParam) throws Exception;

  /**
   * <p>Parse date from ISO8601 date string without TZ,
   * e.g. from 2001-07-04.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601DateNoTz(String pDateStr,
    Map<String, Object> pAddParam) throws Exception;
}
