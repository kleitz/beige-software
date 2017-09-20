package org.beigesoft.orm.holder;

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
import java.util.Hashtable;

import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.orm.processor.PrcEntitiesPage;

/**
 * <p>Generic service that assign processor name for class
 * and action name.</p>
 *
 * @author Yury Demidenko
 */
public class HldProcessorNames
  implements IHolderForClassByName<String> {

  /**
   * <p>Processors names map:
   * "key = class simple name + action"-"processor name".</p>
   **/
  private final Map<String, String> processorsNamesMap =
      new Hashtable<String, String>();

  /**
   * <p>Get thing for given class and thing name.
   * findbugs: UG_SYNC_SET_UNSYNC_GET - this code is designed
   * for high performance. Getting name is happened very frequency
   * (e.g. 10 per second by multi-threads).
   * Setting is seldom (e.g. hot change configuration to fix program bug)
   * or may not be happen.</p>
   * @param pClass a Class
   * @param pThingName Thing Name
   * @return a thing
   **/
  @Override
  public final String getFor(final Class<?> pClass, final String pThingName) {
    if ("list".equals(pThingName)) {
      return PrcEntitiesPage.class.getSimpleName();
    }
    return this.processorsNamesMap
      .get(pClass.getSimpleName() + pThingName);
  }

  /**
   * <p>Set thing for given class and thing name.</p>
   * @param pThing Thing
   * @param pClass Class
   * @param pThingName Thing Name
   **/
  @Override
  public final synchronized void setFor(final String pThing,
    final Class<?> pClass, final String pThingName) {
    if ("list".equals(pThingName)) {
      throw new RuntimeException("Forbidden code!");
    }
    this.processorsNamesMap
      .put(pClass.getSimpleName() + pThingName, pThing);
  }
}
