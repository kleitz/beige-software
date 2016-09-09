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
 * <p>Base ORM properties.
 * </p>
 *
 * @author Yury Demidenko
 */
public class PropertiesBase {

  /**
   * <p>Key for SQL driver class name.
   * It doesn't used in Android applications.</p>
   **/
  public static final String KEY_JDBC_DRIVER_CLASS = "jdbcDriverClass";

  /**
   * <p>SQL driver class name, e.g. org.sqlite.JDBC.
   * It doesn't used in Android applications.</p>
   **/
  private String jdbcDriverClass;

  /**
   * <p>Key for Datasource class name.</p>
   **/
  public static final String KEY_DATASOURCE_CLASS = "dataSourceClassName";

  /**
   * <p>Datasource class name.</p>
   **/
  private String dataSourceClassName;

  /**
   * <p>Key for Database name.</p>
   **/
  public static final String KEY_DATABASE_NAME = "databaseName";

  /**
   * <p>Database name.</p>
   **/
  private String databaseName;

  /**
   * <p>Key for database url
   * e.g. jdbc:sqlite:beigeaccounting.
   * It used last name(after last :)
   * for Android database name i.e. beigeaccounting in that example.</p>
   **/
  public static final String KEY_DATABASE_URL = "databaseUrl";

  /**
   * <p>database url of the form jdbc:subprotocol:subname.
   * e.g. jdbc:sqlite:beigeaccounting.sqlite.</p>
   **/
  private String databaseUrl;

  /**
   * <p>Key for DB user name.</p>
   **/
  public static final String KEY_USER_NAME = "userName";

  /**
   * <p>DB user name.</p>
   **/
  private String userName;

  /**
   * <p>Key for DB user password.</p>
   **/
  public static final String KEY_USER_PASSWORD = "userPassword";

  /**
   * <p>DB user password.</p>
   **/
  private String userPassword;

  /**
   * <p>ORM properties directory (where also SQL files).</p>
   **/
  private String directory;

  //customized getters and setters:
  //Simple getters and setters:
  /**
   * <p>Geter for jdbcDriverClass.</p>
   * @return String
   **/
  public final String getJdbcDriverClass() {
    return this.jdbcDriverClass;
  }

  /**
   * <p>Setter for jdbcDriverClass.</p>
   * @param pJdbcDriverClass reference/value
   **/
  public final void setJdbcDriverClass(final String pJdbcDriverClass) {
    this.jdbcDriverClass = pJdbcDriverClass;
  }

  /**
   * <p>Geter for databaseUrl.</p>
   * @return String
   **/
  public final String getDatabaseUrl() {
    return this.databaseUrl;
  }

  /**
   * <p>Setter for databaseUrl.</p>
   * @param pDatabaseUrl reference/value
   **/
  public final void setDatabaseUrl(final String pDatabaseUrl) {
    this.databaseUrl = pDatabaseUrl;
  }
  /**
   * <p>Geter for dataSourceClassName.</p>
   * @return String
   **/
  public final String getDataSourceClassName() {
    return this.dataSourceClassName;
  }

  /**
   * <p>Setter for dataSourceClassName.</p>
   * @param pDataSourceClassName reference/value
   **/
  public final void setDataSourceClassName(final String pDataSourceClassName) {
    this.dataSourceClassName = pDataSourceClassName;
  }

  /**
   * <p>Geter for databaseName.</p>
   * @return String
   **/
  public final String getDatabaseName() {
    return this.databaseName;
  }

  /**
   * <p>Setter for databaseName.</p>
   * @param pDatabaseName reference/value
   **/
  public final void setDatabaseName(final String pDatabaseName) {
    this.databaseName = pDatabaseName;
  }

  /**
   * <p>Geter for userName.</p>
   * @return String
   **/
  public final String getUserName() {
    return this.userName;
  }

  /**
   * <p>Setter for userName.</p>
   * @param pUserName reference/value
   **/
  public final void setUserName(final String pUserName) {
    this.userName = pUserName;
  }

  /**
   * <p>Geter for userPassword.</p>
   * @return String
   **/
  public final String getUserPassword() {
    return this.userPassword;
  }

  /**
   * <p>Setter for userPassword.</p>
   * @param pUserPassword reference/value
   **/
  public final void setUserPassword(final String pUserPassword) {
    this.userPassword = pUserPassword;
  }

  /**
   * <p>Geter for directory.</p>
   * @return String
   **/
  public final String getDirectory() {
    return this.directory;
  }

  /**
   * <p>Setter for directory.</p>
   * @param pDirectory reference
   **/
  public final void setDirectory(final String pDirectory) {
    this.directory = pDirectory;
  }
}
