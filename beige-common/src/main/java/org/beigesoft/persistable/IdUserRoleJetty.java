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
 * Complex ID for UserRole for Jetty standard JDBC autentification.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class IdUserRoleJetty {

  /**
   * <p>User ID.</p>
   **/
  private Long idUser;

  /**
   * <p>Role ID.</p>
   **/
  private Long idRole;

  /**
   * <p>Default constructor.</p>
   **/
  public IdUserRoleJetty() {
  }

  /**
   * <p>Usable constructor.</p>
   * @param pIdUser user ID
   * @param pIdRole role ID
   **/
  public IdUserRoleJetty(final Long pIdUser, final Long pIdRole) {
    this.idUser = pIdUser;
    this.idRole = pIdRole;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for idUser;.</p>
   * @return Long
   **/
  public final Long getIdUser() {
    return this.idUser;
  }

  /**
   * <p>Setter for idUser.</p>
   * @param pIdUser reference
   **/
  public final void setIdUser(final Long pIdUser) {
    this.idUser = pIdUser;
  }

  /**
   * <p>Geter for idRole.</p>
   * @return Long
   **/
  public final Long getIdRole() {
    return this.idRole;
  }

  /**
   * <p>Setter for idRole.</p>
   * @param pIdRole role ID
   **/
  public final void setIdRole(final Long pIdRole) {
    this.idRole = pIdRole;
  }
}
