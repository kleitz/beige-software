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
 * <pre>
 * Complex ID for UserRole for Tomcat standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class IdUserRoleTomcat {

  /**
   * <p>User ID.</p>
   **/
  private String idUser;

  /**
   * <p>Role ID.</p>
   **/
  private String idRole;

  /**
   * <p>Default constructor.</p>
   **/
  public IdUserRoleTomcat() {
  }

  /**
   * <p>Usable constructor.</p>
   * @param pIdUser user ID
   * @param pIdRole role ID
   **/
  public IdUserRoleTomcat(final String pIdUser, final String pIdRole) {
    this.idUser = pIdUser;
    this.idRole = pIdRole;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for idUser;.</p>
   * @return String
   **/
  public final String getIdUser() {
    return this.idUser;
  }

  /**
   * <p>Setter for idUser.</p>
   * @param pIdUser reference
   **/
  public final void setIdUser(final String pIdUser) {
    this.idUser = pIdUser;
  }

  /**
   * <p>Geter for idRole.</p>
   * @return String
   **/
  public final String getIdRole() {
    return this.idRole;
  }

  /**
   * <p>Setter for idRole.</p>
   * @param pIdRole role ID
   **/
  public final void setIdRole(final String pIdRole) {
    this.idRole = pIdRole;
  }
}
