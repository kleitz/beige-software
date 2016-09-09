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

import org.beigesoft.persistable.AHasIdLong;
import org.beigesoft.model.IHasName;

/**
 * <pre>
 * COGS method.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class CogsMethod extends AHasIdLong implements IHasName {

  /**
   * <p>Version, changed time algorithm.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Name, Not null, unchangeable, "FIFO perpetual".</p>
   **/
  private String itsName;

  /**
   * <p>SQL file name without extension "sql", unchangeable, not Null
   * e.g. trade/drawItemFifoSourceM1S.</p>
   **/
  private String fileName;

  /**
   * <p>Is periodic.</p>
   **/
  private Boolean isPeriodic = false;

  /**
   * <p>Getter for itsName.</p>
   * @return String
   **/
  @Override
  public final String getItsName() {
    return this.itsName;
  }

  /**
   * <p>Setter for itsName.</p>
   * @param pItsName reference
   **/
  @Override
  public final void setItsName(final String pItsName) {
    this.itsName = pItsName;
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

  /**
   * <p>Geter for fileName.</p>
   * @return String
   **/
  public final String getFileName() {
    return this.fileName;
  }

  /**
   * <p>Setter for fileName.</p>
   * @param pFileName reference
   **/
  public final void setFileName(final String pFileName) {
    this.fileName = pFileName;
  }

  /**
   * <p>Getter for isPeriodic.</p>
   * @return Boolean
   **/
  public final Boolean getIsPeriodic() {
    return this.isPeriodic;
  }

  /**
   * <p>Setter for isPeriodic.</p>
   * @param pIsPeriodic reference
   **/
  public final void setIsPeriodic(final Boolean pIsPeriodic) {
    this.isPeriodic = pIsPeriodic;
  }
}
