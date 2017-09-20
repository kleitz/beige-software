package org.beigesoft.holder;

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
