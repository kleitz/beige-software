package org.beigesoft.test;

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

import java.util.Date;
import java.util.Calendar;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.beigesoft.model.EPeriod;

/**
 * <p>Java Calendar Test.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CalendarTest {

  @Test
  public void test1() throws Exception {
    Date date = new Date(1470225720000L); // 3 Aug 2016 15:02
    System.out.println("date is " + date);

    Date periodDayStart = evalDatePeriodStartFor(date, EPeriod.DAILY);
    System.out.println("period day start is " + periodDayStart);
    assertEquals(new Date(1470171600000L), periodDayStart); //Wed Aug 03 2016 00:00:00
    Date periodWeekStart = evalDatePeriodStartFor(date, EPeriod.WEEKLY);
    System.out.println("period week start is " + periodWeekStart);
    assertEquals(new Date(1469912400000L), periodWeekStart); //Sun Jul 31 2016 00:00:00 USA standard!!!
    Date periodMonthStart = evalDatePeriodStartFor(date, EPeriod.MONTHLY);
    System.out.println("period month start is " + periodMonthStart);
    assertEquals(new Date(1469998800000L), periodMonthStart); //Mon Aug 01 2016 00:00:00

    Date nextDayStart = evalDateNextPeriodStart(date, EPeriod.DAILY);
    System.out.println("next day start is " + nextDayStart);
    assertEquals(new Date(1470258000000L), nextDayStart); //Thu Aug 04 2016 00:00:00
    Date nextWeekStart = evalDateNextPeriodStart(date, EPeriod.WEEKLY);
    System.out.println("next week start is " + nextWeekStart);
    assertEquals(new Date(1470517200000L), nextWeekStart); //Sun Aug 07 2016 00:00:00 USA standard!!!
    Date nextMonthStart = evalDateNextPeriodStart(date, EPeriod.MONTHLY);
    System.out.println("next month start is " + nextMonthStart);
    assertEquals(new Date(1472677200000L), nextMonthStart); //Thu Sep 01 2016 00:00:00
  }

  @Test
  public void test2() throws Exception {
    Date date = new Date(1451649720000L); //Fri Jan 01 2016 15:02:00

    System.out.println("date is " + date);

    Date periodDayStart = evalDatePeriodStartFor(date, EPeriod.DAILY);
    System.out.println("period day start is " + periodDayStart);
    assertEquals(new Date(1451595600000L), periodDayStart); //Fri Jan 01 2016 00:00:00
    Date periodWeekStart = evalDatePeriodStartFor(date, EPeriod.WEEKLY);
    System.out.println("period week start is " + periodWeekStart);
    assertEquals(new Date(1451163600000L), periodWeekStart); //Sun Dec 27 2015 00:00:00 USA standard!!!
    Date periodMonthStart = evalDatePeriodStartFor(date, EPeriod.MONTHLY);
    System.out.println("period month start is " + periodMonthStart);
    assertEquals(new Date(1451595600000L), periodMonthStart); //Fri Jan 01 2016 00:00:00

    date = new Date(1483185720000L); // Sat Dec 31 2016 15:02:00
    System.out.println("date is " + date);
    Date nextDayStart = evalDateNextPeriodStart(date, EPeriod.DAILY);
    System.out.println("next day start is " + nextDayStart);
    assertEquals(new Date(1483218000000L), nextDayStart); //Sun Jan 01 2017 00:00:00
    Date nextWeekStart = evalDateNextPeriodStart(date, EPeriod.WEEKLY);
    System.out.println("next week start is " + nextWeekStart);
    assertEquals(new Date(1483218000000L), nextWeekStart); //Sun Jan 01 2017 00:00:00 USA standard!!!
    Date nextMonthStart = evalDateNextPeriodStart(date, EPeriod.MONTHLY);
    System.out.println("next month start is " + nextMonthStart);
    assertEquals(new Date(1483218000000L), nextMonthStart); //Sun Jan 01 2017 00:00:00
  }

  /**
   * <p>Evaluate date start of next balance store period.</p>
   * @param pDateFor date for
   * @return Start of next period nearest to pDateFor
   * @throws Exception - an exception
   **/
  public final Date evalDateNextPeriodStart(Date pDateFor, EPeriod period) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    if (period.equals(EPeriod.DAILY)) {
      cal.add(Calendar.DATE, 1);
    } else if (period.equals(EPeriod.MONTHLY)) {
      cal.add(Calendar.MONTH, 1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (period.equals(EPeriod.WEEKLY)) {
      cal.add(Calendar.DAY_OF_YEAR, 7);
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }

  /**
   * <p>Evaluate start of period nearest to pDateFor.</p>
   * @param pDateFor date for
   * @return Start of period nearest to pDateFor
   * @throws Exception - an exception
   **/
  public final synchronized Date evalDatePeriodStartFor(
    Date pDateFor, EPeriod period) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0); //Daily is ready
    if (period.equals(EPeriod.MONTHLY)) {
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (period.equals(EPeriod.WEEKLY)) {
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }
}
