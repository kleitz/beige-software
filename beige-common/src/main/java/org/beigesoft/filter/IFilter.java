package org.beigesoft.filter;

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
