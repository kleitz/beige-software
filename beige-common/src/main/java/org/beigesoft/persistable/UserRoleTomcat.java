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
   * <p>Complex ID. Must be initialized cause reflection use.</p>
   **/
  private IdUserRoleTomcat itsId = new IdUserRoleTomcat();

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
    if (this.itsId == null) {
      this.itsUser = null;
      this.itsRole = null;
    } else {
      this.itsUser = this.itsId.getItsUser();
      this.itsRole = this.itsId.getItsRole();
    }
  }

  /**
   * <p>Setter for itsUser.</p>
   * @param pItsUser reference/value
   **/
  public final void setItsUser(final UserTomcat pItsUser) {
    this.itsUser = pItsUser;
    if (this.itsId == null) {
      this.itsId = new IdUserRoleTomcat();
    }
    this.itsId.setItsUser(this.itsUser);
  }

  /**
   * <p>Setter for itsRole.</p>
   * @param pItsRole reference
   **/
  public final void setItsRole(final String pItsRole) {
    this.itsRole = pItsRole;
    if (this.itsId == null) {
      this.itsId = new IdUserRoleTomcat();
    }
    this.itsId.setItsRole(this.itsRole);
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
   * <p>Geter for itsRole.</p>
   * @return String
   **/
  public final String getItsRole() {
    return this.itsRole;
  }
}
