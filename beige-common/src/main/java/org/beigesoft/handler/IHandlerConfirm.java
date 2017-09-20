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
