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

import java.util.Map;
import java.util.List;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.ISrvEntity;
import org.beigesoft.holder.IAttributes;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.persistable.SubaccountLine;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for account.</p>
 *
 * @author Yury Demidenko
 */
public class SrvAccount implements ISrvEntity<Account> {

  /**
   * <p>Entity class.</p>
   **/
  private final Class<Account> entityClass;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<?> srvOrm;

  /**
   * <p>Type Codes of sub-accounts service.</p>
   **/
  private ISrvTypeCode srvTypeCode;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvAccount() {
    this.entityClass = Account.class;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvTypeCodeSubacc Type Codes of sub-accounts service
   **/
  public SrvAccount(final ISrvOrm<?> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvTypeCode pSrvTypeCodeSubacc) {
    this();
    this.srvAccSettings = pSrvAccSettings;
    this.srvOrm = pSrvOrm;
    this.srvTypeCode = pSrvTypeCodeSubacc;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final Account createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    Account entity = new Account();
    addTypeCodeIntoAttrs(pAddParam);
    entity.setIsNew(true);
    return entity;
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final Account retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    Account entity = getSrvOrm()
      .retrieveCopyEntity(Account.class, pId);
    entity.setIsNew(true);
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, ?> pAddParam,
    final Account pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      Account oldAcc = getSrvOrm().retrieveEntityById(Account.class,
        pEntity.getItsId());
      if (!oldAcc.getIsCreatedByUser()
        && (!pEntity.getItsType().equals(oldAcc.getItsType()) || !pEntity
          .getNormalBalanceType().equals(oldAcc.getNormalBalanceType()))) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "account_cant_be_changed");
      }
      //BeigeORM refresh:
      pEntity.setSubaccounts(getSrvOrm()
        .retrieveEntityOwnedlist(SubaccountLine.class, pEntity));
      if (pEntity.getSubaccType() == null
        && pEntity.getSubaccounts().size() > 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "wrong_paramaters");
      }
      if (pEntity.getSubaccType() != null && pEntity.getSubaccounts()
        .size() > 0 && !pEntity.getSubaccType().equals(pEntity
        .getSubaccounts().get(0).getSubaccType())) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "wrong_paramaters");
      }
      getSrvOrm().updateEntity(pEntity);
    }
  }


  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final Account retrieveEntity(final Map<String, ?> pAddParam,
    final Account pEntity) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(this.entityClass, pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final Account retrieveEntityById(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(this.entityClass, pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final Account pEntity) throws Exception {
    deleteEntity(pAddParam, pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
      Account oldAcc = getSrvOrm().retrieveEntityById(Account.class, pId);
    if (!oldAcc.getIsCreatedByUser()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "account_cant_be_changed");
    }
    getSrvOrm().deleteEntity(getEntityClass(), pId);
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param pAddParam additional param
   * @return list of all business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<Account> retrieveList(
    final Map<String, ?> pAddParam) throws Exception {
    return getSrvOrm().retrieveList(this.entityClass);
  }

  /**
   * <p>Retrieve a list of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<Account> retrieveListWithConditions(
    final Map<String, ?> pAddParam,
      final String pQueryConditions) throws Exception {
    return getSrvOrm().retrieveListWithConditions(this.entityClass,
      pQueryConditions);
  }

  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<Account> retrievePage(
    final Map<String, ?> pAddParam,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrievePage(this.entityClass, pFirst, pPageSize);
  }
  /**
   * <p>Retrieve a page of entities.</p>
   * @param pAddParam additional param
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<Account> retrievePageWithConditions(
    final Map<String, ?> pAddParam,
      final String pQueryConditions,
        final Integer pFirst, final Integer pPageSize) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrievePageWithConditions(this.entityClass,
      pQueryConditions, pFirst, pPageSize);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCount(
    final Map<String, ?> pAddParam) throws Exception {
    return getSrvOrm().evalRowCount(this.entityClass);
  }

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param pAddParam additional param
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final Integer evalRowCountWhere(final Map<String, ?> pAddParam,
    final String pWhere) throws Exception {
    return getSrvOrm().evalRowCountWhere(this.entityClass, pWhere);
  }

  /**
   * <p>Added source types.</p>
   * @param pAddParam additional param
   */
  public final void addTypeCodeIntoAttrs(final Map<String, ?> pAddParam) {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("typeCodeSubaccMap", srvTypeCode.getTypeCodeMap());
  }

  /**
   * <p>Added accounting settings to attributes.</p>
   * @param pAddParam additional param
   * @throws Exception - an exception
   */
  public final void addAccSettingsIntoAttrs(
    final Map<String, ?> pAddParam) throws Exception {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("accSettings", srvAccSettings.lazyGetAccSettings());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for entityClass.</p>
   * @return Class<T>
   **/
  public final Class<Account> getEntityClass() {
    return this.entityClass;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<?>
   **/
  public final ISrvOrm<?> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<?> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for srvTypeCode.</p>
   * @return ISrvTypeCode
   **/
  public final ISrvTypeCode getSrvTypeCode() {
    return this.srvTypeCode;
  }

  /**
   * <p>Setter for srvTypeCode.</p>
   * @param pSrvTypeCode reference
   **/
  public final void setSrvTypeCode(final ISrvTypeCode pSrvTypeCode) {
    this.srvTypeCode = pSrvTypeCode;
  }

  /**
   * <p>Geter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final void setSrvAccSettings(final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }
}
