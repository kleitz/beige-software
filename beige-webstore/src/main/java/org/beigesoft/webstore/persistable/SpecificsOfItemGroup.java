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
 * Model of Group of Specifics e.g. "Monitor" for its size, web-cam,
 * LED-type in a notebook.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SpecificsOfItemGroup extends AHasNameIdLongVersion {

  /**
   * <p>Description.</p>
   **/
  private String description;

  //Simple getters and setters:
  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }
}
