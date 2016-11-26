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
 * <p>Abstraction of generic delegate that evaluates (retrieves) data.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model
 */
public interface IDelegateEval<M> {

  /**
   * <p>Evaluate (retrieve) model.</p>
   * @return evaluated data
   **/
  M evalData();
}
