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
 * <p>Abstraction an editable model that has status "is new".
 * Also it's used for persistable model to make decision
 * "insert or update?".
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IEditable {

  /**
   * <p>Evaluate "is new" status.
   * Usually it's simple getter.
   * </p>
   * @return boolean "is new?"
   **/
  Boolean getIsNew();

  /**
   * <p>Set "is new" status.
   * Usually it's simple setter.
   * </p>
   * @param isNew "is new?"
   **/
  void setIsNew(Boolean isNew);
}
