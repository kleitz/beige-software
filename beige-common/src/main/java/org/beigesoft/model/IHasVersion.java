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
 * <p>Abstraction an persistable model that has Version to check dirty
 * and/or replication.
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
