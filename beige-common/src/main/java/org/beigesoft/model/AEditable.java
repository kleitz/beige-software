package org.beigesoft.model;

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
  @Override
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
