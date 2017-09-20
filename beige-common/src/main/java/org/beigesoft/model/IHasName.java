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
 * <p>Abstraction an model that has name.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IHasName {

  /**
   * <p>Usually it's simple setter for model name.
   * </p>
   * @param pItsName model name
   **/
  void setItsName(final String pItsName);

  /**
   * <p>Usually it's simple getter that return model name.</p>
   * @return String model name
   **/
  String getItsName();
}
