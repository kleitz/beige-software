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
 * Warehouse Site Rest Line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class WarehouseSiteRestLine extends WarehouseRestLine {

  /**
   * <p>Warehouse site name.</p>
   **/
  private String warehouseSite;
  //Simple getters and setters:

  /**
   * <p>Getter for warehouseSite.</p>
   * @return String
   **/
  public final String getWarehouseSite() {
    return this.warehouseSite;
  }

  /**
   * <p>Setter for warehouseSite.</p>
   * @param pWarehouseSite reference
   **/
  public final void setWarehouseSite(final String pWarehouseSite) {
    this.warehouseSite = pWarehouseSite;
  }
}
