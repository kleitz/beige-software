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

import java.util.List;

import org.beigesoft.accounting.persistable.base.ADoc;
import org.beigesoft.accounting.model.EWarehouseMovementType;

/**
 * <pre>
 * Model of Beginning Inventory.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class BeginningInventory extends ADoc
  implements IDocWarehouse {

  /**
   * <p>Lines.</p>
   **/
  private List<BeginningInventoryLine> itsLines;

  /**
   * <p>OOP friendly Constant of code type 15.</p>
   **/
  @Override
  public final Integer constTypeCode() {
    return 15;
  }

  /**
   * <p>If owned lines make warehouse entries this return
   * their type.</p>
   * @return Boolean
   **/
  @Override
  public final EWarehouseMovementType getLinesWarehouseType() {
    return EWarehouseMovementType.LOAD;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for itsLines.</p>
   * @return List<BeginningInventoryLine>
   **/
  public final List<BeginningInventoryLine> getItsLines() {
    return this.itsLines;
  }

  /**
   * <p>Setter for itsLines.</p>
   * @param pItsLines reference
   **/
  public final void setItsLines(final List<BeginningInventoryLine> pItsLines) {
    this.itsLines = pItsLines;
  }
}
