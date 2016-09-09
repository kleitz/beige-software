package org.beigesoft.web.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;

/**
 * <p>Database manager for file-based RDBMS (SQLite/H2).
 * It can create and change current database (file),
 * backup DB to global storage and restore from it,
 * also can delete database.</p>
 *
 * @author Yury Demidenko
 */
public interface IMngDatabaseExt extends IMngDatabase {

  /**
   * <p>
   * List backup databases.
   * </p>
   * @return List<String> list of backup database files.
   * @throws Exception - an exception
   **/
  List<String> retrieveBackupList() throws Exception;

  /**
   * <p>Delete database.</p>
   * @param pDbName database name without extension
   * @throws Exception - an exception
   **/
  void deleteDatabase(String pDbName) throws Exception;

  /**
   * <p>Backup database.</p>
   * @param pDbName database name without extension
   * @throws Exception - an exception
   **/
  void backupDatabase(String pDbName) throws Exception;

  /**
   * <p>Restore database.</p>
   * @param pDbName database name without extension
   * @throws Exception - an exception
   **/
  void restoreDatabase(String pDbName) throws Exception;

  /**
   * <p>Getter for backupDir.</p>
   * @return String
   **/
  String getBackupDir();

  /**
   * <p>Setter for backupDir.</p>
   * @param pBackupDir reference
   **/
  void setBackupDir(String pBackupDir);
}
