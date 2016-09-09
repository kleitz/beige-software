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
 * Model User for Tomcat standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class UserTomcat extends AEditable implements IHasId<String> {

  /**
   * <p>User's name/id.
   * FIELD MUST BE SAME NAME itsUser AS IN USERROLETOMCAT!
   * And so in database.</p>
   **/
  private String itsUser;

  /**
   * <p>User's password.</p>
   **/
  private String itsPassword;

  /**
   * <p>Geter for itsId.</p>
   * @return IdUserRoleTomcat
   **/
  @Override
  public final String getItsId() {
    return this.itsUser;
  }

  /**
   * <p>Setter for itsId.</p>
   * @param pItsId reference/value
   **/
  @Override
  public final void setItsId(final String pItsId) {
    this.itsUser = pItsId;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for itsUser.</p>
   * @return String
   **/
  public final String getItsUser() {
    return this.itsUser;
  }

  /**
   * <p>Setter for itsUser.</p>
   * @param pItsUser reference/value
   **/
  public final void setItsUser(final String pItsUser) {
    this.itsUser = pItsUser;
  }

  /**
   * <p>Geter for itsPassword.</p>
   * @return String
   **/
  public final String getItsPassword() {
    return this.itsPassword;
  }

  /**
   * <p>Setter for itsPassword.</p>
   * @param pItsPassword reference
   **/
  public final void setItsPassword(final String pItsPassword) {
    this.itsPassword = pItsPassword;
  }
}
