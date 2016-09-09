package org.beigesoft.accounting.persistable.base;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.persistable.APersistableBase;

/**
 * <pre>
 * Model of used subaccount in AccountingEntries.
 * </pre>
 *
 * @param <T> extends ASubaccount type
 * @author Yury Demidenko
 */
public abstract class ASubaccountUsed<T extends ASubaccount>
  extends APersistableBase {

  /**
   * <p>Geter for subaccount.</p>
   * @return subaccount
   **/
  public abstract T getSubaccount();

  /**
   * <p>Setter for subaccount.</p>
   * @param pSubaccount reference
   **/
  public abstract void setSubaccount(T pSubaccount);
}
