package org.beigesoft.holder;

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
 * <p>Abstraction of generic holder of a thing for given class,
 * and type of this thing doesn't reflect to class e.g. ID field name.</p>
 *
 * @param <M> type of thing.
 * @author Yury Demidenko
 */
public interface IHolderForClass<M> {

  /**
   * <p>Get thing for given class.</p>
   * @param pClass a Class
   * @return a thing
   **/
  M getFor(Class<?> pClass);

  /**
   * <p>Set thing for given class.</p>
   * @param pThing Thing
   * @param pClass Class
   **/
  void setFor(M pThing, Class<?> pClass);
}
