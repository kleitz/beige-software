package org.beigesoft.android.sqlite.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
}
