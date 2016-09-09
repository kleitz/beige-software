package org.beigesoft.holder;

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
