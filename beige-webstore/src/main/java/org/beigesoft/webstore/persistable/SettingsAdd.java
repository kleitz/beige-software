package org.beigesoft.webstore.persistable;

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
