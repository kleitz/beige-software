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
 * Beans are same type and same sphere, e.g. fields converters.</p>
 *
 * @param <T> bean super-type
 * @author Yury Demidenko
 */
public interface IFactoryAppBeansByName<T> {

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanName - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  T lazyGet(Map<String, Object> pAddParam, String pBeanName) throws Exception;

  /**
   * <p>Set bean.</p>
   * @param pBeanName - bean name
   * @param pBean bean
   * @throws Exception - an exception
   */
  void set(String pBeanName, T pBean) throws Exception;
}
