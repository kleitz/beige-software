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

import org.beigesoft.model.AEditable;
import org.beigesoft.model.IHasId;

/**
 * <pre>
 * Model UserRole for Jetty standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public class UserRoleJetty extends AEditable
    implements IHasId<IdUserRoleJetty> {

  /**
   * <p>Complex ID.</p>
   **/
  private IdUserRoleJetty itsId;

  /**
   * <p>User.</p>
   **/
  private UserJetty itsUser;

  /**
   * <p>User's role.</p>
   **/
  private RoleJetty itsRole;

  /**
   * <p>Geter for id.</p>
   * @return IdUserRoleJetty
   **/
  @Override
  public final IdUserRoleJetty getItsId() {
    return this.itsId;
  }

  /**
   * <p>Setter for id.</p>
   * @param pId reference
   **/
  @Override
  public final void setItsId(final IdUserRoleJetty pId) {
    this.itsId = pId;
  }

  /**
   * <p>Setter for role.</p>
   * @param pRole reference
   **/
  public final void setItsRole(final RoleJetty pRole) {
    this.itsRole = pRole;
    if (this.itsId == null) {
      this.itsId = new IdUserRoleJetty();
    }
    this.itsId.setIdRole(pRole.getItsId());
  }

  /**
   * <p>Setter for user.</p>
   * @param pUser reference
   **/
  public final void setItsUser(final UserJetty pUser) {
    this.itsUser = pUser;
    if (this.itsId == null) {
      this.itsId = new IdUserRoleJetty();
    }
    this.itsId.setIdUser(pUser.getItsId());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for user.</p>
   * @return UserJetty
   **/
  public final UserJetty getItsUser() {
    return this.itsUser;
  }

  /**
   * <p>Geter for role.</p>
   * @return RoleJettyM
   **/
  public final RoleJetty getItsRole() {
    return this.itsRole;
  }
}
