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

import org.beigesoft.model.IOwned;

/**
 * <p>Integer properties to connect to email server
 * e.g. "mail.smtp.port"-465.</p>
 *
 * @author Yury Demidenko
 */
public class EmailIntegerProperty extends AHasIdLongVersion
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
  private Integer propretyValue;

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
  public final Integer getPropretyValue() {
    return this.propretyValue;
  }

  /**
   * <p>Setter for propretyValue.</p>
   * @param pPropretyValue reference
   **/
  public final void setPropretyValue(final Integer pPropretyValue) {
    this.propretyValue = pPropretyValue;
  }
}
