package org.beigesoft.test.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.model.IOwned;
import org.beigesoft.persistable.AHasIdLong;
import org.beigesoft.persistable.UserRoleTomcat;

/**
 * <pre>
 * Test model User's Role for Tomcat line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class UserRoleTomcatLine extends AHasIdLong
  implements IOwned<UserRoleTomcat> {

  /**
   * <p>Complex foreign ID.</p>
   **/
  private UserRoleTomcat itsOwner;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Getter for itsOwner.</p>
   * @return UserRoleTomcat
   **/
  @Override
  public final UserRoleTomcat getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final UserRoleTomcat pItsOwner) {
    this.itsOwner = pItsOwner;
  }

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
