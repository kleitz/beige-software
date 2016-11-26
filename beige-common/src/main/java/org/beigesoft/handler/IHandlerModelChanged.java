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
 * <p>Abstraction of generic handler of model changed event.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of changed model
 */
public interface IHandlerModelChanged<M> {

  /**
   * <p>Handle model changed event.</p>
   * @param pModel which changed
   **/
  void handleModelChanged(M pModel);
}
