package org.beigesoft.android.sqlite.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Date;
import java.math.BigDecimal;

import android.database.Cursor;

import org.beigesoft.orm.service.ISrvRecordRetriever;

/**
 * <p>Record retriever Android.
 * </p>
 *
 * @author Yury Demidenko
 */
public class SrvRecordRetriever
  implements ISrvRecordRetriever<Cursor> {

  /**
   * <p>Retrieve String column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return String result
   * @throws Exception - an exception
   **/
  @Override
  public final String getString(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    return pRecordSet.getString(columnIndex);
  }

  /**
   * <p>Retrieve Double column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Double result
   * @throws Exception - an exception
   **/
  @Override
  public final Double getDouble(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    Double result = null;
    if (!pRecordSet.isNull(columnIndex)) {
      result = pRecordSet.getDouble(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve Float column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Float result
   * @throws Exception - an exception
   **/
  @Override
  public final Float getFloat(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    Float result = null;
    if (!pRecordSet.isNull(columnIndex)) {
      result = pRecordSet.getFloat(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve Integer column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Integer result
   * @throws Exception - an exception
   **/
  @Override
  public final Integer getInteger(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    Integer result = null;
    if (!pRecordSet.isNull(columnIndex)) {
      result = pRecordSet.getInt(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve Long column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Long result
   * @throws Exception - an exception
   **/
  @Override
  public final Long getLong(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    Long result = null;
    if (!pRecordSet.isNull(columnIndex)) {
      result = pRecordSet.getLong(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve BigDecimal column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return BigDecimal result
   * @throws Exception - an exception
   **/
  @Override
  public final BigDecimal getBigDecimal(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    BigDecimal result = null;
    if (!pRecordSet.isNull(columnIndex)) {
      result = BigDecimal.valueOf(pRecordSet.getDouble(columnIndex));
    }
    return result;
  }

  /**
   * <p>Retrieve Boolean column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @throws Exception - an exception
   * @return Boolean result
   **/
  @Override
  public final Boolean getBoolean(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    //The simple, the better
    //Findbugs NP_BOOLEAN_RETURN_NULL prohibit Boolean to be null
    //So Boolean must be allways initialized to false/true
    //And database fields that hold Booleans must be not null
    //And any user interface also must treat Booleans as false/true, not null
    //For 3 states values use Enum "YES/NO" instead where null
    //equivalent "not answered yet"
    return pRecordSet.getInt(columnIndex) != 0;
  }

  /**
   * <p>Retrieve Date column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Date result
   * @throws Exception - an exception
   **/
  @Override
  public final Date getDate(final Cursor pRecordSet,
    final String pColumnName) throws Exception {
    int columnIndex = pRecordSet.getColumnIndex(pColumnName);
    Date result = null;
    if (!pRecordSet.isNull(columnIndex)) {
      result = new Date(pRecordSet.getLong(columnIndex));
    }
    return result;
  }
}
