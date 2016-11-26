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
import org.beigesoft.persistable.APersistableBaseVersion;

/**
 * <pre>
 * Model of Wage Employer Line of payroll tax table.
 * Version changed time algorithm.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class WageTaxTableEmployee extends APersistableBaseVersion
  implements IOwned<WageTaxTable> {

  /**
   * <p>Wage.</p>
   **/
  private WageTaxTable itsOwner;

  /**
   * <p>Employee.</p>
   **/
  private Employee employee;

  /**
   * <p>Allowance, not null.</p>
   **/
  private BigDecimal allowance = BigDecimal.ZERO;

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
   * <p>Getter for employee.</p>
   * @return Employee
   **/
  public final Employee getEmployee() {
    return this.employee;
  }

  /**
   * <p>Setter for employee.</p>
   * @param pEmployee reference
   **/
  public final void setEmployee(final Employee pEmployee) {
    this.employee = pEmployee;
  }

  /**
   * <p>Getter for allowance.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getAllowance() {
    return this.allowance;
  }

  /**
   * <p>Setter for allowance.</p>
   * @param pAllowance reference
   **/
  public final void setAllowance(final BigDecimal pAllowance) {
    this.allowance = pAllowance;
  }
}
