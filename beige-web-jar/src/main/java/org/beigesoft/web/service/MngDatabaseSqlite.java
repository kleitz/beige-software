package org.beigesoft.web.service;

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

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.beigesoft.log.ILogger;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.web.factory.FactoryAppBeansSqlite;

/**
 * <p>Database manager for SQLite.
 * It can create and change current database (file).
 * In standard OS (*NIX, MS WINDOWS) user should make
 * database copy by himself and it is recommended to use Google Drive
 * as private and safety storage.</p>
 *
 * @author Yury Demidenko
 */
public class MngDatabaseSqlite implements IMngDatabase {

  /**
   * <p>Databases folder.</p>
   **/
  private String databaseDir = System.getProperty("user.dir");

  /**
   * <p>Database service.</p>
   **/
  private FactoryAppBeansSqlite factoryAppBeansSqlite;

  /**
   * <p>
   * List databases.
   * </p>
   * @return List<String> list of database files.
   * @throws Exception - an exception
   **/
  @Override
  public final List<String> retrieveList() throws Exception {
    List<String> result = new ArrayList<String>();
    File dbDir = new File(this.databaseDir);
    if (dbDir.exists() && dbDir.isDirectory()) {
      String[] files = dbDir.list();
      if (files != null) {
        for (String flNm : files) {
          if (flNm.endsWith(".sqlite")) {
            result.add(flNm.replace(".sqlite", ""));
          }
        }
      }
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "DB directory not found: " + this.databaseDir);
    }
    return result;
  }

  /**
   * <p>
   * Retrieve current DB name.
   * </p>
   * @return String DB name.
   * @throws Exception - an exception
   **/
  @Override
  public final String retrieveCurrentDbName() throws Exception {
    return this.factoryAppBeansSqlite.getDatabaseName();
  }

  /**
   * <p>Create new database.</p>
   * @param pDbName database name without extension
   * @param pDbId database ID
   * @throws Exception - an exception
   **/
  @Override
  public final void createDatabase(final String pDbName,
    final int pDbId) throws Exception {
    String dbUrl = "jdbc:sqlite:" + this.databaseDir + File.separator
      + pDbName + ".sqlite";
    if (!this.factoryAppBeansSqlite.getDatabaseName().equals(dbUrl)) {
      ILogger logger = (ILogger) this.factoryAppBeansSqlite
        .lazyGet(this.factoryAppBeansSqlite.getLoggerName());
      logger.info(null, MngDatabaseSqlite.class, "Database create name/id "
        + pDbName + "/" + pDbId);
      this.factoryAppBeansSqlite.setDatabaseName(dbUrl);
      this.factoryAppBeansSqlite.setNewDatabaseId(pDbId);
      this.factoryAppBeansSqlite.handleDatabaseChanged();
    }
  }

  /**
   * <p>Change database.</p>
   * @param pDbName database name without extension
   * @throws Exception - an exception
   **/
  @Override
  public final void changeDatabase(final String pDbName) throws Exception {
    String dbUrl = "jdbc:sqlite:" + this.databaseDir + File.separator
      + pDbName + ".sqlite";
    if (!this.factoryAppBeansSqlite.getDatabaseName().equals(dbUrl)) {
      ILogger logger = (ILogger) this.factoryAppBeansSqlite
        .lazyGet(this.factoryAppBeansSqlite.getLoggerName());
      logger.info(null, MngDatabaseSqlite.class,
        "Database changed to " + pDbName);
      this.factoryAppBeansSqlite.setDatabaseName(dbUrl);
      this.factoryAppBeansSqlite.handleDatabaseChanged();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for databaseDir.</p>
   * @return String
   **/
  public final String getDatabaseDir() {
    return this.databaseDir;
  }

  /**
   * <p>Setter for databaseDir.</p>
   * @param pDatabaseDir reference
   **/
  public final void setDatabaseDir(final String pDatabaseDir) {
    this.databaseDir = pDatabaseDir;
  }

  /**
   * <p>Getter for factoryAppBeansSqlite.</p>
   * @return FactoryAppBeansSqlite
   **/
  public final FactoryAppBeansSqlite getFactoryAppBeansSqlite() {
    return this.factoryAppBeansSqlite;
  }

  /**
   * <p>Setter for factoryAppBeansSqlite.</p>
   * @param pFactoryAppBeansSqlite reference
   **/
  public final void setFactoryAppBeansSqlite(
    final FactoryAppBeansSqlite pFactoryAppBeansSqlite) {
    this.factoryAppBeansSqlite = pFactoryAppBeansSqlite;
  }
}
