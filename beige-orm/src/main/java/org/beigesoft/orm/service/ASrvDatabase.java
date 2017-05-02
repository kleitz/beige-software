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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.persistable.DatabaseInfo;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.log.ILogger;

/**
 * <p>Base Database service that hold cross-platform
 * methods.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public abstract class ASrvDatabase<RS> implements ISrvDatabase<RS> {

  /**
   * <p>Database ID.</p>
   **/
  private int idDatabase = 0;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Evaluate single Integer result.</p>
   * @param pQuery Query
   * @param pColumnName Column Name
   * @return Integer result e.g 11231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalIntegerResult(
    final String pQuery, final String pColumnName) throws Exception {
    Integer result = null;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        result = recordSet.getInteger(pColumnName);
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate single Long result.</p>
   * @param pQuery Query
   * @param pColumnName Column Name
   * @return Long result e.g 11231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Long evalLongResult(
    final String pQuery, final String pColumnName) throws Exception {
    Long result = null;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        result = recordSet.getLong(pColumnName);
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate single Float result.</p>
   * @param pQuery Query
   * @param pColumnName Column Name
   * @return Float result e.g 1.1231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Float evalFloatResult(
    final String pQuery, final String pColumnName) throws Exception {
    Float result = null;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        result = recordSet.getFloat(pColumnName);
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate single Double result.</p>
   * @param pQuery Query
   * @param pColumnName Column Name
   * @return Double result e.g 1.1231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Double evalDoubleResult(
    final String pQuery, final String pColumnName) throws Exception {
    Double result = null;
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        result = recordSet.getDouble(pColumnName);
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate Double results.</p>
   * @param pQuery Query
   * @param pColumnNames Column Names
   * @return Double[] result e.g. [2.14, NULL, 111.456]
   * @throws Exception - an exception
   */
  @Override
  public final Double[] evalDoubleResults(
    final String pQuery, final String[] pColumnNames) throws Exception {
    Double[] result = new Double[pColumnNames.length];
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        for (int i = 0; i < pColumnNames.length; i++) {
          result[i] = recordSet.getDouble(pColumnNames[i]);
        }
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Geter for database ID.</p>
   * @return ID database
   **/
  @Override
  public final synchronized int getIdDatabase() {
    if (this.idDatabase == 0) {
      try {
        String query = "select count(*) as TOTALROWS from " + DatabaseInfo.class
          .getSimpleName().toUpperCase() + ";";
        Integer rowCount = evalIntegerResult(query, "TOTALROWS");
        if (rowCount != 1) {
          throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
            "database_info_config_error");
        }
        query = "select DATABASEID from " + DatabaseInfo.class
          .getSimpleName().toUpperCase() + ";";
        Integer databaseId = evalIntegerResult(query, "DATABASEID");
        if (databaseId == null) {
          throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
            "database_info_config_error");
        }
        this.idDatabase = databaseId;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return this.idDatabase;
  }

  /**
   * <p>Geter for database version.</p>
   * @return database version
   **/
  @Override
  public final int getVersionDatabase() {
    try {
      String query = "select count(*) as TOTALROWS from " + DatabaseInfo.class
        .getSimpleName().toUpperCase() + ";";
      Integer rowCount = evalIntegerResult(query, "TOTALROWS");
      if (rowCount != 1) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "database_info_config_error");
      }
      query = "select DATABASEVERSION from " + DatabaseInfo.class
        .getSimpleName().toUpperCase() + ";";
      Integer databaseVersion = evalIntegerResult(query, "DATABASEVERSION");
      if (databaseVersion == null) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "database_info_config_error");
      }
      return databaseVersion;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }
}
