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

import org.beigesoft.model.IHasId;
import org.beigesoft.model.AEditable;
import org.beigesoft.persistable.UserRoleTomcat;

/**
 * <pre>
 * Test model User's Role for Tomcat priority.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class UserRoleTomcatPriority extends AEditable
  implements IHasId<UserRoleTomcat> {

  /**
   * <p>Composite primary and foreign ID.</p>
   **/
  private UserRoleTomcat userRoleTomcat;

  /**
   * <p>Priority.</p>
   **/
  private Integer priority;

  /**
   * <p>Getter for itsId.</p>
   * @return UserRoleTomcat
   **/
  @Override
  public final UserRoleTomcat getItsId() {
    return this.userRoleTomcat;
  }

  /**
   * <p>Setter for itsId.</p>
   * @param pItsId reference/value
   **/
  @Override
  public final void setItsId(final UserRoleTomcat pItsId) {
    this.userRoleTomcat = pItsId;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for userRoleTomcat.</p>
   * @return UserRoleTomcat
   **/
  public final UserRoleTomcat getUserRoleTomcat() {
    return this.userRoleTomcat;
  }

  /**
   * <p>Setter for userRoleTomcat.</p>
   * @param pUserRoleTomcat reference
   **/
  public final void setUserRoleTomcat(final UserRoleTomcat pUserRoleTomcat) {
    this.userRoleTomcat = pUserRoleTomcat;
  }

  /**
   * <p>Getter for priority.</p>
   * @return Integer
   **/
  public final Integer getPriority() {
    return this.priority;
  }

  /**
   * <p>Setter for priority.</p>
   * @param pPriority reference
   **/
  public final void setPriority(final Integer pPriority) {
    this.priority = pPriority;
  }
}
