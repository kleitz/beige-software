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

import org.beigesoft.model.IOwned;
import org.beigesoft.persistable.APersistableBaseVersion;

/**
 * <pre>
 * Model of Wage Type Line of payroll tax table.
 * Version changed time algorithm.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class WageTaxTableType extends APersistableBaseVersion
  implements IOwned<WageTaxTable> {

  /**
   * <p>Wage.</p>
   **/
  private WageTaxTable itsOwner;

  /**
   * <p>Wage Type, not null, e.g. Cooking, Sick compensation.</p>
   **/
  private WageType wageType;

  /**
   * <p>Getter for itsOwner.</p>
   * @return WageTaxTable
   **/
  @Override
  public final WageTaxTable getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final WageTaxTable pItsOwner) {
    this.itsOwner = pItsOwner;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for wageType.</p>
   * @return WageType
   **/
  public final WageType getWageType() {
    return this.wageType;
  }

  /**
   * <p>Setter for wageType.</p>
   * @param pWageType reference
   **/
  public final void setWageType(final WageType pWageType) {
    this.wageType = pWageType;
  }
}
