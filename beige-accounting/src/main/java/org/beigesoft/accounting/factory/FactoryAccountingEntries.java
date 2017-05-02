package org.beigesoft.accounting.factory;

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
import java.util.Date;

import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.accounting.persistable.AccountingEntries;

/**
 * <pre>
 * Simple factory that create a request(or) scoped AccountingEntries.
 * </pre>
 *
 * @author Yury Demidenko
 **/
public class FactoryAccountingEntries
  implements IFactorySimple<AccountingEntries> {

  /**
   * <p>ID Database.</p>
   **/
  private Integer databaseId;

  /**
   * <p>Create AccountingEntries.</p>
   * @param pAddParam additional param
   * @return AccountingEntries request(or) scoped bean
   * @throws Exception - an exception
   */
  @Override
  public final AccountingEntries create(
    final Map<String, Object> pAddParam) throws Exception {
    AccountingEntries object = new AccountingEntries();
    object.setIsNew(false);
    object.setIdDatabaseBirth(this.databaseId);
    object.setItsDate(new Date());
    return object;
  }

  /**
   * <p>Getter for databaseId.</p>
   * @return Integer
   **/
  public final Integer getDatabaseId() {
    return this.databaseId;
  }

  /**
   * <p>Setter for databaseId.</p>
   * @param pDatabaseId reference
   **/
  public final void setDatabaseId(final Integer pDatabaseId) {
    this.databaseId = pDatabaseId;
  }
}
