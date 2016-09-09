package org.beigesoft.accounting.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.accounting.persistable.base.ASubaccount;

/**
 * <pre>
 * Model of depreciable property.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class Property extends ASubaccount {

  /**
   * <p>OOP friendly Constant of code type.</p>
   * @return 2010
   **/
  @Override
  public final Integer constTypeCode() {
    return 2010;
  }
}
