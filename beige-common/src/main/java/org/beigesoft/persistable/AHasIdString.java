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
