package org.beigesoft.delegate;

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
 * <p>Abstraction of generic delegate that
 * evaluates something from given data.</p>
 *
 * @author Yury Demidenko
 * @param <M> type given data
 * @param <R> type of evaluated data
 */
public interface IDelegateEvaluate<M, R> {

  /**
   * <p>Evaluate something from given data.</p>
   * @param pData data
   * @return evaluated data
   * @throws Exception - an exception
   **/
  R evaluate(M pData) throws Exception;
}
