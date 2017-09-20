package org.beigesoft.factory;

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

import java.util.Map;

/**
 * <p>Abstraction of type checked application scope beans factory.
 * Beans are same super-type and same sphere, e.g. fields fillers.
 * This factory designed to produce beans for objects that can has
 * only bean, e.g. object filler. Otherwise e.g. object Date has several
 * ISrvToString and should use IFactoryAppBeansByName.</p>
 *
 * @param <T> bean super-type
 * @author Yury Demidenko
 */
public interface IFactoryAppBeansByClass<T> {

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanClass - bean class
   * @return requested bean
   * @throws Exception - an exception
   */
  T lazyGet(Map<String, Object> pAddParam,
    Class<?> pBeanClass) throws Exception;

  /**
   * <p>Set bean.</p>
   * @param pBeanClass - bean class
   * @param pBean bean
   * @throws Exception - an exception
   */
  void set(Class<?> pBeanClass, T pBean) throws Exception;
}
