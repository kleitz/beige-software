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
 * <p>Client expect an app-scoped bean.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of created bean
 */
public interface IFactoryAppScoped<M> {

  /**
   * <p>Lazy initialyze and return app-scoped bean.</p>
   * @return M app-scoped bean
   **/
  M lazyGet();
}
