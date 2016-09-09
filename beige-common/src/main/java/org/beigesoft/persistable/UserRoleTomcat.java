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
 * Model User's Role for Tomcat standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class UserRoleTomcat extends AEditable
  implements IHasId<IdUserRoleTomcat> {

  /**
   * <p>Complex ID.</p>
   **/
  private IdUserRoleTomcat itsId;

  /**
   * <p>User.</p>
   **/
  private UserTomcat itsUser;

  /**
   * <p>User's role.</p>
   **/
  private String itsRole;

  /**
   * <p>Geter for itsId.</p>
   * @return IdUserRoleTomcat
   **/
  @Override
  public final IdUserRoleTomcat getItsId() {
    return this.itsId;
  }

  /**
   * <p>Setter for itsId.</p>
   * @param pItsId reference/value
   **/
  @Override
  public final void setItsId(final IdUserRoleTomcat pItsId) {
    this.itsId = pItsId;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for itsUser.</p>
   * @return UserTomcat
   **/
  public final UserTomcat getItsUser() {
    return this.itsUser;
  }

  /**
   * <p>Setter for itsUser.</p>
   * @param pItsUser reference/value
   **/
  public final void setItsUser(final UserTomcat pItsUser) {
    this.itsUser = pItsUser;
  }

  /**
   * <p>Geter for itsRole.</p>
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
