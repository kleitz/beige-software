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

/**
 * <p>Abstraction of generic delegate without parameters
 * that can throws Exception.</p>
 *
 * @author Yury Demidenko
 */
public interface IDelegateSimpleExc {

  /**
   * <p>Make something.</p>
   * @throws Exception - an exception
   **/
  void make() throws Exception;
}
