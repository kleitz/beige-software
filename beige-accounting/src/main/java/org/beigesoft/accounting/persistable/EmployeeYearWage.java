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

import org.beigesoft.model.IOwned;
import org.beigesoft.persistable.APersistableBase;

/**
 * <pre>
 * Model of Employee Year Wage Line.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class EmployeeYearWage extends APersistableBase
  implements IOwned<Employee> {

  /**
   * <p>Version changed time algorithm.</p>
   **/
  private Long itsVersion;

  /**
   * <p>Wage.</p>
   **/
  private Employee itsOwner;

  /**
   * <p>Work Type.</p>
   **/
  private WageType wageType;

  /**
   * <p>Not Null, total wage of this type in current year.</p>
   **/
  private BigDecimal totalWageYear = BigDecimal.ZERO;

  /**
   * <p>Getter for itsOwner.</p>
   * @return Employee
   **/
  @Override
  public final Employee getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final Employee pItsOwner) {
    this.itsOwner = pItsOwner;
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

  /**
   * <p>Getter for totalWageYear.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotalWageYear() {
    return this.totalWageYear;
  }

  /**
   * <p>Setter for totalWageYear.</p>
   * @param pTotalWageYear reference
   **/
  public final void setTotalWageYear(final BigDecimal pTotalWageYear) {
    this.totalWageYear = pTotalWageYear;
  }
}
