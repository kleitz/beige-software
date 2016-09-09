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

import org.beigesoft.holder.IAttributes;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.base.ASubaccount;
import org.beigesoft.accounting.persistable.base.ASubaccountUsed;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.persistable.SubaccountLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for Account's Subaccount Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvSubaccountLine<RS>
  extends ASrvAccEntitySimple<RS, SubaccountLine>
    implements ISrvEntityOwned<SubaccountLine, Account> {

  /**
   * <p>Type Codes of sub-accounts service.</p>
   **/
  private ISrvSubaccCode srvTypeCode;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvSubaccountLine() {
    super(SubaccountLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvTypeCodeSubacc Type Codes of sub-accounts service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvSubaccountLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvSubaccCode pSrvTypeCodeSubacc,
      final ISrvAccSettings pSrvAccSettings) {
    super(SubaccountLine.class, pSrvOrm, pSrvAccSettings);
    this.srvTypeCode = pSrvTypeCodeSubacc;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final SubaccountLine createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    SubaccountLine entity = new SubaccountLine();
    entity.setIsNew(true);
    Account itsOwner = new Account();
    entity.setItsOwner(itsOwner);
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
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
  public final SubaccountLine retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    SubaccountLine entity = getSrvOrm().retrieveCopyEntity(
      SubaccountLine.class, pId);
    entity.setIsNew(true);
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final SubaccountLine retrieveEntity(final Map<String, ?> pAddParam,
    final SubaccountLine pEntity) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final SubaccountLine retrieveEntityById(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    addTypeCodeIntoAttrs(pAddParam);
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(SubaccountLine.class, pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final SubaccountLine pEntity) throws Exception {
    getSrvOrm().deleteEntity(SubaccountLine.class, pEntity.getItsId());
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
    getSrvOrm().deleteEntity(SubaccountLine.class, pId);
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
    final SubaccountLine pEntity,
      final boolean isEntityDetached) throws Exception {
    Account itsOwner = getSrvOrm().retrieveEntityById(
      Account.class, pEntity.getItsOwner().getItsId());
    pEntity.setItsOwner(itsOwner);
    if (!pEntity.getSubaccType().equals(pEntity
      .getItsOwner().getSubaccType())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "wrong_paramaters");
    }
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
      registerSubaccountUsedIfExist(pEntity.getSubaccType(),
        pEntity.getSubaccId());
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
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
  public final SubaccountLine createEntityWithOwnerById(
    final Map<String, ?> pAddParam,
      final Object pIdOwner) throws Exception {
    SubaccountLine entity = new SubaccountLine();
    entity.setIsNew(true);
    Account itsOwner = getSrvOrm().retrieveEntityById(
      Account.class, pIdOwner);
    entity.setSubaccType(itsOwner.getSubaccType());
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return entity;
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
  public final SubaccountLine createEntityWithOwner(
    final Map<String, ?> pAddParam,
      final Account pEntityItsOwner) throws Exception {
    SubaccountLine entity = new SubaccountLine();
    entity.setIsNew(true);
    entity.setItsOwner(pEntityItsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return entity;
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
  public final List<SubaccountLine> retrieveOwnedListById(
    final Map<String, ?> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(SubaccountLine.class,
      Account.class, pIdEntityItsOwner);
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
  public final List<SubaccountLine> retrieveOwnedList(
    final Map<String, ?> pAddParam,
      final Account pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    addTypeCodeIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(SubaccountLine.class,
      Account.class, pEntityItsOwner.getItsId());
  }

  /**
   * <p>Added source types.</p>
   * @param pAddParam additional param
   */
  public final void addTypeCodeIntoAttrs(final Map<String, ?> pAddParam) {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("typeCodeSubaccMap",
      this.srvTypeCode.getTypeCodeMap());
  }

  /**
   * <p>Register subaccount as Used if it exist.</p>
   * @param pSubaccType Subacc Type
   * @param pSubaccId Subacc ID
   * @throws Exception - an exception
   */
  public final void registerSubaccountUsedIfExist(final Integer pSubaccType,
      final Long pSubaccId) throws Exception {
    if (pSubaccType != null && pSubaccId != null) {
      Class<?> subaccClass = this.srvTypeCode.getTypeCodeMap()
        .get(pSubaccType);
      if (subaccClass == null) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "subacc_class_not_found_in_map_its_type_is" + "---" + pSubaccType);
      }
      Class<?> subaccClassUsed = this.srvTypeCode.getSubaccUsedCodeMap()
        .get(pSubaccType);
      if (subaccClassUsed == null) {
        throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
          "subacc_used_class_not_found_in_map_its_type_is"
            + "---" + pSubaccType);
      }
      @SuppressWarnings("unchecked")
      ASubaccountUsed subaccUsed = (ASubaccountUsed) getSrvOrm()
        .retrieveEntityById(subaccClassUsed, pSubaccId);
      if (subaccUsed == null) {
        subaccUsed = (ASubaccountUsed) getSrvOrm()
          .createEntity(subaccClassUsed);
        @SuppressWarnings("unchecked")
        ASubaccount subaccount = (ASubaccount) getSrvOrm()
          .createEntity(subaccClass);
        subaccount.setItsId(pSubaccId);
        subaccUsed.setSubaccount(subaccount);
        getSrvOrm().insertEntity(subaccUsed);
      }
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvTypeCode.</p>
   * @return ISrvSubaccCode
   **/
  public final ISrvSubaccCode getSrvTypeCode() {
    return this.srvTypeCode;
  }

  /**
   * <p>Setter for srvTypeCode.</p>
   * @param pSrvTypeCode reference
   **/
  public final void setSrvTypeCode(final ISrvSubaccCode pSrvTypeCode) {
    this.srvTypeCode = pSrvTypeCode;
  }
}
