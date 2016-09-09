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

import java.math.BigDecimal;

/**
 * <pre>
 * Model of entity that hold inventory item to draw.
 * It loads(put) an item into warehouse.
 * </pre>
 *
 * @author Yury Demidenko
 */
public interface IDrawItemSource extends IMakingWarehouseEntry {

  /**
   * <p>Getter for theRest.</p>
   * @return BigDecimal
   **/
  BigDecimal getTheRest();

  /**
   * <p>Setter for theRest.</p>
   * @param pTheRest reference
   **/
  void setTheRest(BigDecimal pTheRest);

  /**
   * <p>Getter for ItsCost.</p>
   * @return Long
   **/
  BigDecimal getItsCost();

  /**
   * <p>Setter for ItsCost.</p>
   * @param pItsCost reference
   **/
  void setItsCost(BigDecimal pItsCost);
}
