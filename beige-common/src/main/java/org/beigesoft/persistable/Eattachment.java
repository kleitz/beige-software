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
 * <p>Persistable model of attachment in email.</p>
 *
 * @author Yury Demidenko
 */
public class Eattachment extends AHasNameIdLongVersion
  implements IOwned<EmailMsg> {

  /**
   * <p>Email message.</p>
   **/
  private EmailMsg itsOwner;

  /**
   * <p>Path.</p>
   **/
  private String itsPath;

  /**
   * <p>Getter for itsOwner.</p>
   * @return EmailMsg
   **/
  @Override
  public final EmailMsg getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final EmailMsg pItsOwner) {
    this.itsOwner = pItsOwner;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsPath.</p>
   * @return String
   **/
  public final String getItsPath() {
    return this.itsPath;
  }

  /**
   * <p>Setter for itsPath.</p>
   * @param pItsPath reference
   **/
  public final void setItsPath(final String pItsPath) {
    this.itsPath = pItsPath;
  }
}
