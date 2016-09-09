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

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.persistable.APersistableBase;
import org.beigesoft.accounting.persistable.IDoc;

/**
 * <pre>
 * Abstraction of a document that makes accounting entries.
 * </pre>
 *
 * @author Yury Demidenko
 */
public abstract class ADoc extends APersistableBase
  implements IDoc {

  /**
   * <p>Version, changed time algorithm cause check dirty of
   * calculated from it (derived) records.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Date.</p>
   **/
  private Date itsDate;

  /**
   * <p>If document has made accounting entries
   * then he can only reverse whole document.</p>
   **/
  private Boolean hasMadeAccEntries = false;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal itsTotal = new BigDecimal("0.00");

  /**
   * <p>ID of reversed/reversing document.</p>
   **/
  private Long reversedId;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Geter for hasMadeAccEntries.</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getHasMadeAccEntries() {
    return this.hasMadeAccEntries;
  }

  /**
   * <p>Setter for hasMadeAccEntries.</p>
   * @param pHasMadeAccEntries reference
   **/
  @Override
  public final void setHasMadeAccEntries(final Boolean pHasMadeAccEntries) {
    this.hasMadeAccEntries = pHasMadeAccEntries;
  }

  /**
   * <p>Geter for reversedId.</p>
   * @return Long
   **/
  @Override
  public final Long getReversedId() {
    return this.reversedId;
  }

  /**
   * <p>Setter for reversedId.</p>
   * @param pReversedId reference
   **/
  @Override
  public final void setReversedId(final Long pReversedId) {
    this.reversedId = pReversedId;
  }

  /**
   * <p>Setter for itsTotal.</p>
   * @param pItsTotal reference
   **/
  @Override
  public final void setItsTotal(final BigDecimal pItsTotal) {
    this.itsTotal = pItsTotal;
  }

  /**
   * <p>Geter for itsTotal.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getItsTotal() {
    return this.itsTotal;
  }

  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  @Override
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  @Override
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }

  //Hiding references getters and setters:
  /**
   * <p>Geter for itsDate.</p>
   * @return Date
   **/
  @Override
  public final Date getItsDate() {
    if (this.itsDate == null) {
      return null;
    }
    return new Date(this.itsDate.getTime());
  }

  /**
   * <p>Setter for itsDate.</p>
   * @param pItsDate reference
   **/
  @Override
  public final void setItsDate(final Date pItsDate) {
    if (pItsDate == null) {
      this.itsDate = null;
    } else {
      this.itsDate = new Date(pItsDate.getTime());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for itsVersion.</p>
   * @return Long
   **/
  public final Long getItsVersion() {
    return this.itsVersion;
  }

  /**
   * <p>Setter for itsVersion.</p>
   * @param pItsVersion reference
   **/
  public final void setItsVersion(final Long pItsVersion) {
    this.itsVersion = pItsVersion;
  }
}
