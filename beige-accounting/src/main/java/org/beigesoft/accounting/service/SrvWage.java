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

import java.util.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.text.DateFormat;

import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.accounting.persistable.Wage;
import org.beigesoft.accounting.persistable.WageLine;
import org.beigesoft.accounting.persistable.Employee;
import org.beigesoft.accounting.persistable.EmployeeYearWage;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.service.ISrvI18n;

/**
 * <p>Business service for wage.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvWage<RS>
  extends ASrvDocument<RS, Wage> {

  /**
   * <p>App beans factort.</p>
   **/
  private IFactoryAppBeans factoryAppBeans;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvWage() {
    super(Wage.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   * @param pFactoryAppBeans Factory of App-Beans
   **/
  public SrvWage(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvI18n pSrvI18n, final DateFormat pDateFormatter,
          final IFactoryAppBeans pFactoryAppBeans) {
    super(Wage.class, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvI18n, pDateFormatter);
    this.factoryAppBeans = pFactoryAppBeans;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final Wage createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    Wage entity = new Wage();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsDate(new Date());
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Make additional preparations on entity copy.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeAddPrepareForCopy(final Map<String, Object> pAddParam,
    final Wage pEntity) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "reverse".equals(parameterMap.get("actionAdd")[0])) {
      pEntity.setTotalTaxesEmployee(pEntity.getTotalTaxesEmployee().negate());
      pEntity.setTotalTaxesEmployer(pEntity.getTotalTaxesEmployer().negate());
      pEntity.setNetWage(pEntity.getNetWage().negate());
    } else {
      pEntity.setTotalTaxesEmployer(BigDecimal.ZERO);
      pEntity.setTotalTaxesEmployee(BigDecimal.ZERO);
      pEntity.setNetWage(BigDecimal.ZERO);
    }
  }

  /**
   * <p>If actionAdd is "fill" then make taxes
   * lines according chosen method.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pIsNew if entity was new
   * @throws Exception - an exception
   **/
  @Override
  public final void makeOtherEntries(final Map<String, Object> pAddParam,
    final Wage pEntity, final boolean pIsNew) throws Exception {
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "fill".equals(parameterMap.get("actionAdd")[0])) {
      //User can change method as he want
      String srvFillWgLnNm = getSrvAccSettings().lazyGetAccSettings()
        .getWageTaxesMethod().getServiceName();
      ISrvFillWageLines srvFillWageLines = (ISrvFillWageLines) this
        .factoryAppBeans.lazyGet(srvFillWgLnNm);
      srvFillWageLines.fillWageLines(pAddParam, pEntity);
    } else if (pEntity.getReversedId() != null || parameterMap
      .get("actionAdd") != null && "makeAccEntries"
        .equals(parameterMap.get("actionAdd")[0])) {
      List<WageLine> wageLines = getSrvOrm().
        retrieveEntityOwnedlist(WageLine.class,
          Wage.class, pEntity.getItsId());
      if (pEntity.getReversedId() == null) {
        for (WageLine wageLine : wageLines) {
          String whereStr = " where ITSOWNER=" + pEntity.getEmployee()
            .getItsId() + " and WAGETYPE=" + wageLine.getWageType().getItsId();
          EmployeeYearWage employeeYearWage = getSrvOrm()
            .retrieveEntityWithConditions(EmployeeYearWage.class, whereStr);
          if (employeeYearWage == null) {
            employeeYearWage = getSrvOrm()
              .createEntityWithOwner(EmployeeYearWage.class,
                Employee.class, pEntity.getEmployee().getItsId());
            employeeYearWage.setIsNew(true);
            employeeYearWage.setWageType(wageLine.getWageType());
          }
          employeeYearWage.setTotalWageYear(employeeYearWage.getTotalWageYear()
            .add(wageLine.getGrossWage())
              .subtract(wageLine.getTaxesEmployee()));
          if (employeeYearWage.getIsNew()) {
            getSrvOrm().insertEntity(employeeYearWage);
          } else {
            getSrvOrm().updateEntity(employeeYearWage);
          }
        }
      } else {
        for (WageLine wageLine : wageLines) {
          String whereStr = " where ITSOWNER=" + pEntity.getEmployee()
            .getItsId() + " and WAGETYPE=" + wageLine.getWageType().getItsId();
          EmployeeYearWage employeeYearWage = getSrvOrm()
            .retrieveEntityWithConditions(EmployeeYearWage.class, whereStr);
          employeeYearWage.setTotalWageYear(employeeYearWage.getTotalWageYear()
            .subtract(wageLine.getGrossWage())
              .add(wageLine.getTaxesEmployee()));
          getSrvOrm().updateEntity(employeeYearWage);
        }
      }
    }
  }

  /**
   * <p>Retrieve other data of entity e.g. warehouse entries.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void retrieveOtherDataFor(final Map<String, Object> pAddParam,
    final Wage pEntity) throws Exception {
    //nothing
  }

  /**
   * <p>Additional check document for ready to account (make acc.entries).</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void addCheckIsReadyToAccount(
    final Map<String, Object> pAddParam,
      final Wage pEntity) throws Exception {
    //nothing
  }

  /**
   * <p>Check other fraud update e.g. prevent change completed unaccounted
   * manufacturing process.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pOldEntity old saved entity
   * @throws Exception - an exception
   **/
  @Override
  public final void checkOtherFraudUpdate(final Map<String, Object> pAddParam,
    final Wage pEntity,
      final Wage pOldEntity) throws Exception {
    //nothing
  }

  /**
   * <p>Make save preparations before insert/update block if it's need.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void makeFirstPrepareForSave(final Map<String, Object> pAddParam,
    final Wage pEntity) throws Exception {
    //nothing
  }

  //Simple getters and setters:
  /**
   * <p>Getter for factoryAppBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final IFactoryAppBeans getFactoryAppBeans() {
    return this.factoryAppBeans;
  }

  /**
   * <p>Setter for factoryAppBeans.</p>
   * @param pFactoryAppBeans reference
   **/
  public final void setFactoryAppBeans(
    final IFactoryAppBeans pFactoryAppBeans) {
    this.factoryAppBeans = pFactoryAppBeans;
  }
}
