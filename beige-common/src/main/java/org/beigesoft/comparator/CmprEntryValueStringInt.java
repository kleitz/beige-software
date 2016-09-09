package org.beigesoft.comparator;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;

/**
 * <p>Comparator for Entry.getValue().get(orderKey).Value of String that
 * contain Integer.toString() value.</p>
 *
 * @author Yury Demidenko
 */
public class CmprEntryValueStringInt
  implements Comparator<Map.Entry<String, Map<String, String>>>, Serializable {

  /**
   * <p>serialVersionUID.</p>
   **/
  static final long serialVersionUID = 49732947987712L;

  /**
   * <p>Key of order property.</p>
   **/
  private String orderKey;

  /**
   * <p>Only constructor.</p>
   * @param pOrderKey order key
   **/
  public CmprEntryValueStringInt(final String pOrderKey) {
    this.orderKey = pOrderKey;
  }

  @Override
  public final int compare(final Map.Entry<String, Map<String, String>> o1,
          final Map.Entry<String, Map<String, String>> o2) {
    Integer intO1 = Integer.valueOf(o1.getValue().get(orderKey));
    Integer intO2 = Integer.valueOf(o2.getValue().get(orderKey));
    return intO1.compareTo(intO2);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for orderKey.</p>
   * @return String
   **/
  public final String getOrderKey() {
    return this.orderKey;
  }
}
