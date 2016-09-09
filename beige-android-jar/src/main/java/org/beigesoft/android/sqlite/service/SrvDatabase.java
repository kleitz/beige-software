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

import java.util.Map;

import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.android.sqlite.model.RecordSetAndroid;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.service.ASrvDatabase;

/**
 * <p>Implementation of database service on Android.
 * Android SQLite Database not designed to be multiconnection.
 * So two or more requests on IJetty at same time will be sharing
 * the same connection, and result will be bad. E.g. thread1 for request1
 * invoke "setAutocommit(true)", then thread2 for request2
 * invoke "setAutocommit(false)", then thread1 invoke "beginTransaction"...
 * </p>
 *
 * @author Yury Demidenko
 */
public class SrvDatabase extends ASrvDatabase<Cursor> {

  /**
   * <p>SQLiteDatabase one for all threads.</p>
   **/
  private SQLiteDatabase sqliteDatabase;

  /**
   * <p>Autocommit flag.</p>
   */
  private boolean isAutocommit;

  /**
   * <p>Get if an transaction is started.</p>
   * @param pIsAutocommit is autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final boolean getIsAutocommit() throws Exception {
    return this.isAutocommit;
  }

  /**
   * <p>Set RDBMS autocommit mode.</p>
   * @param pIsAutocommit if autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final void setIsAutocommit(
    final boolean pIsAutocommit) throws Exception {
    this.isAutocommit = pIsAutocommit;
  }

  /**
   * <p>Set transaction isolation level.
   * For single-connection Android it doesnt care.</p>
   * @param pLevel according ISrvOrm
   * @throws Exception - an exception
   **/
  @Override
  public final void setTransactionIsolation(
    final int pLevel) throws Exception {
    //nothing
  }

  /**
   * <p>Get transaction isolation level.
   * For single-connection Android it doesnt care.</p>
   * @return pLevel according ISrvOrm
   * @throws Exception - an exception
   **/
  @Override
  public final int getTransactionIsolation() throws Exception {
    return TRANSACTION_SERIALIZABLE;
  }

  /**
   * <p>Create savepoint.</p>
   * @param pSavepointName savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void
    createSavepoint(final String pSavepointName) throws Exception {
    executeQuery("SAVEPOINT " + pSavepointName + ";");
  }

  /**
   * <p>Release savepoint.</p>
   * @param pSavepointName savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void
    releaseSavepoint(final String pSavepointName) throws Exception {
    executeQuery("RELEASE " + pSavepointName + ";");
  }

  /**
   * <p>Rollback transaction to savepoint.</p>
   * @param pSavepointName savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void
    rollBackTransaction(final String pSavepointName) throws Exception {
    //according https://code.google.com/p/android/issues/detail?id=38706
    //its very easy to make simple things complicated
    getLogger().debug(SrvDatabase.class, "try to rollback to savepoint: "
      + pSavepointName);
    executeQuery(";ROLLBACK TRANSACTION TO SAVEPOINT " + pSavepointName + ";");
  }

  /**
   * <p>Start new transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void beginTransaction() throws Exception {
    executeQuery("BEGIN TRANSACTION;");
    setIsAutocommit(false);
  }

  /**
   * <p>Commit transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void commitTransaction() throws Exception {
    executeQuery("COMMIT TRANSACTION;");
    setIsAutocommit(true);
  }

  /**
   * <p>Rollback transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void rollBackTransaction() throws Exception {
    executeQuery("ROLLBACK TRANSACTION;");
    setIsAutocommit(true);
  }

  /**
   * <p>Release only unneeded memory.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void releaseResources() throws Exception {
    if (this.sqliteDatabase != null) {
      this.sqliteDatabase.releaseMemory();
    }
  }

  /**
   * <p>Retrieve records from DB.</p>
   * @param pSelect query SELECT
   * @return IRecordSet record set
   * @throws ExceptionWithCode - if an exception occured
   **/
  @Override
  public final IRecordSet<Cursor>
    retrieveRecords(final String pSelect) throws ExceptionWithCode {
    try {
      getLogger().debug(SrvDatabase.class, "try to retrieve records: "
        + pSelect);
      Cursor rs = this.sqliteDatabase.rawQuery(pSelect, null);
      RecordSetAndroid rsa = new RecordSetAndroid(rs);
      getLogger().debug(SrvDatabase.class, "Recordset: " + rsa);
      return rsa;
    } catch (Exception ex) {
      String msg = ex.getMessage() + ", query:\n" + pSelect;
      ExceptionWithCode ewc = new ExceptionWithCode(SrvDatabase.SQL_EXEC_ERROR,
        msg);
      ewc.setStackTrace(ex.getStackTrace());
      throw ewc;
    }
  }

  /**
   * <p>Execute any SQL query that returns no data.
   * E.g. PRAGMA, etc.</p>
   * @param pQuery query
   * @throws ExceptionWithCode - if an exception occured
   **/
  @Override
  public final void executeQuery(final String pQuery) throws Exception {
    try {
      getLogger().debug(SrvDatabase.class, "try to execute query: "
        + pQuery);
      this.sqliteDatabase.execSQL(pQuery);
    } catch (Exception ex) {
      String msg = ex.getMessage() + ", query:\n" + pQuery;
      ExceptionWithCode ewc = new ExceptionWithCode(SrvDatabase.SQL_EXEC_ERROR,
        msg);
      ewc.setStackTrace(ex.getStackTrace());
      throw ewc;
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
   * @throws ExceptionWithCode - if an exception occured
   **/
  @Override
  public final int executeUpdate(final String pTable,
    final ColumnsValues pColumnsVals,
     final String pWhere) throws Exception {
    try {
      getLogger().debug(SrvDatabase.class, "try to update t: " + pTable
        + " where: " + pWhere + " cv: " + pColumnsVals);
      ContentValues contentValues = convertToContentValues(pColumnsVals);
      return this.sqliteDatabase.update(pTable,
        contentValues, pWhere, null);
    } catch (Exception ex) {
      String msg = ex.getMessage() + ", table: " + pTable + ", values: "
        + pColumnsVals.toString() + ", where: " + pWhere;
      ExceptionWithCode ewc = new ExceptionWithCode(SrvDatabase.SQL_EXEC_ERROR,
        msg);
      ewc.setStackTrace(ex.getStackTrace());
      throw ewc;
    }
  }

  /**
   * <p>Execute SQL INSERT that returns affected rows.
   * It is to adapt Android insert/update/delete interface.
   * </p>
   * @param pTable table name
   * @param pColumnsVals type-safe map column name - column value
   * @return -1 - error or autogenerated key
   * or ? maybe 1 for String key ?
   * @throws ExceptionWithCode - if an exception occured
   **/
  @Override
  public final long executeInsert(final String pTable,
    final ColumnsValues pColumnsVals) throws Exception {
    try {
      getLogger().debug(SrvDatabase.class, "try to insert t: " + pTable
        + " cv: " + pColumnsVals);
      ContentValues contentValues = convertToContentValues(pColumnsVals);
      long result = this.sqliteDatabase.insert(pTable, null,
        contentValues);
      getLogger().debug(SrvDatabase.class, "result insert:" + result);
      if (result == -1) {
        throw new Exception("Result = -1!");
      }
      return result;
    } catch (Exception ex) {
      String msg = ex.getMessage() + ", table: " + pTable + ", values: "
        + pColumnsVals.toString();
      ExceptionWithCode ewc = new ExceptionWithCode(SrvDatabase.SQL_EXEC_ERROR,
        msg);
      ewc.setStackTrace(ex.getStackTrace());
      throw ewc;
    }
  }

  /**
   * <p>Execute SQL DELETE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.
   * </p>
   * @param pTable table name
   * @param pWhere where conditions e.g. "itsId=2" or NULL -delete all
   * @return row count affected
   * @throws ExceptionWithCode - if an exception occured
   **/
  @Override
  public final int executeDelete(final String pTable,
    final String pWhere) throws Exception {
    try {
      getLogger().debug(SrvDatabase.class, "try to delete t: " + pTable
        + " where: " + pWhere);
      return this.sqliteDatabase.delete(pTable, pWhere, null);
    } catch (Exception ex) {
      String msg = ex.getMessage() + ", table: " + pTable
        + ", where: " + pWhere;
      ExceptionWithCode ewc = new ExceptionWithCode(SrvDatabase.SQL_EXEC_ERROR,
        msg);
      ewc.setStackTrace(ex.getStackTrace());
      throw ewc;
    }
  }

  @Override
  protected final void finalize() {
    if (this.sqliteDatabase != null) {
      this.sqliteDatabase.close();
    }
  }

  //Utils:
  /**
   * <p>Convert org.beigesoft.orm.model.ColumnsValues
   * to android.content.ContentValues.</p>
   * @param pColumnsVals Columns Values
   * @return ContentValues
   **/
  public final ContentValues convertToContentValues(
    final ColumnsValues pColumnsVals) {
    ContentValues contentValues = new ContentValues();
    for (Map.Entry<String, Integer> entry
      : pColumnsVals.getIntegersMap().entrySet()) {
      contentValues.put(entry.getKey().toUpperCase(), entry.getValue());
    }
    for (Map.Entry<String, Integer> entry
      : pColumnsVals.getIntegersMap().entrySet()) {
      contentValues.put(entry.getKey().toUpperCase(), entry.getValue());
    }
    for (Map.Entry<String, Long> entry
      : pColumnsVals.getLongsMap().entrySet()) {
      contentValues.put(entry.getKey().toUpperCase(), entry.getValue());
    }
    for (Map.Entry<String, Float> entry
      : pColumnsVals.getFloatsMap().entrySet()) {
      contentValues.put(entry.getKey().toUpperCase(), entry.getValue());
    }
    for (Map.Entry<String, Double> entry
      : pColumnsVals.getDoublesMap().entrySet()) {
      contentValues.put(entry.getKey().toUpperCase(), entry.getValue());
    }
    for (Map.Entry<String, String> entry
      : pColumnsVals.getStringsMap().entrySet()) {
      contentValues.put(entry.getKey().toUpperCase(), entry.getValue());
    }
    return contentValues;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for sqliteDatabase.</p>
   * @return SQLiteDatabase
   **/
  public final SQLiteDatabase getSqliteDatabase() {
    return this.sqliteDatabase;
  }

  /**
   * <p>Setter for sqliteDatabase.</p>
   * @param pSqliteDatabase reference
   **/
  public final void setSqliteDatabase(final SQLiteDatabase pSqliteDatabase) {
    this.sqliteDatabase = pSqliteDatabase;
  }
}
