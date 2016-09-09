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

import org.beigesoft.accounting.persistable.base.ASubaccountUsed;

/**
 * <pre>
 * Model of used Wage Type in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class WageTypeUsed extends ASubaccountUsed<WageType> {

  /**
   * <p>WageType.</p>
   **/
  private WageType subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return WageType
   **/
  @Override
  public final WageType getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final WageType pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
