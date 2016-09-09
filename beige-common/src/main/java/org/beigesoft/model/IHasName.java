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
