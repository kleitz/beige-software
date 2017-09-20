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
