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
