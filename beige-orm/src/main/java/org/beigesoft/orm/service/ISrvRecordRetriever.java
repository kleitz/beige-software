package org.beigesoft.orm.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Recordset retriever abstraction to adapt JDBC or Android one.
 * There is neither type Boolean nor BigDecimal in Sqlite.
 * So it must be implemented retriever for a RDBMS.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public interface ISrvRecordRetriever<RS> {

  /**
   * <p>Retrieve String column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return String result
   * @throws Exception - an exception
   **/
  String getString(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve BigDecimal column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return BigDecimal result
   * @throws Exception - an exception
   **/
  BigDecimal getBigDecimal(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve Double column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Double result
   * @throws Exception - an exception
   **/
  Double getDouble(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve Float column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Float result
   * @throws Exception - an exception
   **/
  Float getFloat(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve Integer column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Integer result
   * @throws Exception - an exception
   **/
  Integer getInteger(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve Long column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Long result
   * @throws Exception - an exception
   **/
  Long getLong(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve Boolean column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Boolean result
   * @throws Exception - an exception
   **/
  Boolean getBoolean(RS pRecordSet, String pColumnName) throws Exception;

  /**
   * <p>Retrieve Date column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Date result
   * @throws Exception - an exception
   **/
  Date getDate(RS pRecordSet, String pColumnName) throws Exception;
}
