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
   *
   * @param original bean
   * @return M copy of original bean (request(or) scoped)
   */
  M createClone(M original);
}
