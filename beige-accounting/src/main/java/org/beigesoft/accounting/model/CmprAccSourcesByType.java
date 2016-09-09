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

import java.util.Comparator;
import java.io.Serializable;

import org.beigesoft.accounting.persistable.AccEntriesSourcesLine;

/**
 * <p>Comparator for accounting entries source
 * by type DEBIT/DEBIT_CREDIT/CREDIT.</p>
 *
 * @author Yury Demidenko
 */
public class CmprAccSourcesByType
  implements Comparator<AccEntriesSourcesLine>, Serializable {

  /**
   * <p>serialVersionUID.</p>
   **/
  static final long serialVersionUID = 49731247864712L;

  @Override
  public final int compare(final AccEntriesSourcesLine o1,
          final AccEntriesSourcesLine o2) {
    return o1.getEntriesAccountingType()
      .compareTo(o2.getEntriesAccountingType());
  }
}
