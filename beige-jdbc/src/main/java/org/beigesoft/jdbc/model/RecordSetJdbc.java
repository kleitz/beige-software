package org.beigesoft.jdbc.model;

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

import java.sql.Statement;
import java.sql.ResultSet;

import org.beigesoft.orm.model.IRecordSet;

/**
 * <p>Recordset adapter implementation with JDBC.
 * </p>
 *
 * @author Yury Demidenko
 */
public class RecordSetJdbc implements IRecordSet<ResultSet> {

  /**
   * <p>JDBC recordset.</p>
   **/
  private final ResultSet resultSet;

  /**
   * <p>JDBC recordset.</p>
   **/
  private final Statement statement;

  /**
   * <p>JDBC recordset.</p>
   * @param pResultSet result set
   * @param pStatement JDBC statement
   **/
  public RecordSetJdbc(final ResultSet pResultSet, final Statement pStatement) {
    this.resultSet = pResultSet;
    this.statement = pStatement;
  }

  /**
   * <p>Geter for ResultSet.</p>
   * @return ResultSet
   **/
  @Override
  public final ResultSet getRecordSet() {
    return this.resultSet;
  }

  /**
   * <p>Move cursor to next record.</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean moveToNext() throws Exception {
    return this.resultSet.next();
  }

  /**
   * <p>Move cursor to first record (for Android compatible).</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean moveToFirst() throws Exception {
    return this.resultSet.next();
  }

  /**
   * <p>Close resultset, for JDBC close statement.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void close() throws Exception {
    this.resultSet.close();
    this.statement.close();
  }


  /**
   * <p>Retrieve String column value.</p>
   * @param pColumnName column name
   * @return String result
   * @throws Exception - an exception
   **/
  @Override
  public final String getString(final String pColumnName) throws Exception {
    // JSE API -  if the value is SQL NULL, the value returned is null
    return this.resultSet.getString(pColumnName);
  }

  /**
   * <p>Retrieve Double column value.</p>
   * @param pColumnName column name
   * @return Double result
   * @throws Exception - an exception
   **/
  @Override
  public final Double getDouble(final String pColumnName) throws Exception {
    Double result = this.resultSet.getDouble(pColumnName);
    if (this.resultSet.wasNull()) {
      return null;
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
    Float result = this.resultSet.getFloat(pColumnName);
    if (this.resultSet.wasNull()) {
      return null;
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
    Integer result = this.resultSet.getInt(pColumnName);
    if (this.resultSet.wasNull()) {
      return null;
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
    Long result = this.resultSet.getLong(pColumnName);
    if (this.resultSet.wasNull()) {
      return null;
    }
    return result;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for Statement.</p>
   * @return Statement
   **/
  public final Statement getStatement() {
    return this.statement;
  }
}
