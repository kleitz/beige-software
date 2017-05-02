package org.beigesoft.factory;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
