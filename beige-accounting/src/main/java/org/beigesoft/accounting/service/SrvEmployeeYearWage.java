package org.beigesoft.accounting.service;

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
import java.util.Map;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.Employee;
import org.beigesoft.accounting.persistable.EmployeeYearWage;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for none-editable Employee Year Wage Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvEmployeeYearWage<RS>
  extends ASrvAccEntityImmutable<RS, EmployeeYearWage>
    implements ISrvEntityOwned<EmployeeYearWage, Employee> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvEmployeeYearWage() {
    super(EmployeeYearWage.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvEmployeeYearWage(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings) {
    super(EmployeeYearWage.class, pSrvOrm, pSrvAccSettings);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final EmployeeYearWage createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "forbidden_operation");
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final EmployeeYearWage retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "forbidden_operation");
  }

  /**
   * <p>Insert immutable line into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, ?> pAddParam,
    final EmployeeYearWage pEntity,
      final boolean isEntityDetached) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "forbidden_operation");
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner entity itsOwner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final EmployeeYearWage createEntityWithOwnerById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "forbidden_operation");
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final EmployeeYearWage createEntityWithOwner(
    final Map<String, ?> pAddParam,
      final Employee pEntityItsOwner) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "forbidden_operation");
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner ID itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<EmployeeYearWage> retrieveOwnedListById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(EmployeeYearWage.class,
      Employee.class, pIdEntityItsOwner);
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<EmployeeYearWage> retrieveOwnedList(
    final Map<String, ?> pAddParam,
      final Employee pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(EmployeeYearWage.class,
      Employee.class, pEntityItsOwner.getItsId());
  }
}
