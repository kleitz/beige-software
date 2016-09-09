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
public class WageTaxesMethod extends AHasIdLong implements IHasName {

  /**
   * <p>Version, changed time algorithm.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Name, Not null, unchangeable, "Standard wage tax percentage table".</p>
   **/
  private String itsName;

  /**
   * <p>Service name, e.g. SrvWageTaxPercentageTable.</p>
   **/
  private String serviceName;

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
   * <p>Getter for itsVersion.</p>
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
   * <p>Getter for serviceName.</p>
   * @return String
   **/
  public final String getServiceName() {
    return this.serviceName;
  }

  /**
   * <p>Setter for serviceName.</p>
   * @param pServiceName reference
   **/
  public final void setServiceName(final String pServiceName) {
    this.serviceName = pServiceName;
  }
}
