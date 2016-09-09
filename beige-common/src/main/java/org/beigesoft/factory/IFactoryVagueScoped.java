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
 * <p>Client don't care about requested bean scope.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of created bean
 */
public interface IFactoryVagueScoped<M> {

  /**
   * <p>Create (bean per request) or lazy
   * initialize and get app scoped bean.</p>
   * @return M any scoped bean
   **/
  M createOrGet();
}
