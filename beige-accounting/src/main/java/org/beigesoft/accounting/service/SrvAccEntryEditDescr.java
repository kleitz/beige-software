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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.AccountingEntry;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for edit accounting entry description.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvAccEntryEditDescr<RS>
  extends ASrvAccEntityImmutable<RS, AccountingEntry> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvAccEntryEditDescr() {
    super(AccountingEntry.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvAccEntryEditDescr(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings) {
    super(AccountingEntry.class, pSrvOrm, pSrvAccSettings);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final AccountingEntry createEntity(
    final Map<String, ?> pAddParam) throws Exception {
    AccountingEntry entity = new AccountingEntry();
    entity.setIsNew(true);
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
    final AccountingEntry pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getIsNew()) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "forbidden_operation");
    } else {
      AccountingEntry oldEntity = getSrvOrm().retrieveEntity(pEntity);
      oldEntity.setDescription(pEntity.getDescription());
      getSrvOrm().updateEntity(oldEntity);
    }
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final AccountingEntry retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "forbidden_operation");
  }
}
