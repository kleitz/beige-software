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
 * It can create and change current database (file).
 * For Android there is extended manager that can upload/download database
 * from Google Drive. In standard OS (*NIX, MS WINDOWS) user should make
 * database copy by himself and it is recommended to use Google Drive
 * as private and safety storage.</p>
 *
 * @author Yury Demidenko
 */
public interface IMngDatabase {

  /**
   * <p>
   * List databases.
   * </p>
   * @return List<String> list of database files.
   * @throws Exception - an exception
   **/
  List<String> retrieveList() throws Exception;

  /**
   * <p>
   * Retrieve current DB name.
   * </p>
   * @return String DB name.
   * @throws Exception - an exception
   **/
  String retrieveCurrentDbName() throws Exception;

  /**
   * <p>Create new database.</p>
   * @param pDbName database name without extension
   * @throws Exception - an exception
   **/
  void createDatabase(String pDbName) throws Exception;

  /**
   * <p>Change database.</p>
   * @param pDbName database name without extension
   * @throws Exception - an exception
   **/
  void changeDatabase(String pDbName) throws Exception;
}
