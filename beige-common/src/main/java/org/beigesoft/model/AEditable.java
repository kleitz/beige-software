package org.beigesoft.model;

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
 * <p>Abstraction an editable model that has status "is new".
 * Also it's used for persistable model for "is need insert or update?".
 * </p>
 *
 * @author Yury Demidenko
 */
public abstract class AEditable implements IEditable {

  /**
   * <p>Flag "is new".</p>
   **/
  private Boolean isNew = false;

  /**
   * <p>Geter for isNew.</p>
   * @return boolean
   **/
  public final Boolean getIsNew() {
    return this.isNew;
  }

  /**
   * <p>Setter for isNew.</p>
   * @param pIsNew value
   **/
  @Override
  public final void setIsNew(final Boolean pIsNew) {
    this.isNew = pIsNew;
  }
}
