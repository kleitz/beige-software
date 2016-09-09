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
 * Model of used Inventory Item Category in AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class InvItemCategoryUsed extends ASubaccountUsed<InvItemCategory> {

  /**
   * <p>InvItemCategory.</p>
   **/
  private InvItemCategory subaccount;

  /**
   * <p>Getter for subaccount.</p>
   * @return InvItemCategory
   **/
  @Override
  public final InvItemCategory getSubaccount() {
    return this.subaccount;
  }

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  @Override
  public final void setSubaccount(final InvItemCategory pSubaccount) {
    this.subaccount = pSubaccount;
  }
}
