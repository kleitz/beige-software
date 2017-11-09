package org.beigesoft.jdbc.service;

/*
 * Copyright (c) 2016 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.sql.Savepoint;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.jdbc.model.RecordSetJdbc;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.orm.service.ASrvDatabase;

/**
 * <p>Implementation of database service with JDBC.
 * </p>
 *
 * @author Yury Demidenko
 */
public class SrvDatabase extends ASrvDatabase<ResultSet> {

  /**
   * <p>Connection per thread holder.</p>
   **/
  private final ThreadLocal<Connection> threadConnection =
    new ThreadLocal<Connection>() { };

  /**
   * <p>Data source.</p>
   */
  private DataSource dataSource;

  /**
   * <p>Helper to create Insert Update statement
   * by Android way.</p>
   **/
  private HlpInsertUpdate hlpInsertUpdate;

  /**
   * <p>Hold JDBC Savepoint for SQL standard String Savepoint name.</p>
   **/
  private final Map<String, Savepoint> savepointsMap
    = new HashMap<String, Savepoint>();

  /**
   * <p>Get if an transaction is started.</p>
   * @param pIsAutocommit is autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final boolean getIsAutocommit() throws Exception {
    return getOrEstablishConnection().getAutoCommit();
  }

  /**
   * <p>Set RDBMS autocommit mode.</p>
   * @param pIsAutocommit if autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final void setIsAutocommit(
    final boolean pIsAutocommit) throws Exception {
    getOrEstablishConnection().setAutoCommit(pIsAutocommit);
  }

  /**
   * <p>Set transaction isolation level.</p>
   * @param pLevel according ASrvDatabase
   * @throws Exception - an exception
   **/
  @Override
  public final void setTransactionIsolation(
    final int pLevel) throws Exception {
    getOrEstablishConnection();
    if (pLevel == ASrvDatabase.TRANSACTION_READ_UNCOMMITTED) {
      this.threadConnection.get()
        .setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    } else if (pLevel == ASrvDatabase.TRANSACTION_READ_COMMITTED) {
      this.threadConnection.get()
        .setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    } else if (pLevel == ASrvDatabase.TRANSACTION_REPEATABLE_READ) {
      this.threadConnection.get()
        .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
    } else if (pLevel == ASrvDatabase.TRANSACTION_SERIALIZABLE) {
      this.threadConnection.get()
        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    } else if (pLevel == ASrvDatabase.TRANSACTION_NONE) {
      this.threadConnection.get()
        .setTransactionIsolation(Connection.TRANSACTION_NONE);
    } else {
      throw new ExceptionWithCode(ASrvDatabase.BAD_PARAMS,
        "Unknown isolation level parameter!");
    }
  }

  /**
   * <p>Get transaction isolation level.
   * You must {@link #setTransactionIsolation(int) setTransactionIsolation}
   * before invoke it cause a RDBMS may has any level by default.</p>
   * @return pLevel according ASrvDatabase
   * @throws Exception - an exception
   **/
  @Override
  public final int getTransactionIsolation() throws Exception {
    int level = getOrEstablishConnection().getTransactionIsolation();
    if (level == Connection.TRANSACTION_READ_UNCOMMITTED) {
      return ASrvDatabase.TRANSACTION_READ_UNCOMMITTED;
    } else if (level == Connection.TRANSACTION_READ_COMMITTED) {
      return ASrvDatabase.TRANSACTION_READ_COMMITTED;
    } else if (level == Connection.TRANSACTION_REPEATABLE_READ) {
      return ASrvDatabase.TRANSACTION_REPEATABLE_READ;
    } else if (level == Connection.TRANSACTION_SERIALIZABLE) {
      return ASrvDatabase.TRANSACTION_SERIALIZABLE;
    } else if (level == Connection.TRANSACTION_NONE) {
      return ASrvDatabase.TRANSACTION_NONE;
    }
    throw new ExceptionWithCode(ASrvDatabase.BAD_PARAMS,
      "Unknown JDBC isolation level!");
  }

  /**
   * <p>Create savepoint.</p>
   * @param pSavepointName savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void
    createSavepoint(final String pSavepointName) throws Exception {
    Savepoint savepoint = this.threadConnection.get().
      setSavepoint(pSavepointName);
    savepointsMap.put(pSavepointName, savepoint);
  }

  /**
   * <p>Release savepoint.</p>
   * @param pSavepointName savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void
    releaseSavepoint(final String pSavepointName) throws Exception {
    this.threadConnection.get().
      releaseSavepoint(savepointsMap.get(pSavepointName));
    savepointsMap.remove(pSavepointName);
  }

  /**
   * <p>Rollback transaction to savepoint.</p>
   * @param pSavepointName savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void
    rollBackTransaction(final String pSavepointName) throws Exception {
    if (getLogger().getIsShowDebugMessagesFor(getClass())) {
      getLogger().debug(null, SrvDatabase.class,
        "try to rollback to savepoint: " + pSavepointName);
    }
    this.threadConnection.get().
      rollback(savepointsMap.get(pSavepointName));
    savepointsMap.remove(pSavepointName);
  }

  /**
   * <p>Start new transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void beginTransaction() throws Exception {
    getOrEstablishConnection();
  }

  /**
   * <p>Commit transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void commitTransaction() throws Exception {
    this.threadConnection.get().commit();
  }

  /**
   * <p>Rollback transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void rollBackTransaction() throws Exception {
    this.threadConnection.get().rollback();
  }

  /**
   * <p>Release resources if they is not null.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void releaseResources() throws Exception {
    if (this.threadConnection.get() != null) {
      this.threadConnection.get().setAutoCommit(true);
      this.threadConnection.get().close();
      this.threadConnection.set(null);
    }
  }

  /**
   * <p>Retrieve records from DB.</p>
   * @param pSelect query SELECT
   * @return IRecordSet record set
   * @throws Exception - an exception
   **/
  @Override
  public final IRecordSet<ResultSet>
    retrieveRecords(final String pSelect) throws Exception {
    try {
      if (getLogger().getIsShowDebugMessagesFor(getClass())) {
        getLogger().debug(null, SrvDatabase.class, "Thread ID="
          + Thread.currentThread().getId() + ", try to retrieve records: "
            + pSelect);
      }
      final Statement stmt = getOrEstablishConnection().createStatement();
      final ResultSet rs = stmt.executeQuery(pSelect);
      return new RecordSetJdbc(rs, stmt);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", query:\n" + pSelect;
      throw new ExceptionWithCode(ASrvDatabase.SQL_EXEC_ERROR, msg);
    }
  }

  /**
   * <p>Execute any SQL query that returns no data.
   * E.g. PRAGMA, etc.</p>
   * @param pQuery query
   * @throws Exception - an exception
   **/
  @Override
  public final void executeQuery(final String pQuery) throws Exception {
    Statement stmt = null;
    try {
      if (getLogger().getIsShowDebugMessagesFor(getClass())) {
        getLogger().debug(null, SrvDatabase.class, "Thread ID="
          + Thread.currentThread().getId() + ", try to execute query: "
            + pQuery);
      }
      stmt = getOrEstablishConnection().createStatement();
      stmt.executeUpdate(pQuery);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", query:\n" + pQuery;
      throw new ExceptionWithCode(ASrvDatabase.SQL_EXEC_ERROR, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * <p>Execute SQL UPDATE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.
   * </p>
   * @param pTable table name
   * @param pColumnsVals type-safe map column name - column value
   * @param pWhere where conditions e.g. "itsId=2"
   * @return row count affected
   * @throws Exception - an exception
   **/
  @Override
  public final int executeUpdate(final String pTable,
    final ColumnsValues pColumnsVals,
     final String pWhere) throws Exception {
    Statement stmt = null;
    String query = getHlpInsertUpdate().evalSqlUpdate(pTable,
      pColumnsVals, pWhere);
    try {
      if (getLogger().getIsShowDebugMessagesFor(getClass())) {
        getLogger().debug(null, SrvDatabase.class, "Thread ID="
          + Thread.currentThread().getId() + ", try to execute update: "
            + query);
      }
      stmt = getOrEstablishConnection().createStatement();
      return stmt.executeUpdate(query);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", query:\n" + query;
      throw new ExceptionWithCode(ASrvDatabase.SQL_EXEC_ERROR, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * <p>Execute SQL INSERT that returns affected rows.
   * It is to adapt Android insert/update/delete interface.
   * </p>
   * @param pTable table name
   * @param pColumnsVals type-safe map column name - column value
   * @return row count affected
   * @throws Exception - an exception
   **/
  @Override
  public final long executeInsert(final String pTable,
    final ColumnsValues pColumnsVals) throws Exception {
    Statement stmt = null;
    String query = getHlpInsertUpdate().evalSqlInsert(pTable, pColumnsVals);
    try {
      if (getLogger().getIsShowDebugMessagesFor(getClass())) {
        getLogger().debug(null, SrvDatabase.class, "Thread ID="
          + Thread.currentThread()
          .getId() + ", try to execute insert: "
          + query);
      }
      stmt = getOrEstablishConnection().createStatement();
      return stmt.executeUpdate(query);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", query:\n" + query;
      throw new ExceptionWithCode(ASrvDatabase.SQL_EXEC_ERROR, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * <p>Execute SQL DELETE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.
   * </p>
   * @param pTable table name
   * @param pWhere where conditions e.g. "itsId=2" or NULL -delete all
   * @return row count affected
   * @throws Exception - an exception
   **/
  @Override
  public final int executeDelete(final String pTable,
    final String pWhere) throws Exception {
    Statement stmt = null;
    String strWhere = "";
    if (pWhere != null) {
      strWhere = " where " + pWhere;
    }
    String query = "delete from " + pTable + strWhere + ";";
    try {
      if (getLogger().getIsShowDebugMessagesFor(getClass())) {
        getLogger().debug(null, SrvDatabase.class, "Thread ID="
          + Thread.currentThread().getId() + ", try to execute delete: "
            + query);
      }
      stmt = getOrEstablishConnection().createStatement();
      return stmt.executeUpdate(query);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", query:\n" + query;
      throw new ExceptionWithCode(ASrvDatabase.SQL_EXEC_ERROR, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * <p>Getor establish connection per thread.</p>
   * @return DataSource
   * @throws Exception - an exception
   **/
  public final Connection getOrEstablishConnection() throws Exception {
    Connection con = this.threadConnection.get();
    if (con == null) {
      con = dataSource.getConnection();
      threadConnection.set(con);
    }
    return con;
  }

  /**
   * <p>Getter for current connection per thread.</p>
   * @return DataSource
   * @throws Exception - an exception
   **/
  public final Connection getCurrentConnection() throws Exception {
    return this.threadConnection.get();
  }

  //Simple getters and setters:
  /**
   * <p>Geter for threadConnection.</p>
   * @return final ThreadLocal<Connection>
   **/
  public final ThreadLocal<Connection> getThreadConnection() {
    return this.threadConnection;
  }

  /**
   * <p>Geter for dataSource.</p>
   * @return DataSource
   **/
  public final DataSource getDataSource() {
    return this.dataSource;
  }

  /**
   * <p>Setter for dataSource.</p>
   * @param pDataSource reference
   **/
  public final void setDataSource(final DataSource pDataSource) {
    this.dataSource = pDataSource;
  }

  /**
   * <p>Geter for hlpInsertUpdate.</p>
   * @return HlpInsertUpdate
   **/
  public final HlpInsertUpdate getHlpInsertUpdate() {
    return this.hlpInsertUpdate;
  }

  /**
   * <p>Setter for hlpInsertUpdate.</p>
   * @param pHlpInsertUpdate reference
   **/
  public final void setHlpInsertUpdate(final HlpInsertUpdate pHlpInsertUpdate) {
    this.hlpInsertUpdate = pHlpInsertUpdate;
  }

  /**
   * <p>Geter for savepointsMap.</p>
   * @return Map<String, Savepoint>
   **/
  public final Map<String, Savepoint> getSavepointsMap() {
    return this.savepointsMap;
  }
}
