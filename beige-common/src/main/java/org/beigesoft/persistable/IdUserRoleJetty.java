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
 * Composite ID for UserRole for Jetty standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class IdUserRoleJetty {

  /**
   * <p>User.</p>
   **/
  private UserJetty itsUser;

  /**
   * <p>User's role.</p>
   **/
  private RoleJetty itsRole;

  /**
   * <p>Default constructor.</p>
   **/
  public IdUserRoleJetty() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pItsUser user ID
   * @param pItsRole role ID
   **/
  public IdUserRoleJetty(final UserJetty pItsUser, final RoleJetty pItsRole) {
    this.itsUser = pItsUser;
    this.itsRole = pItsRole;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsUser.</p>
   * @return UserJetty
   **/
  public final UserJetty getItsUser() {
    return this.itsUser;
  }

  /**
   * <p>Setter for itsUser.</p>
   * @param pItsUser reference
   **/
  public final void setItsUser(final UserJetty pItsUser) {
    this.itsUser = pItsUser;
  }

  /**
   * <p>Getter for itsRole.</p>
   * @return RoleJetty
   **/
  public final RoleJetty getItsRole() {
    return this.itsRole;
  }

  /**
   * <p>Setter for itsRole.</p>
   * @param pItsRole reference
   **/
  public final void setItsRole(final RoleJetty pItsRole) {
    this.itsRole = pItsRole;
  }
}
