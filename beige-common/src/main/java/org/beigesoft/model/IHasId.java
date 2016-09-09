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
 * <p>Abstraction an persistable model that must has ID.</p>
 *
 * @author Yury Demidenko
 * @param <ID> type of ID
 */
public interface IHasId<ID> extends IEditable {

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  ID getItsId();

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pId model ID
   **/
  void setItsId(ID pId);
}
