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

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Complex model that contains of type-safe maps Key-Value.
 * It is to adapt Android ContentValues.</p>
 *
 * @author Yury Demidenko
 */
public class ColumnsValues {

  /**
   * <p>ID name (primary key).</p>
   **/
  private String idName;

  /**
   * <p>Map of Integers values.</p>
   **/
  private final Map<String, Integer> integersMap =
    new HashMap<String, Integer>();

  /**
   * <p>Map of Long values.</p>
   **/
  private final Map<String, Long> longsMap =
    new HashMap<String, Long>();

  /**
   * <p>Map of Float values.</p>
   **/
  private final Map<String, Float> floatsMap =
    new HashMap<String, Float>();

  /**
   * <p>Map of Double values.</p>
   **/
  private final Map<String, Double> doublesMap =
    new HashMap<String, Double>();

  /**
   * <p>Map of String values.</p>
   **/
  private final Map<String, String> stringsMap =
    new HashMap<String, String>();

  //Utils:
  @Override
  public final String toString() {
    StringBuffer sb = new StringBuffer();
    boolean isFirst = true;
    for (Map.Entry<String, Integer> entry : integersMap.entrySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      sb.append(entry.getKey() + "=" + entry.getValue());
    }
    for (Map.Entry<String, Long> entry : longsMap.entrySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      sb.append(entry.getKey() + "=" + entry.getValue());
    }
    for (Map.Entry<String, Float> entry : floatsMap.entrySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      sb.append(entry.getKey() + "=" + entry.getValue());
    }
    for (Map.Entry<String, Double> entry : doublesMap.entrySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      sb.append(entry.getKey() + "=" + entry.getValue());
    }
    for (Map.Entry<String, String> entry : stringsMap.entrySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      sb.append(entry.getKey() + "=" + entry.getValue());
    }
    return sb.toString();
  }

  /**
   * <p>Put column with Integer value.</p>
   * @param pName column name
   * @param pValue column value
   **/
  public final void put(final String pName, final Integer pValue) {
    integersMap.put(pName, pValue);
  }

  /**
   * <p>Put column with Long value.</p>
   * @param pName column name
   * @param pValue column value
   **/
  public final void put(final String pName, final Long pValue) {
    longsMap.put(pName, pValue);
  }

  /**
   * <p>Put column with Float value.</p>
   * @param pName column name
   * @param pValue column value
   **/
  public final void put(final String pName, final Float pValue) {
    floatsMap.put(pName, pValue);
  }

  /**
   * <p>Put column with Double value.</p>
   * @param pName column name
   * @param pValue column value
   **/
  public final void put(final String pName, final Double pValue) {
    doublesMap.put(pName, pValue);
  }

  /**
   * <p>Put column with String value.</p>
   * @param pName column name
   * @param pValue column value
   **/
  public final void put(final String pName, final String pValue) {
    stringsMap.put(pName, pValue);
  }

  /**
   * <p>Get column with Integer value.</p>
   * @param pName column name
   * @return Integer column value
   **/
  public final Integer getInteger(final String pName) {
    return integersMap.get(pName);
  }

  /**
   * <p>Get column with Long value.</p>
   * @param pName column name
   * @return Long column value
   **/
  public final Long getLong(final String pName) {
    return longsMap.get(pName);
  }

  /**
   * <p>Get column with Float value.</p>
   * @param pName column name
   * @return Float column value
   **/
  public final Float getFloat(final String pName) {
    return floatsMap.get(pName);
  }

  /**
   * <p>Get column with Double value.</p>
   * @param pName column name
   * @return Double column value
   **/
  public final Double getDouble(final String pName) {
    return doublesMap.get(pName);
  }

  /**
   * <p>Get column with String value.</p>
   * @param pName column name
   * @return String column value
   **/
  public final String getString(final String pName) {
    return stringsMap.get(pName);
  }

  /**
   * <p>Evaluate column value for SQL statement INSERT or UPDATE.</p>
   * @param pName column name
   * @return String column value
   * @throws ExceptionWithCode if column not found
   **/
  public final String evalSqlValue(
    final String pName) throws ExceptionWithCode {
    Object value = evalObjectValue(pName);
    if (value == null) {
      return "null";
    } else {
      return value.toString();
    }
  }

  /**
   * <p>Evaluate column string value for
   * SQL statement INSERT or UPDATE.
   * For String return '[value]' or NULL</p>
   * @param pName column name
   * @return Object column value
   * @throws ExceptionWithCode if column not found
   **/
  public final Object evalObjectValue(
    final String pName) throws ExceptionWithCode {
    for (String key : integersMap.keySet()) {
      if (key.equals(pName)) {
        return integersMap.get(pName);
      }
    }
    for (String key : longsMap.keySet()) {
      if (key.equals(pName)) {
        return longsMap.get(pName);
      }
    }
    for (String key : floatsMap.keySet()) {
      if (key.equals(pName)) {
        return floatsMap.get(pName);
      }
    }
    for (String key : doublesMap.keySet()) {
      if (key.equals(pName)) {
        return doublesMap.get(pName);
      }
    }
    for (String key : stringsMap.keySet()) {
      if (key.equals(pName)) {
        String value = (String) stringsMap.get(pName);
        if (value == null) {
          return null;
        } else {
          return "'" + value + "'";
        }
      }
    }
    String msg = "Value not found for column name " + pName;
    throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG, msg);
  }

  /**
   * <p>Check whether column is contained here.</p>
   * @param pName column name
   * @return if contains
    **/
  public final boolean ifContains(final String pName) {
    for (String key : integersMap.keySet()) {
      if (key.equals(pName)) {
        return true;
      }
    }
    for (String key : longsMap.keySet()) {
      if (key.equals(pName)) {
        return true;
      }
    }
    for (String key : floatsMap.keySet()) {
      if (key.equals(pName)) {
        return true;
      }
    }
    for (String key : doublesMap.keySet()) {
      if (key.equals(pName)) {
        return true;
      }
    }
    for (String key : stringsMap.keySet()) {
      if (key.equals(pName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Remove column.</p>
   * @param pName column name
   **/
  public final void remove(final String pName) {
    for (String key : integersMap.keySet()) {
      if (key.equals(pName)) {
        integersMap.remove(pName);
        return;
      }
    }
    for (String key : longsMap.keySet()) {
      if (key.equals(pName)) {
        longsMap.remove(pName);
        return;
      }
    }
    for (String key : floatsMap.keySet()) {
      if (key.equals(pName)) {
        floatsMap.remove(pName);
        return;
      }
    }
    for (String key : doublesMap.keySet()) {
      if (key.equals(pName)) {
        doublesMap.remove(pName);
        return;
      }
    }
    for (String key : stringsMap.keySet()) {
      if (key.equals(pName)) {
        stringsMap.remove(pName);
        return;
      }
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for idName.</p>
   * @return String
   **/
  public final String getIdName() {
    return this.idName;
  }

  /**
   * <p>Setter for idName.</p>
   * @param pIdName reference
   **/
  public final void setIdName(final String pIdName) {
    this.idName = pIdName;
  }

  /**
   * <p>Geter for integersMap.</p>
   * @return Map<String, Integer>
   **/
  public final Map<String, Integer> getIntegersMap() {
    return this.integersMap;
  }

  /**
   * <p>Geter for longsMap.</p>
   * @return Map<String, Long>
   **/
  public final Map<String, Long> getLongsMap() {
    return this.longsMap;
  }

  /**
   * <p>Geter for floatsMap.</p>
   * @return Map<String, Float>
   **/
  public final Map<String, Float> getFloatsMap() {
    return this.floatsMap;
  }

  /**
   * <p>Geter for doublesMap.</p>
   * @return Map<String, Double>
   **/
  public final Map<String, Double> getDoublesMap() {
    return this.doublesMap;
  }

  /**
   * <p>Geter for stringsMap.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getStringsMap() {
    return this.stringsMap;
  }
}
