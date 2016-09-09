package org.beigesoft.properties;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Properties;
import java.util.LinkedHashSet;

/**
 * <p>Comparator for Entry.getValue().get(orderKey).Value of String that
 * contain Integer.toString() value.</p>
 *
 * @author Yury Demidenko
 */
public class LinkedProperties extends Properties {

  /**
   * <p>Ordered keys.</p>
   **/
  private final LinkedHashSet<String> orderedKeys =
    new LinkedHashSet<String>();

  @Override
  public final Object put(final Object key, final Object value) {
    orderedKeys.add(key.toString());
    return super.put(key, value);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for orderedKeys.</p>
   * @return final LinkedHashSet<String>
   **/
  public final LinkedHashSet<String> getOrderedKeys() {
    return this.orderedKeys;
  }
}
