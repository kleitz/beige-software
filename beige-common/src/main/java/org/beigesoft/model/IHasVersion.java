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
 * <p>Abstraction an model that has Version to check dirty.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IHasVersion {

  /**
   * <p>Geter for itsVersion to check dirty.</p>
   * @return Long
   **/
  Long getItsVersion();

  /**
   * <p>Setter for itsVersion to check dirty.</p>
   * @param pItsVersion reference
   **/
  void setItsVersion(final Long pItsVersion);
}
