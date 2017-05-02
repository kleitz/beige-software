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

import org.beigesoft.model.IOwned;

/**
 * <p>String properties to connect to email server
 * e.g. "mail.transport.protocol"-"smtp".</p>
 *
 * @author Yury Demidenko
 */
public class EmailStringProperty extends AHasIdLongVersion
  implements IOwned<EmailConnect> {

  /**
   * <p>Email connection.</p>
   **/
  private EmailConnect itsOwner;

  /**
   * <p>Property name.</p>
   **/
  private String propertyName;

  /**
   * <p>Property value.</p>
   **/
  private String propretyValue;

  /**
   * <p>Getter for itsOwner.</p>
   * @return EmailConnect
   **/
  @Override
  public final EmailConnect getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final EmailConnect pItsOwner) {
    this.itsOwner = pItsOwner;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for propertyName.</p>
   * @return String
   **/
  public final String getPropertyName() {
    return this.propertyName;
  }

  /**
   * <p>Setter for propertyName.</p>
   * @param pPropertyName reference
   **/
  public final void setPropertyName(final String pPropertyName) {
    this.propertyName = pPropertyName;
  }

  /**
   * <p>Getter for propretyValue.</p>
   * @return String
   **/
  public final String getPropretyValue() {
    return this.propretyValue;
  }

  /**
   * <p>Setter for propretyValue.</p>
   * @param pPropretyValue reference
   **/
  public final void setPropretyValue(final String pPropretyValue) {
    this.propretyValue = pPropretyValue;
  }
}
