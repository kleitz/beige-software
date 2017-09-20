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
