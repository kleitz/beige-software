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
import java.util.List;

import org.beigesoft.model.IHasTypeCode;
import org.beigesoft.persistable.APersistableBase;

/**
 * <pre>
 * Model of Move Items within/between warehouse/s.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class MoveItems extends APersistableBase
  implements IHasTypeCode {

  /**
   * <p>Version, changed time algorithm.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Date.</p>
   **/
  private Date itsDate;

  /**
   * <p>Lines.</p>
   **/
  private List<MoveItemsLine> itsLines;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>OOP friendly Constant of code type 14.</p>
   **/
  @Override
  public final Integer constTypeCode() {
    return 14;
  }

  //Hiding references getters and setters:
  /**
   * <p>Geter for itsDate.</p>
   * @return Date
   **/
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
  public final void setItsDate(final Date pItsDate) {
    if (pItsDate == null) {
      this.itsDate = null;
    } else {
      this.itsDate = new Date(pItsDate.getTime());
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }

  /**
   * <p>Geter for itsLines.</p>
   * @return List<MoveItemsLine>
   **/
  public final List<MoveItemsLine> getItsLines() {
    return this.itsLines;
  }

  /**
   * <p>Setter for itsLines.</p>
   * @param pItsLines reference
   **/
  public final void setItsLines(final List<MoveItemsLine> pItsLines) {
    this.itsLines = pItsLines;
  }

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
