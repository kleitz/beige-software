package org.beigesoft.orm.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <p>Record-set abstraction to adapt JDBC or Android one.
 * It's SQlite fields types oriented.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public interface IRecordSet<RS> {

  /**
   * <p>Getter for record set.</p>
   * @return RS record set
   **/
  RS getRecordSet();

  /**
   * <p>Move cursor to first record (for Android compatible).</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  boolean moveToFirst() throws Exception;

  /**
   * <p>Move cursor to next record.</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  boolean moveToNext() throws Exception;

  /**
   * <p>Close resultset, for JDBC close statement.</p>
   * @throws Exception - an exception
   **/
  void close() throws Exception;

  /**
   * <p>Retrieve String column value.</p>
   * @param pColumnName column name
   * @return String result
   * @throws Exception - an exception
   **/
  String getString(String pColumnName) throws Exception;

  /**
   * <p>Retrieve Double column value.</p>
   * @param pColumnName column name
   * @return Double result
   * @throws Exception - an exception
   **/
  Double getDouble(String pColumnName) throws Exception;

  /**
   * <p>Retrieve Float column value.</p>
   * @param pColumnName column name
   * @return Float result
   * @throws Exception - an exception
   **/
  Float getFloat(String pColumnName) throws Exception;

  /**
   * <p>Retrieve Integer column value.</p>
   * @param pColumnName column name
   * @return Integer result
   * @throws Exception - an exception
   **/
  Integer getInteger(String pColumnName) throws Exception;

  /**
   * <p>Retrieve Long column value.</p>
   * @param pColumnName column name
   * @return Long result
   * @throws Exception - an exception
   **/
  Long getLong(String pColumnName) throws Exception;
}
