package org.beigesoft.webstore.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.persistable.AHasNameIdLongVersion;

/**
 * <pre>
 * Model of Chooseable Specifics of Items, it's picked from list.
 * ID auto-generated.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class ChooseableSpecifics extends AHasNameIdLongVersion {

  /**
   * <p>Its type, e.g. "Operation systems" for "Linux".</p>
   **/
  private ChooseableSpecificsType itsType;

  //Simple getters and setters:
  /**
   * <p>Getter for itsType.</p>
   * @return ChooseableSpecificsType
   **/
  public final ChooseableSpecificsType getItsType() {
    return this.itsType;
  }

  /**
   * <p>Setter for itsType.</p>
   * @param pItsType reference
   **/
  public final void setItsType(final ChooseableSpecificsType pItsType) {
    this.itsType = pItsType;
  }
}
