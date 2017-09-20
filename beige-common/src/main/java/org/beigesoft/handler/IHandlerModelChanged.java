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
