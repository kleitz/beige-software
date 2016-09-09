package org.beigesoft.jdbc.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.sql.ResultSet;
import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.orm.service.ISrvRecordRetriever;

/**
 * <p>Record retriever base JDBC.
 * </p>
 *
 * @author Yury Demidenko
 */
public class SrvRecordRetriever
    implements ISrvRecordRetriever<ResultSet> {

  /**
   * <p>Retrieve String column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return String result
   * @throws Exception - an exception
   **/
  @Override
  public final String getString(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    return pRecordSet.getString(pColumnName);
  }

  /**
   * <p>Retrieve Double column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Double result
   * @throws Exception - an exception
   **/
  @Override
  public final Double getDouble(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    Double result = pRecordSet.getDouble(pColumnName);
    if (pRecordSet.wasNull()) {
      return null;
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
  public final Float getFloat(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    Float result = pRecordSet.getFloat(pColumnName);
    if (pRecordSet.wasNull()) {
      return null;
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
  public final Integer getInteger(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    Integer result = pRecordSet.getInt(pColumnName);
    if (pRecordSet.wasNull()) {
      return null;
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
  public final Long getLong(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    Long result = pRecordSet.getLong(pColumnName);
    if (pRecordSet.wasNull()) {
      return null;
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
  public final BigDecimal getBigDecimal(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    double result = pRecordSet.getDouble(pColumnName);
    if (pRecordSet.wasNull()) {
      return null;
    }
    return BigDecimal.valueOf(result);
  }

  /**
   * <p>Retrieve Boolean column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @throws Exception - an exception
   * @return Boolean result
   **/
  @Override
  public final Boolean getBoolean(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    //The simple, the better
    //Findbugs NP_BOOLEAN_RETURN_NULL prohibit Boolean to be null
    //So Boolean must be allways initialized to false/true
    //And database fields that hold Booleans must be not null
    //And any user interface also must treat Booleans as false/true, not null
    //For 3-states values use Enum "YES/NO" instead where null
    //equivalent "not answered yet"
    return pRecordSet.getInt(pColumnName) != 0;
  }

  /**
   * <p>Retrieve Date column value.</p>
   * @param pRecordSet record set
   * @param pColumnName column name
   * @return Date result
   * @throws Exception - an exception
   **/
  @Override
  public final Date getDate(final ResultSet pRecordSet,
    final String pColumnName) throws Exception {
    Long val = pRecordSet.getLong(pColumnName);
    if (pRecordSet.wasNull()) {
      return null;
    }
    return new Date(val);
  }
}
