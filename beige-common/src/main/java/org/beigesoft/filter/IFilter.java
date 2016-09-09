package org.beigesoft.filter;

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
 * <p>Abstraction of base filter.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model to be examined
 */
public interface IFilter<M> {

  /**
   * <p>Examine model for accepted.</p>
   * @param model to be examined
   * @return boolean - is accepted
   **/
  boolean isAccepted(M model);
}
