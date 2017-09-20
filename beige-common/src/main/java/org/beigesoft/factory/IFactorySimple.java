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
 * <pre>
 * Simple factory that create a request(or) scoped bean.
 * </pre>
 *
 * @author Yury Demidenko
 * @param <M> type of created bean
 **/
public interface IFactorySimple<M> {

  /**
   * <p>Create a bean.</p>
   * @param pAddParam additional param
   * @return M request(or) scoped bean
   * @throws Exception - an exception
   */
  M create(Map<String, Object> pAddParam) throws Exception;
}
