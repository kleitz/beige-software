package org.beigesoft.webstore.holder;

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
import java.util.Hashtable;

import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.webstore.processor.PrcTradeEntitiesPage;

/**
 * <p>Generic service that assign processor name for class
 * and action name. The most trade processors doesn't need to map action
 * to their name cause it (name) passed in request into dedicated
 * TransactionalRequestHandler, e.g. PrcWebstoreStart,
 * PrcAssignGoodsToCatalog.</p>
 *
 * @author Yury Demidenko
 */
public class HldTradeProcessorNames
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
      // for performance reason (do not check class type - accounting or trade)
      // it return trade copy processor that is same as accounting one but added
      // trading settings
      return PrcTradeEntitiesPage.class.getSimpleName();
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
