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

import org.beigesoft.persistable.AHasNameIdLongVersion;

/**
 * <pre>
 * COGS method.
 * Version changed time algorithm.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class CogsMethod extends AHasNameIdLongVersion {

  /**
   * <p>SQL file name without extension "sql", unchangeable, not Null
   * e.g. trade/drawItemFifoSourceM1S.</p>
   **/
  private String fileName;

  /**
   * <p>Is periodic.</p>
   **/
  private Boolean isPeriodic = false;

  //Simple getters and setters:
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
