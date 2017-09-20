package org.beigesoft.service;

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
 * <p>Date service standard JSE7 implementation.</p>
 *
 * @author Yury Demidenko
 */
public class SrvDate implements ISrvDate {

  /**
   * <p>Format date-hour-sec-millisecond ISO8601 no time zone,
   * e.g. 2001-07-04T12:08:56.235.</p>
   **/
  private DateFormat dateTimeFullNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  /**
   * <p>Format date-hour-min-sec ISO8601 no time zone,
   * e.g. 2001-07-04T12:08:56.</p>
   **/
  private DateFormat dateTimeSecNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  /**
   * <p>Format date-hour-min ISO8601 no time zone,
   * e.g. 2001-07-04T12:08.</p>
   **/
  private DateFormat dateTimeNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

  /**
   * <p>Format date ISO8601 no time zone,
   * e.g. 2001-07-04.</p>
   **/
  private DateFormat dateNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd");

  /**
   * <p>Format date-hour-min-sec-millisecond ISO8601 with time zone,
   * e.g. 2001-07-04T12:08:56.235-0700.</p>
   **/
  private DateFormat dateTimeFullFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); Android bug

  /**
   * <p>Format date localized for logging.</p>
   **/
  private DateFormat dateFormatLog = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

  /**
   * <p>Format date-time localized.</p>
   **/
  private DateFormat dateTimeFormat = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

  /**
   * <p>Format date-time-sec localized.</p>
   **/
  private DateFormat dateTimeSecFormat = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

  /**
   * <p>Format date-time-sec-ms localized.</p>
   **/
  private DateFormat dateTimeSecMsFormat = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG);

  /**
   * <p>Format date localized.</p>
   **/
  private DateFormat dateFormat = DateFormat
        .getDateInstance(DateFormat.MEDIUM);

  /**
   * <p>Format date to ISO8601 full string,
   * e.g. 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  @Override
  public final String toIso8601Full(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeFullFormatIso8601.format(pDate);
  }

  /**
   * <p>Format date to ISO8601 full string without TZ,
   * e.g. 2001-07-04T12:08:56.235.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  @Override
  public final String toIso8601FullNoTz(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeFullNoTzFormatIso8601.format(pDate);
  }

  /**
   * <p>Format date to ISO8601 date-time-sec string without TZ,
   * e.g. 2001-07-04T12:08:56.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  @Override
  public final String toIso8601DateTimeSecNoTz(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeSecNoTzFormatIso8601.format(pDate);
  }

  /**
   * <p>Format date to ISO8601 date-time string without TZ,
   * e.g. 2001-07-04T12:08.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  @Override
  public final String toIso8601DateTimeNoTz(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeNoTzFormatIso8601.format(pDate);
  }

  /**
   * <p>Format date to ISO8601 date string without TZ,
   * e.g. 2001-07-04.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  @Override
  public final String toIso8601DateNoTz(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateNoTzFormatIso8601.format(pDate);
  }

  /**
   * <p>Format date to localized string with or without seconds,
   * e.g. Jan 2, 2016 12:00 AM. Can be used for logging.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date string
   * @throws Exception - an exception
   **/
  @Override
  public final String toLocalString(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateFormatLog.format(pDate);
  }

  /**
   * <p>Format date to localized date string,
   * e.g. Jan 2, 2016".</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date string
   * @throws Exception - an exception
   **/
  @Override
  public final String toDateString(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateFormat.format(pDate);
  }

  /**
   * <p>Format date to localized date-time string,
   * e.g. Jan 2, 2016 12:00 AM.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date-time string
   * @throws Exception - an exception
   **/
  @Override
  public final String toDateTimeString(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeFormat.format(pDate);
  }

  /**
   * <p>Format date to localized date-time-sec string,
   * e.g. Jan 2, 2016 12:00:12 AM.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date-time-sec string
   * @throws Exception - an exception
   **/
  @Override
  public final String toDateTimeSecString(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeSecFormat.format(pDate);
  }

  /**
   * <p>Format date to localized date-time-sec-ms string,
   * e.g. Jan 2, 2016 12:00:12.12 AM.</p>
   * @param pDate date
   * @param pAddParam additional params
   * @return date-time-sec-ms string
   * @throws Exception - an exception
   **/
  @Override
  public final String toDateTimeSecMsString(final Date pDate,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeSecMsFormat.format(pDate);
  }

  /**
   * <p>Parse date from ISO8601 full string,
   * e.g. from 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromIso8601Full(final String pDateStr,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeFullFormatIso8601.parse(pDateStr);
  }

  /**
   * <p>Parse date from ISO8601 full string without TZ,
   * e.g. from 2001-07-04T12:08:56.235.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromIso8601FullNoTz(final String pDateStr,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeFullNoTzFormatIso8601.parse(pDateStr);
  }

  /**
   * <p>Parse date from ISO8601 date-time-sec string without TZ,
   * e.g. from 2001-07-04T12:08:56.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromIso8601DateTimeSecNoTz(final String pDateStr,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeSecNoTzFormatIso8601.parse(pDateStr);
  }

  /**
   * <p>Parse date from ISO8601 date-time string without TZ,
   * e.g. from 2001-07-04T12:08.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromIso8601DateTimeNoTz(final String pDateStr,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateTimeNoTzFormatIso8601.parse(pDateStr);
  }

  /**
   * <p>Parse date from ISO8601 date string without TZ,
   * e.g. from 2001-07-04.</p>
   * @param pDateStr date in ISO8601 format
   * @param pAddParam additional params
   * @return String representation
   * @throws Exception - an exception
   **/
  @Override
  public final Date fromIso8601DateNoTz(final String pDateStr,
    final Map<String, Object> pAddParam) throws Exception {
    return this.dateNoTzFormatIso8601.parse(pDateStr);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for dateTimeFullNoTzFormatIso8601.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeFullNoTzFormatIso8601() {
    return this.dateTimeFullNoTzFormatIso8601;
  }

  /**
   * <p>Setter for dateTimeFullNoTzFormatIso8601.</p>
   * @param pDateTimeFullNoTzFormatIso8601 reference
   **/
  public final void setDateTimeFullNoTzFormatIso8601(
    final DateFormat pDateTimeFullNoTzFormatIso8601) {
    this.dateTimeFullNoTzFormatIso8601 = pDateTimeFullNoTzFormatIso8601;
  }

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

  /**
   * <p>Getter for dateTimeFullFormatIso8601.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeFullFormatIso8601() {
    return this.dateTimeFullFormatIso8601;
  }

  /**
   * <p>Setter for dateTimeFullFormatIso8601.</p>
   * @param pDateTimeFullFormatIso8601 reference
   **/
  public final void setDateTimeFullFormatIso8601(
    final DateFormat pDateTimeFullFormatIso8601) {
    this.dateTimeFullFormatIso8601 = pDateTimeFullFormatIso8601;
  }

  /**
   * <p>Getter for dateFormatLog.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateFormatLog() {
    return this.dateFormatLog;
  }

  /**
   * <p>Setter for dateFormatLog.</p>
   * @param pDateFormatLog reference
   **/
  public final void setDateFormatLog(final DateFormat pDateFormatLog) {
    this.dateFormatLog = pDateFormatLog;
  }

  /**
   * <p>Getter for dateTimeFormat.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeFormat() {
    return this.dateTimeFormat;
  }

  /**
   * <p>Setter for dateTimeFormat.</p>
   * @param pDateTimeFormat reference
   **/
  public final void setDateTimeFormat(final DateFormat pDateTimeFormat) {
    this.dateTimeFormat = pDateTimeFormat;
  }

  /**
   * <p>Getter for dateTimeSecFormat.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeSecFormat() {
    return this.dateTimeSecFormat;
  }

  /**
   * <p>Setter for dateTimeSecFormat.</p>
   * @param pDateTimeSecFormat reference
   **/
  public final void setDateTimeSecFormat(final DateFormat pDateTimeSecFormat) {
    this.dateTimeSecFormat = pDateTimeSecFormat;
  }

  /**
   * <p>Getter for dateTimeSecMsFormat.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateTimeSecMsFormat() {
    return this.dateTimeSecMsFormat;
  }

  /**
   * <p>Setter for dateTimeSecMsFormat.</p>
   * @param pDateTimeSecMsFormat reference
   **/
  public final void setDateTimeSecMsFormat(
    final DateFormat pDateTimeSecMsFormat) {
    this.dateTimeSecMsFormat = pDateTimeSecMsFormat;
  }

  /**
   * <p>Getter for dateFormat.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateFormat() {
    return this.dateFormat;
  }

  /**
   * <p>Setter for dateFormat.</p>
   * @param pDateFormat reference
   **/
  public final void setDateFormat(final DateFormat pDateFormat) {
    this.dateFormat = pDateFormat;
  }
}
