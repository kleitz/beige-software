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
 * <p>Abstraction of generic handler of an event.</p>
 *
 * @author Yury Demidenko
 * @param <EV> type of handled event
 */
public interface IHandlerEvent<EV> {

  /**
   * <p>Handle an event.</p>
   * @param event to be handled
   * @return boolean if event handled (in case of use of events chain)
   **/
  boolean handleEvent(EV event);
}
