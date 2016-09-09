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
 * Model User for Jetty standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class UserJetty extends APersistableBaseHasName {

  /**
   * <p>User's password.</p>
   **/
  private String itsPassword;

  /**
   * <p>Geter for itsPassword.</p>
   * @return String
   **/
  public final String getItsPassword() {
    return this.itsPassword;
  }

  /**
   * <p>Setter for itsPassword.</p>
   * @param pItsPassword reference/value
   **/
  public final void setItsPassword(final String pItsPassword) {
    this.itsPassword = pItsPassword;
  }
}
