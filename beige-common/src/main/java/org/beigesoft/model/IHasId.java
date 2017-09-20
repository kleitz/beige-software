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
