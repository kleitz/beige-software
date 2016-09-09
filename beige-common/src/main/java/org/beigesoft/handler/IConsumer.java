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
