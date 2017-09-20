package org.beigesoft.persistable;

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
