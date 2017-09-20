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
 * <p>Base abstraction persistable model with ID Long type
 * usually non-auto-incremented.</p>
 *
 * @author Yury Demidenko
 */
public abstract class AHasIdLong extends AEditable
    implements IHasId<Long> {

  /**
   * <p>Fast explicit, usable ID of type Long.</p>
   **/
  private Long itsId;

  /**
   * <p>Geter for id.</p>
   * @return Long
   **/
  @Override
  public final Long getItsId() {
    return this.itsId;
  }

  /**
   * <p>Setter for id.</p>
   * @param pId reference/value
   **/
  @Override
  public final void setItsId(final Long pId) {
    this.itsId = pId;
  }
}
