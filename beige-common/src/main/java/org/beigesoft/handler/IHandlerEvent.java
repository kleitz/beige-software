package org.beigesoft.handler;

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
