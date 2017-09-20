package org.beigesoft.test.persistable;

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
