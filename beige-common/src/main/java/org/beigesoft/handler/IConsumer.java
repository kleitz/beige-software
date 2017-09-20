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
 * <p>Abstraction of consumer of a bean,
 * that do something with it.
 * It's used usually in a chooser.
 * </p>
 *
 * @author Yury Demidenko
 * @param <M> type of consumed model
 */
public interface IConsumer<M> {

  /**
   * <p>Handle an event.</p>
   * @param model to be consumed
   **/
  void consume(M model);
}
