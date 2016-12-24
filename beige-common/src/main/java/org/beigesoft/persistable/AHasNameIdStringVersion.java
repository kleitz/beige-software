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

import org.beigesoft.model.IHasName;

/**
 * <pre>
 * Abstraction of model with name and
 * non-generated String Id.
 * </pre>
 *
 * @author Yury Demidenko
 */
public abstract class AHasNameIdStringVersion extends AHasIdStringVersion
  implements IHasName {

  /**
   * <p>Name.</p>
   **/
  private String itsName;

  /**
   * <p>Geter for itsName.</p>
   * @return String
   **/
  @Override
  public final String getItsName() {
    return this.itsName;
  }

  /**
   * <p>Setter for itsName.</p>
   * @param pItsName reference
   **/
  @Override
  public final void setItsName(final String pItsName) {
    this.itsName = pItsName;
  }
}
