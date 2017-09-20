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

import org.beigesoft.model.IHasIdStringVersion;

/**
 * <p>Base abstraction persistable model with ID String type.</p>
 *
 * @author Yury Demidenko
 */
public abstract class AHasIdStringVersion extends AHasIdString
    implements IHasIdStringVersion {


  /**
   * <p>Version to check dirty or replication.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Geter for itsVersion.</p>
   * @return Long
   **/
  @Override
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference
   **/
  @Override
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
  }
}
