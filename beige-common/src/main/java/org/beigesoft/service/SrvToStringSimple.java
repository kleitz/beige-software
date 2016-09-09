package org.beigesoft.service;

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
 * <p>Simple delegator to Object.toString().</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model to be explained
 */
public class SrvToStringSimple<M> implements ISrvToString<M> {

  /**
   * <p>Say about model.</p>
   * @param model to be explained
   * @return String representation
   **/
  public final String toString(final M model) {
    return model.toString();
  }
}
