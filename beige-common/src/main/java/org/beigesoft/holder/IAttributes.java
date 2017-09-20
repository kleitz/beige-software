package org.beigesoft.holder;

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
 * <p>Abstraction of non-generic attributes holder.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IAttributes {

  /**
   * <p>Get attribute.</p>
   * @param pName attribute name
   * @return attribute
   **/
  Object getAttribute(String pName);

  /**
   * <p>Set attribute.</p>
   * @param pName attribute name
   * @param pAttr attribute
   **/
  void setAttribute(String pName, Object pAttr);
}
