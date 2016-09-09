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
 * <p>Base abstraction persistable model with ID String type.</p>
 *
 * @author Yury Demidenko
 */
public abstract class AHasIdString extends AEditable
    implements IHasId<String> {

  /**
   * <p>Fast explicit, usable ID of type String.</p>
   **/
  private String itsId;

  /**
   * <p>Geter for id.</p>
   * @return String
   **/
  @Override
  public final String getItsId() {
    return this.itsId;
  }

  /**
   * <p>Setter for id.</p>
   * @param pId reference/value
   **/
  @Override
  public final void setItsId(final String pId) {
    this.itsId = pId;
  }
}
