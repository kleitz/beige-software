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
import java.util.Calendar;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DateFormat;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.AccountingEntries;
import org.beigesoft.accounting.persistable.AccountingEntry;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for accounting entry that made "by hand".</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvAccountingEntriesLine<RS>
  extends ASrvAccEntitySimple<RS, AccountingEntry>
    implements ISrvEntityOwned<AccountingEntry, AccountingEntries> {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Balance service.</p>
   **/
  private ISrvBalance srvBalance;

  /**
   * <p>I18N service.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>Date Formatter.</p>
   **/
  private DateFormat dateFormatter;

  /**
   * <p>AccountingEntries type code.</p>
   **/
  private final Integer accountingEntriesTypeCode =
    new AccountingEntries().constTypeCode();

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvAccountingEntriesLine() {
    super(AccountingEntry.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvBalance Balance service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
  **/
  public SrvAccountingEntriesLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvAccSettings pSrvAccSettings,
      final ISrvBalance pSrvBalance, final ISrvI18n pSrvI18n,
        final DateFormat pDateFormatter) {
    super(AccountingEntry.class, pSrvOrm, pSrvAccSettings);
    this.srvBalance = pSrvBalance;
    this.srvDatabase = pSrvDatabase;
    this.srvI18n = pSrvI18n;
    this.dateFormatter = pDateFormatter;
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final AccountingEntry createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    AccountingEntry entity = new AccountingEntry();
    entity.setIdDatabaseBirth(getSrvDatabase().getIdDatabase());
    entity.setSourceDatabaseBirth(getSrvDatabase().getIdDatabase());
    entity.setIsNew(true);
    entity.setItsDate(new Date());
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
  public final AccountingEntry retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    AccountingEntry entity = getSrvOrm().retrieveCopyEntity(
      AccountingEntry.class, pId);
    if (!entity.getIdDatabaseBirth()
      .equals(getSrvOrm().getIdDatabase())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can_not_make_entry_for_foreign_src");
    }
    entity.setIdDatabaseBirth(getSrvDatabase().getIdDatabase());
    entity.setSourceDatabaseBirth(getSrvDatabase().getIdDatabase());
    entity.setIsNew(true);
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
  public final AccountingEntry retrieveEntity(
    final Map<String, Object> pAddParam,
      final AccountingEntry pEntity) throws Exception {
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
  public final AccountingEntry retrieveEntityById(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final AccountingEntry pEntity) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "delete_not_allowed::" + pAddParam.get("user"));
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
      "delete_not_allowed::" + pAddParam.get("user"));
  }

  /**
   * <p>Save line into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, Object> pAddParam,
    final AccountingEntry pEntity,
      final boolean isEntityDetached) throws Exception {
    if (!pEntity.getIdDatabaseBirth()
      .equals(getSrvOrm().getIdDatabase())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can_not_make_entry_for_foreign_src");
    }
    Calendar calCurrYear = Calendar.getInstance();
    calCurrYear.setTime(getSrvAccSettings().lazyGetAccSettings()
      .getCurrentAccYear());
    calCurrYear.set(Calendar.MONTH, 0);
    calCurrYear.set(Calendar.DAY_OF_MONTH, 1);
    calCurrYear.set(Calendar.HOUR_OF_DAY, 0);
    calCurrYear.set(Calendar.MINUTE, 0);
    calCurrYear.set(Calendar.SECOND, 0);
    calCurrYear.set(Calendar.MILLISECOND, 0);
    Calendar calDoc = Calendar.getInstance();
    calDoc.setTime(pEntity.getItsDate());
    calDoc.set(Calendar.MONTH, 0);
    calDoc.set(Calendar.DAY_OF_MONTH, 1);
    calDoc.set(Calendar.HOUR_OF_DAY, 0);
    calDoc.set(Calendar.MINUTE, 0);
    calDoc.set(Calendar.SECOND, 0);
    calDoc.set(Calendar.MILLISECOND, 0);
    if (calCurrYear.getTime().getTime() != calDoc.getTime().getTime()) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "wrong_year");
    }
    if (!this.accountingEntriesTypeCode.equals(pEntity.getSourceType())) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "attempt_to_work_with_accounting_entry_with_wrong_source_type::"
          + pEntity.getSourceType() + ", user-" + pAddParam.get("user"));
    }
    if (pEntity.getIsNew()) {
      if (pEntity.getDebit().doubleValue() == 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "amount_eq_zero");
      }
      if (pEntity.getDebit().doubleValue() != 0 && pEntity.getAccDebit() == null
        && pEntity.getAccCredit() == null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "account_is_null");
      }
      if (pEntity.getAccDebit() != null
        && pEntity.getAccDebit().getItsId().equals("Inventory")) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "account_must_not_be_inventory");
      }
      if (pEntity.getAccCredit() != null
        && pEntity.getAccCredit().getItsId().equals("Inventory")) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "account_must_not_be_inventory");
      }
      if (pEntity.getAccCredit() != null) {
        pEntity.setCredit(pEntity.getDebit());
        //BeigeORM refresh:
        pEntity.setAccCredit(getSrvOrm()
          .retrieveEntity(pEntity.getAccCredit()));
        if (pEntity.getAccCredit().getSubaccType() != null
          && pEntity.getSubaccCreditId() == null) {
          throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
            "select_subaccount");
        }
      }
      if (pEntity.getAccDebit() == null) {
        pEntity.setDebit(BigDecimal.ZERO);
      } else {
        //BeigeORM refresh:
        pEntity.setAccDebit(getSrvOrm()
          .retrieveEntity(pEntity.getAccDebit()));
        if (pEntity.getAccDebit().getSubaccType() != null
          && pEntity.getSubaccDebitId() == null) {
          throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
            "select_subaccount");
        }
      }
      getSrvOrm().insertEntity(pEntity);
      AccountingEntries itsOwner = getSrvOrm().retrieveEntityById(
        AccountingEntries.class, pEntity.getSourceId());
      String query =
        "select sum(DEBIT) as DEBIT, sum(CREDIT) as CREDIT from "
        + "ACCOUNTINGENTRY where SOURCETYPE=" + this.accountingEntriesTypeCode
        + " and SOURCEID=" + itsOwner.getItsId();
      String[] columns = new String[]{"DEBIT", "CREDIT"};
      Double[] totals = getSrvDatabase().evalDoubleResults(query, columns);
      itsOwner.setTotalDebit(BigDecimal.valueOf(totals[0]).setScale(
        getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      itsOwner.setTotalCredit(BigDecimal.valueOf(totals[1]).setScale(
        getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
          getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
      getSrvOrm().updateEntity(itsOwner);
      getSrvBalance().handleNewAccountEntry(null, null,
        pEntity.getItsDate()); //This is for SrvBalanceStd only!!!
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "edit_not_allowed::" + pAddParam.get("user"));
    }
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdOwner entity itsOwner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final AccountingEntry createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdOwner) throws Exception {
    AccountingEntries doc = getSrvOrm().retrieveEntityById(
      AccountingEntries.class, pIdOwner);
    if (!doc.getIdDatabaseBirth()
      .equals(getSrvOrm().getIdDatabase())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can_not_make_entry_for_foreign_src");
    }
    AccountingEntry entity = new AccountingEntry();
    entity.setIdDatabaseBirth(getSrvDatabase().getIdDatabase());
    entity.setSourceDatabaseBirth(getSrvDatabase().getIdDatabase());
    entity.setIsNew(true);
    entity.setItsDate(new Date());
    entity.setSourceId(Long.valueOf(pIdOwner.toString()));
    entity.setSourceType(this.accountingEntriesTypeCode);
    entity.setDescription(getSrvI18n().getMsg(AccountingEntries.class
      .getSimpleName() + "short") + " #" + doc.getIdDatabaseBirth() + "-"
        + doc.getItsId() + ", " + getDateFormatter().format(doc.getItsDate())
          + ". " + doc.getDescription()); //only local allowed
    addAccSettingsIntoAttrs(pAddParam);
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
  public final AccountingEntry createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final AccountingEntries pEntityItsOwner) throws Exception {
    if (!pEntityItsOwner.getIdDatabaseBirth()
      .equals(getSrvOrm().getIdDatabase())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can_not_make_entry_for_foreign_src");
    }
    AccountingEntry entity = new AccountingEntry();
    entity.setIsNew(true);
    entity.setItsDate(new Date());
    entity.setSourceId(pEntityItsOwner.getItsId());
    entity.setSourceType(this.accountingEntriesTypeCode);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdOwner ID itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<AccountingEntry> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdOwner) throws Exception {
    String where = "where SOURCETYPE=" + this.accountingEntriesTypeCode
      + " and SOURCEID=" + pIdOwner;
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveListWithConditions(AccountingEntry.class,
      where);
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
  public final List<AccountingEntry> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final AccountingEntries pEntityItsOwner) throws Exception {
    return retrieveOwnedListById(pAddParam, pEntityItsOwner.getItsId());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for accountingEntriesTypeCode.</p>
   * @return Integer
   **/
  public final Integer getAccountingEntriesTypeCode() {
    return this.accountingEntriesTypeCode;
  }

  /**
   * <p>Getter for srvI18n.</p>
   * @return ISrvI18n
   **/
  public final ISrvI18n getSrvI18n() {
    return this.srvI18n;
  }

  /**
   * <p>Setter for srvI18n.</p>
   * @param pSrvI18n reference
   **/
  public final void setSrvI18n(final ISrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }

  /**
   * <p>Getter for dateFormatter.</p>
   * @return DateFormat
   **/
  public final DateFormat getDateFormatter() {
    return this.dateFormatter;
  }

  /**
   * <p>Setter for dateFormatter.</p>
   * @param pDateFormatter reference
   **/
  public final void setDateFormatter(final DateFormat pDateFormatter) {
    this.dateFormatter = pDateFormatter;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Getter for srvBalance.</p>
   * @return ISrvBalance
   **/
  public final ISrvBalance getSrvBalance() {
    return this.srvBalance;
  }

  /**
   * <p>Setter for srvBalance.</p>
   * @param pSrvBalance reference
   **/
  public final void setSrvBalance(final ISrvBalance pSrvBalance) {
    this.srvBalance = pSrvBalance;
  }
}
