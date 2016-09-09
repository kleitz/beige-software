package org.beigesoft.accounting.model;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <pre>
 * When Make Entries.
 * </pre>
 *
 * @author Yury Demidenko
 */
 public enum EWhenMakeEntries {

  /**
   * <p>When document saved into database.
   * This approach not recommended in "High load" case.</p>
   **/
  IMMEDIATELY,

  /**
   * <p>On demand.</p>
   **/
  ON_DEMAND;
}
