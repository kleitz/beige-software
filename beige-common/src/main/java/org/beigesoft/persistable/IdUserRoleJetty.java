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
