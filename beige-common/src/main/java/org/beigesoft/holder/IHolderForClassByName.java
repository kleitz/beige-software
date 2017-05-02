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
 * <p>Abstraction of generic holder of a thing for given class and thing name,
 * and type of this thing doesn't reflect to class and thing,
 * e.g. field's converter name.</p>
 *
 * @param <M> type of thing.
 * @author Yury Demidenko
 */
public interface IHolderForClassByName<M> {

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pClass a Class
   * @param pThingName Thing Name
   * @return a thing
   **/
  M getFor(Class<?> pClass, String pThingName);

  /**
   * <p>Set thing for given class and thing name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pThingName Thing Name
   **/
  void setFor(M pThing, Class<?> pClass, String pThingName);
}
