package org.beigesoft.persistable;

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
 * <p>Every database must has ID for replication purposes
 * and version for upgrade purpose.</p>
 *
 * @author Yury Demidenko
 */
public class DatabaseInfo {

  /**
   * <p>ID of type Integer.</p>
   **/
  private Integer databaseId;

  /**
   * <p>Version of type Integer.</p>
   **/
  private Integer databaseVersion;

  /**
   * <p>Description.</p>
   **/
  private String description;

  //Simple getters and setters:
  /**
   * <p>Getter for databaseId.</p>
   * @return Integer
   **/
  public final Integer getDatabaseId() {
    return this.databaseId;
  }

  /**
   * <p>Setter for databaseId.</p>
   * @param pDatabaseId reference
   **/
  public final void setDatabaseId(final Integer pDatabaseId) {
    this.databaseId = pDatabaseId;
  }

  /**
   * <p>Getter for databaseVersion.</p>
   * @return Integer
   **/
  public final Integer getDatabaseVersion() {
    return this.databaseVersion;
  }

  /**
   * <p>Setter for databaseVersion.</p>
   * @param pDatabaseVersion reference
   **/
  public final void setDatabaseVersion(final Integer pDatabaseVersion) {
    this.databaseVersion = pDatabaseVersion;
  }

  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }
}
