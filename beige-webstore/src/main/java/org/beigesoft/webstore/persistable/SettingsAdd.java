package org.beigesoft.webstore.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.beigesoft.persistable.AHasIdLongVersion;

/**
 * <pre>
 * Hold additional settings.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SettingsAdd extends AHasIdLongVersion {

  /**
   * <p>Records per transactions for bulk operations.</p>
   **/
  private Integer recordsPerTransaction;

  //Simple getters and setters:
  /**
   * <p>Getter for recordsPerTransaction.</p>
   * @return Integer
   **/
  public final Integer getRecordsPerTransaction() {
    return this.recordsPerTransaction;
  }

  /**
   * <p>Setter for recordsPerTransaction.</p>
   * @param pRecordsPerTransaction reference
   **/
  public final void setRecordsPerTransaction(
    final Integer pRecordsPerTransaction) {
    this.recordsPerTransaction = pRecordsPerTransaction;
  }
}
