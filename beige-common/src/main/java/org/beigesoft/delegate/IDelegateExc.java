package org.beigesoft.delegate;

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
 * <p>Abstraction of generic delegate that make something with a parameter
 * that can throws Exception.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model
 */
public interface IDelegateExc<M> {

  /**
   * <p>Make something with a model.</p>
   * @param model to make
   * @throws Exception - an exception
   **/
  void makeWith(M model) throws Exception;
}
