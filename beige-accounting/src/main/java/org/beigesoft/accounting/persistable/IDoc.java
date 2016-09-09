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

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.persistable.IPersistableBase;
import org.beigesoft.model.IHasTypeCode;

/**
 * <pre>
 * Abstract model of document that makes accounting entry,
 * e.g. PurchaseInvoice, Manufacture
 * </pre>
 *
 * @author Yury Demidenko
 */
public interface IDoc extends IPersistableBase, IHasTypeCode {

  /**
   * <p>Geter for hasMadeAccEntries.</p>
   * @return Boolean
   **/
  Boolean getHasMadeAccEntries();

  /**
   * <p>Setter for hasMadeAccEntries.</p>
   * @param pHasMadeAccEntries reference
   **/
  void setHasMadeAccEntries(Boolean pHasMadeAccEntries);

  /**
   * <p>Geter for reversedId.</p>
   * @return Long
   **/
  Long getReversedId();

  /**
   * <p>Setter for reversedId.</p>
   * @param pReversedId reference
   **/
  void setReversedId(Long pReversedId);

  /**
   * <p>Geter for itsDate.</p>
   * @return Date
   **/
  Date getItsDate();

  /**
   * <p>Setter for itsDate.</p>
   * @param pItsDate reference
   **/
  void setItsDate(Date pItsDate);

  /**
   * <p>Setter for itsTotal.</p>
   * @param pItsTotal reference
   **/
  void setItsTotal(BigDecimal pItsTotal);

  /**
   * <p>Getter for itsTotal.</p>
   * @return BigDecimal
   **/
  BigDecimal getItsTotal();

  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  String getDescription();

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  void setDescription(String pDescription);
}
