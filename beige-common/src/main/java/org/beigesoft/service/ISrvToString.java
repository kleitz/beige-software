package org.beigesoft.service;

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
 * <p>Flexible alternative to Object.toString().</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model to be explained
 */
public interface ISrvToString<M> {

  /**
   * <p>Say about model.</p>
   * @param model to be explained
   * @return String representation
   **/
  String toString(M model);
}
