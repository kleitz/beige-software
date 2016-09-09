package org.beigesoft.model;

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
 * <p>Abstraction an persistable model that has owner,
 * usually a line.</p>
 *
 * @author Yury Demidenko
 * @param <O> type of owner
 */
public interface IOwned<O> extends IEditable {

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
