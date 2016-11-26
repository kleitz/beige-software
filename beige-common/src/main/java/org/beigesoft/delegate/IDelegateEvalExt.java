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

import java.util.Map;

/**
 * <p>Abstraction of generic delegate that evaluates (retrieves) data.
 * For complex business logic.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model
 */
public interface IDelegateEvalExt<M> {

  /**
   * <p>Evaluate (retrieve) model.</p>
   * @param pAddParams additional params, may be null.
   * @throws Exception - an exception
   * @return evaluated data
   **/
  M evalData(Map<String, Object> pAddParams) throws Exception;
}
