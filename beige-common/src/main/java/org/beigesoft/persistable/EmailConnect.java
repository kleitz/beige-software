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

import java.util.List;

/**
 * <p>Data to connect to email server.</p>
 *
 * @author Yury Demidenko
 */
public class EmailConnect extends AHasNameIdLongVersion {

  /**
   * <p>User email.</p>
   **/
  private String userEmail;

  /**
   * <p>User password.</p>
   **/
  private String userPassword;

  /**
   * <p>String properties e.g. "mail.transport.protocol"-"smtp".</p>
   **/
  private List<EmailStringProperty> stringProperties;

  /**
   * <p>Integer properties e.g. "mail.smtp.port"-465.</p>
   **/
  private List<EmailIntegerProperty> integerProperties;

  //Simple getters and setters:
  /**
   * <p>Getter for userEmail.</p>
   * @return String
   **/
  public final String getUserEmail() {
    return this.userEmail;
  }

  /**
   * <p>Setter for userEmail.</p>
   * @param pUserEmail reference
   **/
  public final void setUserEmail(final String pUserEmail) {
    this.userEmail = pUserEmail;
  }

  /**
   * <p>Getter for userPassword.</p>
   * @return String
   **/
  public final String getUserPassword() {
    return this.userPassword;
  }

  /**
   * <p>Setter for userPassword.</p>
   * @param pUserPassword reference
   **/
  public final void setUserPassword(final String pUserPassword) {
    this.userPassword = pUserPassword;
  }

  /**
   * <p>Getter for stringProperties.</p>
   * @return List<EmailIntegerProperty>
   **/
  public final List<EmailStringProperty> getStringProperties() {
    return this.stringProperties;
  }

  /**
   * <p>Setter for stringProperties.</p>
   * @param pStringProperties reference
   **/
  public final void setStringProperties(
    final List<EmailStringProperty> pStringProperties) {
    this.stringProperties = pStringProperties;
  }

  /**
   * <p>Getter for integerProperties.</p>
   * @return List<EmailIntegerProperty>
   **/
  public final List<EmailIntegerProperty> getIntegerProperties() {
    return this.integerProperties;
  }

  /**
   * <p>Setter for integerProperties.</p>
   * @param pIntegerProperties reference
   **/
  public final void setIntegerProperties(
    final List<EmailIntegerProperty> pIntegerProperties) {
    this.integerProperties = pIntegerProperties;
  }
}
