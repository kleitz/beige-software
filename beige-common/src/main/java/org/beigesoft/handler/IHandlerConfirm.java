package org.beigesoft.handler;

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
 * <p>Abstraction of handler of a confirmation event.
 * E.g. oocured then user press "Yes" or "No"</p>
 *
 * @author Yury Demidenko
 */
public interface IHandlerConfirm {

  /**
   * <p>Handle an event.</p>
   * @param isConfirmed if confirmed
   **/
  void handleConfirm(boolean isConfirmed);
}
