package org.beigesoft.model;

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
 * <p>Abstraction an persistable model that has owner,
 * usually a line.</p>
 *
 * @author Yury Demidenko
 * @param <O> type of owner
 */
public interface IOwned<O> {

  /**
   * <p>Usually it's simple getter that return owner.</p>
   * @return O owner
   **/
  O getItsOwner();

  /**
   * <p>Usually it's simple setter for owner.</p>
   * @param pOwner reference
   **/
  void setItsOwner(O pOwner);
}
