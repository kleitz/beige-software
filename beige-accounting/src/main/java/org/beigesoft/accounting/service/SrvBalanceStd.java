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
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.EPeriod;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.persistable.BalanceAt;
import org.beigesoft.accounting.persistable.BalanceAtAllDirtyCheck;
import org.beigesoft.accounting.model.TrialBalanceLine;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.model.IRecordSet;
import org.beigesoft.log.ILogger;


/**
 * <p>Service that maintenance BalanceAt
 * and implements dirty check for all account.
 * If balance for account at given date is NULL then
 * it will be no record BalanceAt, this is cheap approach.
 * All work include recalculation all balances is executed
 * in single transaction</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvBalanceStd<RS> implements ISrvBalance {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Balance store period.</p>
   **/
  private BalanceAtAllDirtyCheck balanceAtAllDirtyCheck;

  /**
   * <p>Query balance for all accounts.</p>
   **/
  private String queryBalance;

  /**
   * <p>Query balance for an account.</p>
   **/
  private String queryBalanceAccount;

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvBalanceStd() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvAccSettings AccSettings service
   * @param pLogger reference
   **/
  public SrvBalanceStd(final ISrvOrm<RS> pSrvOrm,
      final ISrvDatabase<RS> pSrvDatabase,
        final ISrvAccSettings pSrvAccSettings, final ILogger pLogger) {
    this.logger = pLogger;
    this.srvDatabase = pSrvDatabase;
    this.srvOrm = pSrvOrm;
    this.srvAccSettings = pSrvAccSettings;
  }

  /**
   * <p>Change period of stored balances EPeriod.DAILY/WEEKLY/MONTHLY
   * and switch on "current balances are dirty".</p>
   * @param pPeriod EPeriod e.g. MONTHLY
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void changeBalanceStorePeriod(
    final EPeriod pPeriod) throws Exception {
    if (pPeriod == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "null_not_accepted");
    }
    if (!lazyGetBalanceAtAllDirtyCheck().getBalanceStorePeriod()
      .equals(pPeriod)) {
      getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
        + ": changing period from " + lazyGetBalanceAtAllDirtyCheck()
          .getBalanceStorePeriod() + " to " + pPeriod);
      lazyGetBalanceAtAllDirtyCheck().setBalanceStorePeriod(pPeriod);
      if (!getSrvAccSettings().lazyGetAccSettings()
        .getBalanceStorePeriod().equals(pPeriod)) {
        getSrvAccSettings().lazyGetAccSettings()
          .setBalanceStorePeriod(pPeriod);
        getSrvAccSettings().saveEntity(null, getSrvAccSettings()
          .lazyGetAccSettings(), false);
      }
      lazyGetBalanceAtAllDirtyCheck()
        .setCurrentBalanceDate(new Date(157766400000L));
      lazyGetBalanceAtAllDirtyCheck().setIsPeriodChanged(true);
      getSrvOrm().updateEntity(lazyGetBalanceAtAllDirtyCheck());
    }
  }

  /**
   * <p>Evaluate period of stored balances according settings,
   * if it's changed then it switch on "current balances are dirty".</p>
   * @return pPeriod EPeriod e.g. MONTHLY
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized EPeriod
    evalBalanceStorePeriod() throws Exception {
    if (!lazyGetBalanceAtAllDirtyCheck().getBalanceStorePeriod()
      .equals(getSrvAccSettings().lazyGetAccSettings()
        .getBalanceStorePeriod())) {
      getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
        + ": changing period from " + lazyGetBalanceAtAllDirtyCheck()
          .getBalanceStorePeriod() + " to " + getSrvAccSettings()
            .lazyGetAccSettings().getBalanceStorePeriod());
      lazyGetBalanceAtAllDirtyCheck()
        .setBalanceStorePeriod(getSrvAccSettings().lazyGetAccSettings()
          .getBalanceStorePeriod());
      lazyGetBalanceAtAllDirtyCheck().setIsPeriodChanged(true);
      lazyGetBalanceAtAllDirtyCheck()
        .setCurrentBalanceDate(new Date(157766400000L));
    }
    return lazyGetBalanceAtAllDirtyCheck().getBalanceStorePeriod();
  }

  /**
   * <p>Evaluate BalanceAt for given pAcc which itsDate less
   * or equals pDateFor. If required BalanceAt (and all
   * BalanceAt from start of year) is null or dirty
   * it makes it (they).</p>
   * @param pAcc account
   * @param pSubaccId subaccount ID
   * @param pDateFor date for
   * @return BalanceAt data
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized BalanceAt evalBalanceAt(final Account pAcc,
    final Long pSubaccId, final Date pDateFor) throws Exception {
    recalculateAllIfNeed(pDateFor);
    //TODO
    return null;
  }

  /**
   * <p>Handle new accounting entry is created to check dirty.
   * This is implementation of dirty check for all accounts.</p>
   * @param pAcc account
   * @param pSubaccId subaccount ID
   * @param pDateAt date at
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void handleNewAccountEntry(final Account pAcc,
    final Long pSubaccId, final Date pDateAt) throws Exception {
    if (lazyGetBalanceAtAllDirtyCheck().getLeastAccountingEntryDate()
      .getTime() > pDateAt.getTime()) {
      getLogger().debug(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
        + ": changing least last entry date from "
          + lazyGetBalanceAtAllDirtyCheck().getLeastAccountingEntryDate()
           + " to " + pDateAt);
      lazyGetBalanceAtAllDirtyCheck().setLeastAccountingEntryDate(pDateAt);
    }
  }

  /**
   * <p>Recalculate if need for all balances for all dates less
   * or equals pDateFor, this method is always invoked by report ledger.</p>
   * @param pDateFor date for
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void recalculateAllIfNeed(
    final Date pDateFor) throws Exception {
    evalBalanceStorePeriod(); //must be before evalDateBalanceStoreStart!!!
    evalDateBalanceStoreStart();
    Date datePeriodStartFor = evalDatePeriodStartFor(pDateFor);
    if (datePeriodStartFor.getTime() > lazyGetBalanceAtAllDirtyCheck()
      .getCurrentBalanceDate().getTime() || lazyGetBalanceAtAllDirtyCheck()
        .getLeastAccountingEntryDate()
          .getTime() < lazyGetBalanceAtAllDirtyCheck()
            .getCurrentBalanceDate().getTime()) {
      recalculateAll(pDateFor, false);
    }
  }

  /**
   * <p>Forced recalculation all stored balances for this account
   * for all dates less or equals pDateFor. This method usually invoked
   * by account subaccount line service when subaccount is added.</p>
   * @param pAcc account
   * @param pSubaccId subaccount ID
   * @param pDateFor date for
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void recalculateFor(final Account pAcc,
    final Long pSubaccId, final Date pDateFor) throws Exception {
    //this implementation does nothing.
  }

  /**
   * <p>Forced recalculation all balances for all dates less
   * or equals pDateFor. If balance for account at given date is NULL then
   * it will be no recorded into BalanceAt, this is cheap approach.</p>
   * @param pDateFor date for
   * @param pIsPrepareNeed if need evaluation store period/start of store
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void recalculateAll(
    final Date pDateFor, final boolean pIsPrepareNeed) throws Exception {
    getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
      + ": recalculation start BalanceAtAllDirtyCheck was "
        + lazyGetBalanceAtAllDirtyCheck());
    if (pIsPrepareNeed) {
      evalBalanceStorePeriod(); //must be before evalDateBalanceStoreStart!!!
      evalDateBalanceStoreStart();
    }
    if (lazyGetBalanceAtAllDirtyCheck().getIsPeriodChanged()) {
      getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
        + ": deleting all stored balances cause period has changed");
      getSrvDatabase().executeDelete(BalanceAt.class.getSimpleName()
        .toUpperCase(), null);
      lazyGetBalanceAtAllDirtyCheck().setIsPeriodChanged(false);
    }
    Date date;
    if (lazyGetBalanceAtAllDirtyCheck().getLeastAccountingEntryDate()
          .getTime() < lazyGetBalanceAtAllDirtyCheck()
            .getCurrentBalanceDate().getTime()) {
      //recalculate from start;
      date = evalDateNextPeriodStart(lazyGetBalanceAtAllDirtyCheck()
        .getDateBalanceStoreStart());
      getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
        + ": recalculating balances from start " + date + " <- "
          + lazyGetBalanceAtAllDirtyCheck().getDateBalanceStoreStart());
    } else {
      //recalculate from current end;
      date = evalDateNextPeriodStart(lazyGetBalanceAtAllDirtyCheck()
        .getCurrentBalanceDate());
      getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
        + ": recalculating balances from current end " + date + " <- "
          + lazyGetBalanceAtAllDirtyCheck().getCurrentBalanceDate());
    }
    Date lastBalanceStoredDate = date;
    do {
      String query = evalQueryBalance(new Date(date.getTime() - 1));
      List<TrialBalanceLine> tbls = retrieveBalanceLinesForStore(query);
      for (TrialBalanceLine tbl : tbls) {
        lastBalanceStoredDate = date;
        String subAccWhereStr;
        if (tbl.getSubaccId() == null) {
          subAccWhereStr =
            " and SUBACCID is null and BALANCEAT.SUBACCTYPE is null";
        } else {
          subAccWhereStr = " and SUBACCID=" + tbl.getSubaccId()
            + " and BALANCEAT.SUBACCTYPE=" + tbl.getSubaccType();
        }
        BalanceAt balanceAt = getSrvOrm().retrieveEntityWithConditions(
          BalanceAt.class, "where ITSACCOUNT='" + tbl.getAccId()
            + "' and ITSDATE=" + date.getTime() + subAccWhereStr);
        if (balanceAt == null) {
          balanceAt = new BalanceAt();
          balanceAt.setIsNew(true);
        }
        balanceAt.setItsDate(date);
        Account acc = new Account();
        acc.setItsId(tbl.getAccId());
        balanceAt.setItsAccount(acc);
        if (tbl.getDebit().doubleValue() != 0) {
          balanceAt.setItsBalance(tbl.getDebit());
        } else {
          balanceAt.setItsBalance(tbl.getCredit());
        }
        balanceAt.setSubaccType(tbl.getSubaccType());
        balanceAt.setSubaccId(tbl.getSubaccId());
        balanceAt.setSubaccount(tbl.getSubaccName());
        if (balanceAt.getIsNew()) {
          getSrvOrm().insertEntity(balanceAt);
        } else {
          getSrvOrm().updateEntity(balanceAt);
        }
      }
      date = evalDateNextPeriodStart(date);
    } while (date.getTime() <= pDateFor.getTime());
    getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
      + ": last stored balance date " + lastBalanceStoredDate + ", date for "
        + pDateFor);
    if (lastBalanceStoredDate.getTime() > pDateFor.getTime()) {
      lazyGetBalanceAtAllDirtyCheck()
        .setCurrentBalanceDate(lastBalanceStoredDate);
    } else {
      lazyGetBalanceAtAllDirtyCheck().setCurrentBalanceDate(pDateFor);
    }
      lazyGetBalanceAtAllDirtyCheck()
        .setLeastAccountingEntryDate(lazyGetBalanceAtAllDirtyCheck()
          .getCurrentBalanceDate());
    getSrvOrm().updateEntity(lazyGetBalanceAtAllDirtyCheck());
    getLogger().info(SrvBalanceStd.class, SrvBalanceStd.class.getSimpleName()
      + ": recalculation end BalanceAtAllDirtyCheck is "
        + lazyGetBalanceAtAllDirtyCheck());
  }

  /**
   * <p>Retrieve Trial Balance.</p>
   * @param pAddParam additional param
   * @param pDate date
   * @return balance lines
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized List<TrialBalanceLine> retrieveTrialBalance(
    final Map<String, Object> pAddParam,
      final Date pDate) throws Exception {
    recalculateAllIfNeed(pDate);
    List<TrialBalanceLine> result = new ArrayList<TrialBalanceLine>();
    String query = evalQueryBalance(pDate);
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(query);
      if (recordSet.moveToFirst()) {
        do {
          String accName = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ITSNAME");
          String accNumber = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ITSNUMBER");
          String subaccName = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "SUBACC");
          Double debit = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "DEBIT");
          Double credit = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "CREDIT");
          if (debit != 0 || credit != 0) {
            TrialBalanceLine tbl = new TrialBalanceLine();
            tbl.setAccName(accName);
            tbl.setAccNumber(accNumber);
            tbl.setSubaccName(subaccName);
            tbl.setDebit(BigDecimal.valueOf(debit).setScale(
              getSrvAccSettings().lazyGetAccSettings().getBalancePrecision(),
                getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
            tbl.setCredit(BigDecimal.valueOf(credit).setScale(
              getSrvAccSettings().lazyGetAccSettings().getBalancePrecision(),
                getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
            if (tbl.getDebit().doubleValue() != 0
              || tbl.getCredit().doubleValue() != 0) {
              result.add(tbl);
            }
          }
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    //account totals:
    BigDecimal debitAcc = BigDecimal.ZERO;
    BigDecimal creditAcc = BigDecimal.ZERO;
    String accCurr = null;
    int lineCurr = 0;
    int lineStartAcc = 0;
    for (TrialBalanceLine tbl : result) {
      if (!tbl.getAccNumber().equals(accCurr)) {
        //save to old
        if (accCurr != null) {
          for (int j = lineStartAcc; j < lineCurr; j++) {
            result.get(j).setDebitAcc(debitAcc);
            result.get(j).setCreditAcc(creditAcc);
          }
        }
        //init new acc:
        lineStartAcc = lineCurr;
        accCurr = tbl.getAccNumber();
      }
      debitAcc = debitAcc.add(tbl.getDebit());
      creditAcc = creditAcc.add(tbl.getCredit());
      lineCurr++;
    }
    return result;
  }

  /**
   * <p>Evaluate start of period nearest to pDateFor.
   * Tested in beige-common org.beigesoft.test.CalendarTest.</p>
   * @param pDateFor date for
   * @return Start of period nearest to pDateFor
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized Date evalDatePeriodStartFor(
    final Date pDateFor) throws Exception {
    if (!(evalBalanceStorePeriod().equals(EPeriod.MONTHLY)
      || evalBalanceStorePeriod().equals(EPeriod.WEEKLY)
        || evalBalanceStorePeriod().equals(EPeriod.DAILY))) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "stored_balance_period_must_be_dwm");
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0); //Daily is ready
    if (evalBalanceStorePeriod().equals(EPeriod.MONTHLY)) {
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (evalBalanceStorePeriod().equals(EPeriod.WEEKLY)) {
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }

  /**
   * <p>Evaluate date start of stored balances according settings,
   * this is the first month of the first accounting entry or start of current
   * year if there are no any acc-entry.</p>
   * @return Date
   * @throws Exception - an exception
   **/
  public final synchronized Date evalDateBalanceStoreStart() throws Exception {
    Date dateBalanceStoreStart = lazyGetBalanceAtAllDirtyCheck()
      .getDateBalanceStoreStart();
    Date leastAccountingEntryDate = lazyGetBalanceAtAllDirtyCheck()
      .getLeastAccountingEntryDate();
    if (dateBalanceStoreStart.getTime() == 157766400000L
      && leastAccountingEntryDate.getTime() == 157766400000L) {
      //the first time with no acc-entries, it's start of current year:
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.set(Calendar.MONTH, 0);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      lazyGetBalanceAtAllDirtyCheck().setDateBalanceStoreStart(cal.getTime());
    } else if (dateBalanceStoreStart.getTime() == 157766400000L
      && leastAccountingEntryDate.getTime() > 157766400000L) {
      //the first time with acc-entries, it's start nearest period to least:
      lazyGetBalanceAtAllDirtyCheck().setDateBalanceStoreStart(
        evalDatePeriodStartFor(leastAccountingEntryDate));
    }
    return lazyGetBalanceAtAllDirtyCheck().getDateBalanceStoreStart();
   }

  /**
   * <p>Evaluate Trial Balance query.</p>
   * @param pDate date of balance
   * @return query of balance
   * @throws Exception - an exception
   **/
  public final synchronized String evalQueryBalance(
    final Date pDate) throws Exception {
    if (this.queryBalance == null) {
      String flName = "/" + "accounting" + "/" + "balance"
        + "/" + "queryBalance.sql";
      this.queryBalance = loadString(flName);
    }
    String query = queryBalance.replace(":DATE1",
      String.valueOf(evalDatePeriodStartFor(pDate).getTime()));
    query = query.replace(":DATE2", String.valueOf(pDate.getTime()));
    return query;
  }

  /**
   * <p>Retrieve Trial Balance lines with given query and precision cost.</p>
   * @param pQuery date
   * @return balance lines
   * @throws Exception - an exception
   **/
  public final synchronized List<TrialBalanceLine> retrieveBalanceLines(
      final String pQuery) throws Exception {
    List<TrialBalanceLine> result = new ArrayList<TrialBalanceLine>();
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        do {
          String accName = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ITSNAME");
          String accId = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ACCID");
          Long subaccId = getSrvDatabase().getSrvRecordRetriever()
            .getLong(recordSet.getRecordSet(), "SUBACCID");
          Integer subaccType = getSrvDatabase().getSrvRecordRetriever()
            .getInteger(recordSet.getRecordSet(), "SUBACCTYPE");
          String accNumber = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ITSNUMBER");
          String subaccName = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "SUBACC");
          Double debit = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "DEBIT");
          Double credit = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "CREDIT");
          if (debit != 0 || credit != 0) {
            TrialBalanceLine tbl = new TrialBalanceLine();
            tbl.setAccId(accId);
            tbl.setSubaccId(subaccId);
            tbl.setSubaccType(subaccType);
            tbl.setAccName(accName);
            tbl.setAccNumber(accNumber);
            tbl.setSubaccName(subaccName);
            tbl.setDebit(BigDecimal.valueOf(debit).setScale(
              getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
                getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
            tbl.setCredit(BigDecimal.valueOf(credit).setScale(
              getSrvAccSettings().lazyGetAccSettings().getCostPrecision(),
                getSrvAccSettings().lazyGetAccSettings().getRoundingMode()));
            if (tbl.getDebit().doubleValue() != 0
              || tbl.getCredit().doubleValue() != 0) {
              result.add(tbl);
            }
          }
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Retrieve Trial Balance lines with given query for store.</p>
   * @param pQuery date
   * @return balance lines
   * @throws Exception - an exception
   **/
  public final synchronized List<TrialBalanceLine> retrieveBalanceLinesForStore(
      final String pQuery) throws Exception {
    List<TrialBalanceLine> result = new ArrayList<TrialBalanceLine>();
    IRecordSet<RS> recordSet = null;
    try {
      recordSet = getSrvDatabase().retrieveRecords(pQuery);
      if (recordSet.moveToFirst()) {
        do {
          String accName = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ITSNAME");
          String accId = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ACCID");
          Long subaccId = getSrvDatabase().getSrvRecordRetriever()
            .getLong(recordSet.getRecordSet(), "SUBACCID");
          Integer subaccType = getSrvDatabase().getSrvRecordRetriever()
            .getInteger(recordSet.getRecordSet(), "SUBACCTYPE");
          String accNumber = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "ITSNUMBER");
          String subaccName = getSrvDatabase().getSrvRecordRetriever()
            .getString(recordSet.getRecordSet(), "SUBACC");
          Double debit = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "DEBIT");
          Double credit = getSrvDatabase().getSrvRecordRetriever()
            .getDouble(recordSet.getRecordSet(), "CREDIT");
          TrialBalanceLine tbl = new TrialBalanceLine();
          tbl.setAccId(accId);
          tbl.setSubaccId(subaccId);
          tbl.setSubaccType(subaccType);
          tbl.setAccName(accName);
          tbl.setAccNumber(accNumber);
          tbl.setSubaccName(subaccName);
          tbl.setDebit(BigDecimal.valueOf(debit));
          tbl.setCredit(BigDecimal.valueOf(credit));
          result.add(tbl);
        } while (recordSet.moveToNext());
      }
    } finally {
      if (recordSet != null) {
        recordSet.close();
      }
    }
    return result;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String loadString(final String pFileName)
        throws IOException {
    URL urlFile = SrvBalanceStd.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrvBalanceStd.class.getResourceAsStream(pFileName);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return null;
  }

  /**
   * <p>Lazy getter for balanceAtAllDirtyCheck.</p>
   * @return BalanceAtAllDirtyCheck
   * @throws Exception - an exception
   **/
  public final synchronized BalanceAtAllDirtyCheck
    lazyGetBalanceAtAllDirtyCheck() throws Exception {
    if (this.balanceAtAllDirtyCheck == null) {
      this.balanceAtAllDirtyCheck = getSrvOrm()
        .retrieveEntityById(BalanceAtAllDirtyCheck.class, 1L);
      if (this.balanceAtAllDirtyCheck == null) {
        this.balanceAtAllDirtyCheck = new BalanceAtAllDirtyCheck();
        this.balanceAtAllDirtyCheck.setItsId(1L);
        getSrvOrm().insertEntity(this.balanceAtAllDirtyCheck);
      }
    }
    return this.balanceAtAllDirtyCheck;
  }

  /**
   * <p>Evaluate date start of next balance store period.
   * Tested in beige-common org.beigesoft.test.CalendarTest.</p>
   * @param pDateFor date for
   * @return Start of next period nearest to pDateFor
   * @throws Exception - an exception
   **/
  public final synchronized Date evalDateNextPeriodStart(
    final Date pDateFor) throws Exception {
    if (!(evalBalanceStorePeriod().equals(EPeriod.MONTHLY)
      || evalBalanceStorePeriod().equals(EPeriod.WEEKLY)
        || evalBalanceStorePeriod().equals(EPeriod.DAILY))) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "stored_balance_period_must_be_dwm");
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    if (evalBalanceStorePeriod().equals(EPeriod.DAILY)) {
      cal.add(Calendar.DATE, 1);
    } else if (evalBalanceStorePeriod().equals(EPeriod.MONTHLY)) {
      cal.add(Calendar.MONTH, 1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (evalBalanceStorePeriod().equals(EPeriod.WEEKLY)) {
      cal.add(Calendar.DAY_OF_YEAR, 7);
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }


  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final synchronized ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final synchronized void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase
   **/
  public final synchronized ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final synchronized void setSrvDatabase(
    final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final synchronized ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final synchronized void setSrvAccSettings(
    final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }

  /**
   * <p>Getter for queryBalance.</p>
   * @return String
   **/
  public final synchronized String getQueryBalance() {
    return this.queryBalance;
  }

  /**
   * <p>Setter for queryBalance.</p>
   * @param pQueryBalance reference
   **/
  public final synchronized void setQueryBalance(final String pQueryBalance) {
    this.queryBalance = pQueryBalance;
  }

  /**
   * <p>Getter for queryBalanceAccount.</p>
   * @return String
   **/
  public final synchronized String getQueryBalanceAccount() {
    return this.queryBalanceAccount;
  }

  /**
   * <p>Setter for queryBalanceAccount.</p>
   * @param pQueryBalanceAccount reference
   **/
  public final synchronized void
    setQueryBalanceAccount(final String pQueryBalanceAccount) {
    this.queryBalanceAccount = pQueryBalanceAccount;
  }

  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final synchronized ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final synchronized void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }
}
