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
