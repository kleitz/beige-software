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

import org.beigesoft.model.IHasName;

/**
 * <p>Simple delegator to IHasName.getItsName().</p>
 *
 * @author Yury Demidenko
 * @param <M> type of model to be explained
 */
public class SrvToStringHasName<M extends IHasName> implements ISrvToString<M> {

  /**
   * <p>Say about model.</p>
   * @param model to be explained
   * @return String representation
   **/
  public final String toString(final M model) {
    return model.getItsName();
  }
}
