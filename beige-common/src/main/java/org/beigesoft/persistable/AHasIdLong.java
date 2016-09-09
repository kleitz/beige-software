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
