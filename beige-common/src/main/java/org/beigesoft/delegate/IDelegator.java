package org.beigesoft.delegate;

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
