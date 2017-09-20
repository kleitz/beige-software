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

/**
 * <p>Abstraction of application beans factory.
 * This simple, cheap but powerful alternative to CDI.
 * It is pure OOP abstraction method.
 * This factory is able to free memory (release beans)
 * when requestors don't required any bean in a time interval,
 * so it also memory friendly approach.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface IFactoryAppBeans {

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  Object lazyGet(String pBeanName) throws Exception;

  /**
   * <p>Release beans (memory). This is "memory friendly" factory</p>
   * @throws Exception - an exception
   */
  void releaseBeans() throws Exception;
}
