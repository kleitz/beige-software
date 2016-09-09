package org.beigesoft.jdbc.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

  //Simple getters and setters:
  /**
   * <p>Geter for Statement.</p>
   * @return Statement
   **/
  public final Statement getStatement() {
    return this.statement;
  }
}
