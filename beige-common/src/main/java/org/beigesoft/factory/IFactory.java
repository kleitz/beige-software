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
 * Interface of Factory that make a bean copy (clone).
 * </pre>
 *
 * @author Yury Demidenko
 * @param <M> type of created bean
 **/
public interface IFactory<M>
  extends IFactorySimple<M> {

  /**
   * <p>Create bean based on original.</p>
   * @param pAddParam additional param
   * @param original bean
   * @return M copy of original bean (request(or) scoped)
   * @throws Exception - an exception
   */
  M createClone(Map<String, Object> pAddParam, M original) throws Exception;
}
