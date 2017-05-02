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

import java.util.Map;

import org.beigesoft.orm.model.ColumnsValues;

/**
 * <p>Helper to create Insert Update statement
 * by Android way (see ISrvDatabase).
 * JDBC SrvDatabase use it.
 * </p>
 *
 * @author Yury Demidenko
 */
public class HlpInsertUpdate {

  /**
   * <p>Evaluate SQL insert statement.
   * </p>
   * @param pTable table name
   * @param pColumnsVals type-safe map column name - column value
   * @return row count affected
   * @throws Exception - an exception
   **/
  public final String evalSqlInsert(final String pTable,
    final ColumnsValues pColumnsVals) throws Exception  {
    StringBuffer result = new StringBuffer("insert into "
      + pTable + " (");
    StringBuffer values = new StringBuffer(" values (");
    boolean isFirstField = true;
    for (Map.Entry<String, Integer> entry
      : pColumnsVals.getIntegersMap().entrySet()) {
      if (!(isArrContains(entry.getKey(), pColumnsVals.getIdColumnsNames())
        && entry.getValue() == null)) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(", ");
          values.append(", ");
        }
        result.append(entry.getKey().toUpperCase());
        values.append(pColumnsVals.evalSqlValue(entry.getKey()));
      }
    }
    for (Map.Entry<String, Long> entry
      : pColumnsVals.getLongsMap().entrySet()) {
      if (!(isArrContains(entry.getKey(), pColumnsVals.getIdColumnsNames())
        && entry.getValue() == null)
          && !(ISrvOrm.VERSIONOLD_NAME.equals(entry.getKey()))) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(", ");
          values.append(", ");
        }
        result.append(entry.getKey().toUpperCase());
        values.append(pColumnsVals.evalSqlValue(entry.getKey()));
      }
    }
    for (Map.Entry<String, String> entry
      : pColumnsVals.getStringsMap().entrySet()) {
      if (!(isArrContains(entry.getKey(), pColumnsVals.getIdColumnsNames())
        && entry.getValue() == null)) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(", ");
          values.append(", ");
        }
        result.append(entry.getKey().toUpperCase());
        values.append(pColumnsVals.evalSqlValue(entry.getKey()));
      }
    }
    for (Map.Entry<String, Float> entry
      : pColumnsVals.getFloatsMap().entrySet()) {
      if (isFirstField) {
        isFirstField = false;
      } else {
        result.append(", ");
        values.append(", ");
      }
      result.append(entry.getKey().toUpperCase());
      values.append(pColumnsVals.evalSqlValue(entry.getKey()));
    }
    for (Map.Entry<String, Double> entry
      : pColumnsVals.getDoublesMap().entrySet()) {
      if (isFirstField) {
        isFirstField = false;
      } else {
        result.append(", ");
        values.append(", ");
      }
      result.append(entry.getKey().toUpperCase());
      values.append(pColumnsVals.evalSqlValue(entry.getKey()));
    }
    result.append(")\n");
    result.append(values + ")");
    return result.toString();
  }

  /**
   * <p>Evaluate SQL update statement.
   * </p>
   * @param pTable table name
   * @param pColumnsVals type-safe map column name - column value
   * @param pWhere where conditions e.g. "itsId=2 AND itsVersion=2"
   * @return row count affected
   * @throws Exception - an exception
   **/
  public final String evalSqlUpdate(final String pTable,
    final ColumnsValues pColumnsVals,
      final String pWhere) throws Exception {
    StringBuffer result = new StringBuffer("update "
      + pTable + " set \n");
    boolean isFirstField = true;
    for (String key : pColumnsVals.getIntegersMap().keySet()) {
      if (!isArrContains(key, pColumnsVals.getIdColumnsNames())) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(", ");
        }
        result.append(key.toUpperCase() + "=" + pColumnsVals.evalSqlValue(key));
      }
    }
    for (String key : pColumnsVals.getLongsMap().keySet()) {
      if (!isArrContains(key, pColumnsVals.getIdColumnsNames())
        && !ISrvOrm.VERSIONOLD_NAME.equals(key)) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(", ");
        }
        result.append(key.toUpperCase() + "=" + pColumnsVals.evalSqlValue(key));
      }
    }
    for (String key : pColumnsVals.getFloatsMap().keySet()) {
      if (isFirstField) {
        isFirstField = false;
      } else {
        result.append(", ");
      }
      result.append(key.toUpperCase() + "=" + pColumnsVals.evalSqlValue(key));
    }
    for (String key : pColumnsVals.getDoublesMap().keySet()) {
      if (isFirstField) {
        isFirstField = false;
      } else {
        result.append(", ");
      }
      result.append(key.toUpperCase() + "=" + pColumnsVals.evalSqlValue(key));
    }
    for (String key : pColumnsVals.getStringsMap().keySet()) {
      if (!isArrContains(key, pColumnsVals.getIdColumnsNames())) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          result.append(", ");
        }
        result.append(key.toUpperCase() + "=" + pColumnsVals.evalSqlValue(key));
      }
    }
    if (pWhere != null) {
    result.append(" where " + pWhere);
    }
    result.append(";\n");
    return result.toString();
  }

  /**
   * <p>Check if value is in array.</p>
   * @param <T> type value
   * @param pVal value
   * @param pArr array
   * @return if contains
   **/
  protected final <T> boolean isArrContains(final T pVal,
    final T[] pArr) {
    for (T val : pArr) {
      if (val.equals(pVal)) {
        return true;
      }
    }
    return false;
  }
}
