package org.beigesoft.android.sqlite.model;

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

import android.database.Cursor;

import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Recordset adapter implementation on Android.
 * </p>
 *
 * @author Yury Demidenko
 */
public class RecordSetAndroid implements IRecordSet<Cursor> {

  /**
   * <p>Android recordset.</p>
   **/
  private final Cursor resultSet;

  /**
   * <p>Android recordset.</p>
   * @param pResultSet result set
   **/
  public RecordSetAndroid(final Cursor pResultSet) {
    this.resultSet = pResultSet;
  }

  @Override
  public final String toString() {
    StringBuffer columns = new StringBuffer();
    for (String cn :  this.resultSet.getColumnNames()) {
      columns.append(" " + cn);
    }
    return "Columns total: " + this.resultSet.getColumnCount()
      + "\nRows total: " + this.resultSet.getCount()
      + "\nColumns: " + columns.toString();
  }

  /**
   * <p>Geter for ResultSet.</p>
   * @return ResultSet
   **/
  @Override
  public final Cursor getRecordSet() {
    return this.resultSet;
  }

  /**
   * <p>Move cursor to next record.</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean moveToNext() throws Exception {
    return this.resultSet.moveToNext();
  }

  /**
   * <p>Move cursor to first record (for Android compatible).</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean moveToFirst() throws Exception {
    return this.resultSet.moveToFirst();
  }

  /**
   * <p>Close resultset, for JDBC close statement.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void close() throws Exception {
    this.resultSet.close();
  }

  /**
   * <p>Retrieve String column value.</p>
   * @param pColumnName column name
   * @return String result
   * @throws Exception - an exception
   **/
  @Override
  public final String getString(final String pColumnName) throws Exception {
    int columnIndex = this.resultSet.getColumnIndex(pColumnName);
    return this.resultSet.getString(columnIndex);
  }

  /**
   * <p>Retrieve Double column value.</p>
   * @param pColumnName column name
   * @return Double result
   * @throws Exception - an exception
   **/
  @Override
  public final Double getDouble(final String pColumnName) throws Exception {
    int columnIndex = this.resultSet.getColumnIndex(pColumnName);
    Double result = null;
    if (!this.resultSet.isNull(columnIndex)) {
      result = this.resultSet.getDouble(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve Float column value.</p>
   * @param pColumnName column name
   * @return Float result
   * @throws Exception - an exception
   **/
  @Override
  public final Float getFloat(final String pColumnName) throws Exception {
    int columnIndex = this.resultSet.getColumnIndex(pColumnName);
    Float result = null;
    if (!this.resultSet.isNull(columnIndex)) {
      result = this.resultSet.getFloat(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve Integer column value.</p>
   * @param pColumnName column name
   * @return Integer result
   * @throws Exception - an exception
   **/
  @Override
  public final Integer getInteger(final String pColumnName) throws Exception {
    int columnIndex = this.resultSet.getColumnIndex(pColumnName);
    Integer result = null;
    if (!this.resultSet.isNull(columnIndex)) {
      result = this.resultSet.getInt(columnIndex);
    }
    return result;
  }

  /**
   * <p>Retrieve Long column value.</p>
   * @param pColumnName column name
   * @return Long result
   * @throws Exception - an exception
   **/
  @Override
  public final Long getLong(final String pColumnName) throws Exception {
    int columnIndex = this.resultSet.getColumnIndex(pColumnName);
    Long result = null;
    if (!this.resultSet.isNull(columnIndex)) {
      result = this.resultSet.getLong(columnIndex);
    }
    return result;
  }
}
