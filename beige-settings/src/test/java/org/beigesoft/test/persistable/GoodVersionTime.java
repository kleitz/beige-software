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

import java.math.BigDecimal;

import org.beigesoft.persistable.APersistableBaseHasName;

/**
 * <pre>
 * Model for test ORM.
 * Optimistic locking version changed time.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public class GoodVersionTime extends APersistableBaseHasName {

  /**
   * <p>Version algorithm changed time.</p>
   **/
  private Long itsVersion;

  //Simple getters and setters:

  /**
   * <p>Geter for itsVersion.</p>
   * @return Long
   **/
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference
   **/
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
  }
}
