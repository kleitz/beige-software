package org.beigesoft.delegate;

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
 * <p>Useful abstraction of delegate that make something
 * and can use abstract parameter pAddParams,
 * it can throws Exception.</p>
 *
 * @author Yury Demidenko
 */
public interface IDelegator {

  /**
   * <p>Make something.</p>
   * @param pAddParams additional params, may be null.
   * @throws Exception - an exception
   **/
  void make(Map<String, Object> pAddParams) throws Exception;
}
