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
 * Wage tax table method.
 * Version changed time algorithm.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class WageTaxesMethod extends AHasNameIdLongVersion {

  /**
   * <p>Service name, e.g. SrvWageTaxPercentageTable.</p>
   **/
  private String serviceName;

  //Simple getters and setters:
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
