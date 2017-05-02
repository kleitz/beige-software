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
 * Composite ID for UserRole for Tomcat standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class IdUserRoleTomcat {

  /**
   * <p>User.</p>
   **/
  private UserTomcat itsUser;

  /**
   * <p>User's role.</p>
   **/
  private String itsRole;

  /**
   * <p>Default constructor.</p>
   **/
  public IdUserRoleTomcat() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pItsUser user
   * @param pItsRole role
   **/
  public IdUserRoleTomcat(final UserTomcat pItsUser, final String pItsRole) {
    this.itsUser = pItsUser;
    this.itsRole = pItsRole;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsUser.</p>
   * @return UserTomcat
   **/
  public final UserTomcat getItsUser() {
    return this.itsUser;
  }

  /**
   * <p>Setter for itsUser.</p>
   * @param pItsUser reference
   **/
  public final void setItsUser(final UserTomcat pItsUser) {
    this.itsUser = pItsUser;
  }

  /**
   * <p>Getter for itsRole.</p>
   * @return String
   **/
  public final String getItsRole() {
    return this.itsRole;
  }

  /**
   * <p>Setter for itsRole.</p>
   * @param pItsRole reference
   **/
  public final void setItsRole(final String pItsRole) {
    this.itsRole = pItsRole;
  }
}
