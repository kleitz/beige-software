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
import java.util.Map;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.AccountingEntries;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for generic accounting entries document.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvAccountingEntries<RS>
  extends ASrvAccEntityExt<RS, AccountingEntries> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvAccountingEntries() {
    super(AccountingEntries.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvAccountingEntries(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings) {
    super(AccountingEntries.class, pSrvOrm, pSrvAccSettings);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final AccountingEntries createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    AccountingEntries entity = new AccountingEntries();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsDate(new Date());
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
  public final void saveEntity(final Map<String, Object> pAddParam,
    final AccountingEntries pEntity,
      final boolean isEntityDetached) throws Exception {
    if (!pEntity.getIdDatabaseBirth()
      .equals(getSrvOrm().getIdDatabase())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can_not_change_foreign_src");
    }
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
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
  public final AccountingEntries retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    AccountingEntries entity = getSrvOrm()
      .retrieveCopyEntity(AccountingEntries.class, pId);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsDate(new Date());
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }
}
