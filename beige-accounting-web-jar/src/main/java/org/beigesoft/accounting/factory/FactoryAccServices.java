package org.beigesoft.accounting.factory;

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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.text.DateFormat;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.web.factory.AFactoryAppBeans;
import org.beigesoft.accounting.persistable.Account;
import org.beigesoft.accounting.persistable.SubaccountLine;
import org.beigesoft.accounting.persistable.InvItemTaxCategory;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.accounting.persistable.PurchaseInvoice;
import org.beigesoft.accounting.persistable.PurchaseInvoiceLine;
import org.beigesoft.accounting.persistable.PurchaseInvoiceServiceLine;
import org.beigesoft.accounting.persistable.PurchaseInvoiceTaxLine;
import org.beigesoft.accounting.service.SrvPurchaseInvoice;
import org.beigesoft.accounting.service.SrvPurchaseInvoiceLine;
import org.beigesoft.accounting.service.SrvPurchaseInvoiceServiceLine;
import org.beigesoft.accounting.service.UtlPurchaseGoodsServiceLine;
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.SalesInvoiceServiceLine;
import org.beigesoft.accounting.persistable.SalesInvoiceLine;
import org.beigesoft.accounting.persistable.SalesInvoiceTaxLine;
import org.beigesoft.accounting.service.SrvSalesInvoice;
import org.beigesoft.accounting.service.SrvSalesInvoiceLine;
import org.beigesoft.accounting.service.SrvSalesInvoiceServiceLine;
import org.beigesoft.accounting.service.UtlSalesGoodsServiceLine;
import org.beigesoft.accounting.service.SrvInvItemTaxCategoryLine;
import org.beigesoft.accounting.persistable.AccountingEntries;
import org.beigesoft.accounting.persistable.AccountingEntry;
import org.beigesoft.accounting.service.SrvAccount;
import org.beigesoft.accounting.service.SrvSubaccountLine;
import org.beigesoft.accounting.service.SrvAccountingEntries;
import org.beigesoft.accounting.service.SrvAccountingEntriesLine;
import org.beigesoft.accounting.persistable.Manufacture;
import org.beigesoft.accounting.service.SrvManufacture;
import org.beigesoft.accounting.persistable.WageTaxLine;
import org.beigesoft.accounting.persistable.Wage;
import org.beigesoft.accounting.persistable.WageLine;
import org.beigesoft.accounting.service.SrvWage;
import org.beigesoft.accounting.service.SrvWageLine;
import org.beigesoft.accounting.service.SrvWageTaxLine;
import org.beigesoft.accounting.service.SrvWarehouseEntry;
import org.beigesoft.accounting.service.SrvBalanceStd;
import org.beigesoft.accounting.service.SrvLedger;
import org.beigesoft.accounting.persistable.ManufacturingProcess;
import org.beigesoft.accounting.persistable.UsedMaterialLine;
import org.beigesoft.accounting.persistable.AdditionCostLine;
import org.beigesoft.accounting.service.SrvManufacturingProcess;
import org.beigesoft.accounting.service.SrvUsedMaterialLine;
import org.beigesoft.accounting.service.SrvAdditionCostLine;
import org.beigesoft.accounting.service.SrvUseMaterialEntry;
import org.beigesoft.accounting.service.SrvCogsEntry;
import org.beigesoft.accounting.persistable.AccSettings;
import org.beigesoft.accounting.persistable.AccEntriesSourcesLine;
import org.beigesoft.accounting.persistable.CogsItemSourcesLine;
import org.beigesoft.accounting.persistable.DrawMaterialSourcesLine;
import org.beigesoft.accounting.service.SrvAccSettings;
import org.beigesoft.accounting.service.SrvAccSettingsLine;
import org.beigesoft.accounting.service.SrvAccEntitySimple;
import org.beigesoft.accounting.service.SrvAccEntityOwnedSimple;
import org.beigesoft.accounting.service.SrvTypeCodeSubacc;
import org.beigesoft.accounting.service.SrvTypeCodeAccSources;
import org.beigesoft.accounting.service.SrvAccEntry;
import org.beigesoft.accounting.service.SrvWageTaxPercentageTable;
import org.beigesoft.accounting.persistable.PrepaymentTo;
import org.beigesoft.accounting.service.SrvPrepaymentTo;
import org.beigesoft.accounting.persistable.PaymentTo;
import org.beigesoft.accounting.service.SrvPaymentTo;
import org.beigesoft.accounting.persistable.PrepaymentFrom;
import org.beigesoft.accounting.service.SrvPrepaymentFrom;
import org.beigesoft.accounting.persistable.PaymentFrom;
import org.beigesoft.accounting.service.SrvPaymentFrom;
import org.beigesoft.accounting.service.SrvAccEntryEditDescr;
import org.beigesoft.accounting.service.SrvWarehouseRests;
import org.beigesoft.accounting.service.SrvWarehouseSiteRests;
import org.beigesoft.accounting.persistable.Employee;
import org.beigesoft.accounting.persistable.EmployeeYearWage;
import org.beigesoft.accounting.service.SrvEmployeeYearWage;
import org.beigesoft.accounting.persistable.GoodsLoss;
import org.beigesoft.accounting.persistable.GoodsLossLine;
import org.beigesoft.accounting.persistable.MoveItems;
import org.beigesoft.accounting.persistable.MoveItemsLine;
import org.beigesoft.accounting.service.SrvGoodsLoss;
import org.beigesoft.accounting.service.SrvGoodsLossLine;
import org.beigesoft.accounting.service.SrvMoveItemsLine;
import org.beigesoft.accounting.persistable.BeginningInventory;
import org.beigesoft.accounting.persistable.BeginningInventoryLine;
import org.beigesoft.accounting.service.SrvBeginningInventory;
import org.beigesoft.accounting.service.SrvBeginningInventoryLine;
import org.beigesoft.accounting.persistable.SalesReturn;
import org.beigesoft.accounting.persistable.SalesReturnLine;
import org.beigesoft.accounting.persistable.SalesReturnTaxLine;
import org.beigesoft.accounting.service.SrvSalesReturn;
import org.beigesoft.accounting.service.SrvSalesReturnLine;
import org.beigesoft.accounting.persistable.PurchaseReturn;
import org.beigesoft.accounting.persistable.PurchaseReturnLine;
import org.beigesoft.accounting.persistable.PurchaseReturnTaxLine;
import org.beigesoft.accounting.service.SrvPurchaseReturn;
import org.beigesoft.accounting.service.SrvPurchaseReturnLine;
import org.beigesoft.accounting.persistable.ReplicationAccMethod;
import org.beigesoft.accounting.persistable.ReplExcludeAccountsDebit;
import org.beigesoft.accounting.persistable.ReplExcludeAccountsCredit;
import org.beigesoft.replicator.service.SrvReplicationAccMethod;
import org.beigesoft.replicator.service.SrvReplExcludeAccountsDebitCredit;
import org.beigesoft.replicator.service.ReplicatorXmlHttp;
import org.beigesoft.replicator.service.DatabaseWriterXml;
import org.beigesoft.replicator.service.DatabaseReaderSyncStdXml;
import org.beigesoft.replicator.service.SrvEntityReaderXml;
import org.beigesoft.replicator.service.ISrvEntitySync;
import org.beigesoft.replicator.service.SrvEntitySyncHasId;
import org.beigesoft.replicator.service.SrvEntitySyncPersistableBase;
import org.beigesoft.replicator.service.SrvEntitySyncHasVersion;
import org.beigesoft.replicator.service.SrvEntitySyncPersistableBaseVersion;
import org.beigesoft.replicator.service.SrvEntitySyncAccEntry;
import org.beigesoft.replicator.service.ISrvEntityFieldFiller;
import org.beigesoft.replicator.service.ISrvFieldWriter;
import org.beigesoft.replicator.service.SrvEntityFieldPersistableBaseRepl;
import org.beigesoft.replicator.service.SrvEntityFieldHasIdStringRepl;
import org.beigesoft.replicator.service.SrvEntityFieldHasIdLongRepl;
import org.beigesoft.replicator.service.SrvEntityFieldFillerStd;
import org.beigesoft.replicator.service.SrvEntityWriterXml;
import org.beigesoft.replicator.service.SrvFieldWriterXmlStd;
import org.beigesoft.replicator.service.SrvFieldHasIdWriterXml;
import org.beigesoft.replicator.filter.IFilterEntities;
import org.beigesoft.replicator.filter.FilterLastVersionChanged;
import org.beigesoft.replicator.filter.FilterPersistableBaseImmutable;
import org.beigesoft.replicator.filter.FilterAvoidAccDebtCredit;
import org.beigesoft.accounting.report.SrvBalanceSheet;
import org.beigesoft.accounting.report.SrvReqBalanceSheet;

/**
 * <p>
 * Factory accounting services.
 * </p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FactoryAccServices<RS> implements IFactoryAppBeans {

  /**
   * <p>Date Formatter for derived entry description.</p>
   **/
  private DateFormat entryDateFormatter;

  /**
   * <p>Warehouse service.</p>
   **/
  private SrvWarehouseEntry<RS> srvWarehouseEntry;

  /**
   * <p>Business service for accounting entries.</p>
   **/
  private SrvAccEntry<RS> srvAccEntry;

  /**
   * <p>Use (draw) Material Entry service.</p>
   **/
  private SrvUseMaterialEntry<RS> srvUseMaterialEntry;

  /**
   * <p>COGS entry service.</p>
   **/
  private SrvCogsEntry<RS> srvCogsEntry;

  /**
   * <p>Accounting settings service.</p>
   **/
  private SrvAccSettings srvAccSettings;

  /**
   * <p>Business service for code - java sub-account type map.</p>
   **/
  private SrvTypeCodeSubacc srvTypeCodeSubacc;

  /**
   * <p>Business service for code - java type map.</p>
   **/
  private SrvTypeCodeAccSources srvTypeCodeAccSources;

  /**
   * <p>Business service edit accounting entry description.</p>
   **/
  private SrvAccEntryEditDescr<RS> srvAccEntryEditDescr;

  /**
   * <p>Balance service.</p>
   **/
  private SrvBalanceStd<RS> srvBalanceStd;

  /**
   * <p>Warehouse Rests service.</p>
   **/
  private SrvWarehouseRests<RS> srvWarehouseRests;

  /**
   * <p>Warehouse Site Rests service.</p>
   **/
  private SrvWarehouseSiteRests<RS> srvWarehouseSiteRests;

  /**
   * <p>Wide used Wage Tax Table Percentage method service.</p>
   **/
  private SrvWageTaxPercentageTable<RS> srvWageTaxPercentageTable;

  /**
   * <p>Ledger service.</p>
   **/
  private SrvLedger<RS> srvLedger;

  /**
   * <p>Factory Over Beans.</p>
   **/
  private IFactoryAppBeans factoryOverBeans;

  /**
   * <p>Factory app-beans.</p>
   **/
  private AFactoryAppBeans<RS> factoryAppBeans;

  /**
   * <p>Get other bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean or null
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGet(
    final String pBeanName) throws Exception {
    if ("entryDateFormatter".equals(pBeanName)) {
      return lazyGetEntryDateFormatter();
    } else if ("srvReqBalanceSheet".equals(pBeanName)) {
      return lazyGetSrvReqBalanceSheet();
    } else if ("ISrvBalance".equals(pBeanName)) {
      return lazyGetSrvBalanceStd();
    } else if ("srvWarehouseRests".equals(pBeanName)) {
      return lazyGetSrvWarehouseRests();
    } else if ("srvWarehouseSiteRests".equals(pBeanName)) {
      return lazyGetSrvWarehouseSiteRests();
    } else if ("SrvWageTaxPercentageTable".equals(pBeanName)) {
      return lazyGetSrvWageTaxPercentageTable();
    } else if ("srvLedger".equals(pBeanName)) {
      return lazyGetSrvLedger();
    } else if ("srvWarehouseEntry".equals(pBeanName)) {
      return lazyGetSrvWarehouseEntry();
    } else if ("srvAccEntry".equals(pBeanName)) {
      return lazyGetSrvAccEntry();
    } else if ("srvUseMaterialEntry".equals(pBeanName)) {
      return lazyGetSrvUseMaterialEntry();
    } else if ("srvCogsEntry".equals(pBeanName)) {
      return lazyGetSrvCogsEntry();
    } else if ("srvAccSettings".equals(pBeanName)) {
      return lazyGetSrvAccSettings();
    } else if ("srvAccEntryEditDescr".equals(pBeanName)) {
      return lazyGetSrvAccEntryEditDescr();
    } else if ("srvTypeCodeSubacc".equals(pBeanName)) {
      return lazyGetSrvTypeCodeSubacc();
    } else if ("srvTypeCodeAccSources".equals(pBeanName)) {
      return lazyGetSrvTypeCodeAccSources();
    } else if ("replicatorTaxMarket".equals(pBeanName)) {
      return lazyGetReplicatorTaxMarket();
    } else {
      Object bean = null;
      if (this.factoryOverBeans != null) {
        bean = this.factoryOverBeans.lazyGet(pBeanName);
      }
      if (bean == null) {
        //common entity services
        bean = lazyGetSrvEntity(pBeanName);
      }
      return bean;
    }
  }

  /**
   * <p>Release beans (memory). This is "memory friendly" factory</p>
   * @throws Exception - an exception
   */
  public final synchronized void releaseBeans() throws Exception {
    if (this.factoryOverBeans != null) {
      this.factoryOverBeans.releaseBeans();
    }
    this.entryDateFormatter = null;
    this.srvWarehouseEntry = null;
    this.srvAccEntry = null;
    this.srvUseMaterialEntry = null;
    this.srvCogsEntry = null;
    this.srvAccSettings = null;
    this.srvTypeCodeSubacc = null;
    this.srvTypeCodeAccSources = null;
    this.srvAccEntryEditDescr = null;
    this.srvBalanceStd = null;
    this.srvWarehouseRests = null;
    this.srvWarehouseSiteRests = null;
    this.srvWageTaxPercentageTable = null;
    this.srvLedger = null;
  }

  /**
   * <p>Try to create SrvEntity and put it into beansMap.</p>
   * @param pSrvName - entity service name
   * @return Object - created service or null
   * @throws Exception - an exception
   */
  public final synchronized Object lazyGetSrvEntity(
    final String pSrvName) throws Exception {
    Object srvEntity = factoryAppBeans.getBeansMap().get(pSrvName);
    if (srvEntity == null) {
      Class<?> entityClass = factoryAppBeans.getEntitiesMap()
        .get(pSrvName.replaceFirst("srv", ""));
      if (entityClass != null) {
        if (entityClass == PurchaseInvoice.class
          || entityClass == PurchaseInvoiceServiceLine.class
            || entityClass == PurchaseInvoiceLine.class
              || entityClass == PurchaseInvoiceTaxLine.class) {
          srvEntity = createSrvPurchaseInvoice(pSrvName);
        } else if (entityClass == SalesInvoice.class
          || entityClass == SalesInvoiceServiceLine.class
            || entityClass == SalesInvoiceLine.class
              || entityClass == SalesInvoiceTaxLine.class) {
          srvEntity = createSrvSalesInvoice(pSrvName);
        } else if (entityClass == GoodsLoss.class
          || entityClass == GoodsLossLine.class) {
          srvEntity = createSrvGoodsLoss(pSrvName);
        } else if (entityClass == MoveItems.class
          || entityClass == MoveItemsLine.class) {
          srvEntity = createSrvMoveItems(pSrvName);
        } else if (entityClass == PurchaseReturn.class
          || entityClass == PurchaseReturnTaxLine.class
          || entityClass == PurchaseReturnLine.class) {
          srvEntity = createSrvPurchaseReturn(pSrvName);
        } else if (entityClass == SalesReturn.class
          || entityClass == SalesReturnTaxLine.class
          || entityClass == SalesReturnLine.class) {
          srvEntity = createSrvSalesReturn(pSrvName);
        } else if (entityClass == BeginningInventory.class
          || entityClass == BeginningInventoryLine.class) {
          srvEntity = createSrvBeginningInventory(pSrvName);
        } else if (entityClass == InvItemTaxCategory.class
          || entityClass == InvItemTaxCategoryLine.class) {
          srvEntity = createSrvInvItemTaxCat(pSrvName);
        } else if (entityClass == Wage.class
          || entityClass == WageTaxLine.class
          || entityClass == WageLine.class) {
          srvEntity = createSrvWage(pSrvName);
        } else if (entityClass == ReplicationAccMethod.class
          || entityClass == ReplExcludeAccountsDebit.class
          || entityClass == ReplExcludeAccountsCredit.class) {
          srvEntity = createSrvReplicationAccMethod(pSrvName);
        } else if (entityClass == Employee.class
          || entityClass == EmployeeYearWage.class) {
          createSrvEmployee(pSrvName);
        } else if (entityClass == AccountingEntries.class
          || entityClass == SrvAccountingEntriesLine.class) {
          createSrvAccountingEntries(pSrvName);
        } else if (entityClass == Manufacture.class) {
          srvEntity = new SrvManufacture<RS>(factoryAppBeans.lazyGetSrvOrm(),
            lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
              factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
                lazyGetSrvWarehouseEntry(), lazyGetSrvUseMaterialEntry());
          factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
        } else if (entityClass == PrepaymentTo.class) {
          srvEntity = new SrvPrepaymentTo<RS>(factoryAppBeans.lazyGetSrvOrm(),
            lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
              factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
                lazyGetSrvTypeCodeSubacc());
          factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
        } else if (entityClass == PaymentTo.class) {
          createSrvPaymentTo(pSrvName);
        } else if (entityClass == PrepaymentFrom.class) {
          srvEntity = new SrvPrepaymentFrom<RS>(factoryAppBeans.lazyGetSrvOrm(),
            lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
              factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
                lazyGetSrvTypeCodeSubacc());
          factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
        } else if (entityClass == PaymentFrom.class) {
          createSrvPaymentFrom(pSrvName);
        } else if (entityClass == ManufacturingProcess.class
          || entityClass == UsedMaterialLine.class
          || entityClass == AdditionCostLine.class) {
          createSrvManufacturingProcess(pSrvName);
        } else if (entityClass == Account.class
          || entityClass == SubaccountLine.class) {
          createSrvAccount(pSrvName);
        } else {
          srvEntity = new SrvAccEntitySimple(entityClass,
            factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
          factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
          //owned must be settled
          Map<String, String> clSettings = this.factoryAppBeans
            .lazyGetMngUvdSettings().getClassesSettings()
              .get(entityClass.getCanonicalName());
          if (clSettings != null) {
            //ORM has persitable entities that never shown
            String ownedLists = clSettings.get("ownedLists");
            if (ownedLists != null) {
              LinkedHashSet<String> ownedListsSet = this.factoryAppBeans
                .lazyGetUtlProperties().evalPropsStringsSet(ownedLists);
              for (String className : ownedListsSet) {
                Class<?> classOwned = Class.forName(className);
                @SuppressWarnings("unchecked")
                Object srvEntityOwned = new SrvAccEntityOwnedSimple(classOwned,
                  entityClass, factoryAppBeans.lazyGetSrvOrm(),
                    lazyGetSrvAccSettings());
                this.factoryAppBeans.getBeansMap().put("srv" + classOwned
                  .getSimpleName(), srvEntityOwned);
              }
            }
          }
        }
      }
    }
    return srvEntity;
  }

  /**
   * <p>Create SrvPaymentFrom service.</p>
   * @param pSrvName Service Name
   * @return SrvPaymentFrom PaymentFrom service
   * @throws Exception - an exception
   */
  public final synchronized SrvPaymentFrom<RS>
    createSrvPaymentFrom(final String pSrvName) throws Exception {
    String srvSalesInvoiceName = "srv" + SalesInvoice.class
      .getSimpleName();
    @SuppressWarnings("unchecked")
    SrvSalesInvoice<RS> srvSalesInvoice = (SrvSalesInvoice<RS>)
      factoryAppBeans.getBeansMap().get(srvSalesInvoiceName);
    if (srvSalesInvoice == null) {
      srvSalesInvoice = createSrvSalesInvoice(srvSalesInvoiceName);
    }
    SrvPaymentFrom<RS> srvEntity = new SrvPaymentFrom<RS>(factoryAppBeans
      .lazyGetSrvOrm(), srvSalesInvoice, lazyGetSrvAccSettings(),
        lazyGetSrvAccEntry(), factoryAppBeans.lazyGetSrvI18n(),
          lazyGetEntryDateFormatter(), lazyGetSrvTypeCodeSubacc());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    return srvEntity;
  }

  /**
   * <p>Create SrvPaymentTo service.</p>
   * @param pSrvName Service Name
   * @return SrvPaymentTo PaymentTo service
   * @throws Exception - an exception
   */
  public final synchronized SrvPaymentTo<RS>
    createSrvPaymentTo(final String pSrvName) throws Exception {
    String srvPurchaseInvoiceName = "srv" + PurchaseInvoice.class
      .getSimpleName();
    @SuppressWarnings("unchecked")
    SrvPurchaseInvoice<RS> srvPurchaseInvoice = (SrvPurchaseInvoice<RS>)
      factoryAppBeans.getBeansMap().get(srvPurchaseInvoiceName);
    if (srvPurchaseInvoice == null) {
      srvPurchaseInvoice =
        createSrvPurchaseInvoice(srvPurchaseInvoiceName);
    }
    SrvPaymentTo<RS> srvEntity = new SrvPaymentTo<RS>(factoryAppBeans
      .lazyGetSrvOrm(), srvPurchaseInvoice, lazyGetSrvAccSettings(),
        lazyGetSrvAccEntry(), factoryAppBeans.lazyGetSrvI18n(),
          lazyGetEntryDateFormatter(), lazyGetSrvTypeCodeSubacc());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    return srvEntity;
  }

  /**
   * <p>Create SrvAccountingEntries service.</p>
   * @param pSrvName Service Name
   * @return SrvAccountingEntries AccountingEntries service
   * @throws Exception - an exception
   */
  public final synchronized SrvAccountingEntries<RS>
    createSrvAccountingEntries(final String pSrvName) throws Exception {
    SrvAccountingEntries<RS> srvEntity = new SrvAccountingEntries<RS>(
      factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvEntityLine = new SrvAccountingEntriesLine<RS>(
      factoryAppBeans.lazyGetSrvOrm(), factoryAppBeans
        .lazyGetSrvDatabase(), lazyGetSrvAccSettings(),
          lazyGetSrvBalanceStd(), factoryAppBeans
            .lazyGetSrvI18n(), lazyGetEntryDateFormatter());
    factoryAppBeans.getBeansMap().put("srv" + AccountingEntry.class
      .getSimpleName(), srvEntityLine);
    return srvEntity;
  }

  /**
   * <p>Create SrvEmployee service.</p>
   * @param pSrvName Service Name
   * @return SrvEmployee Employee service
   * @throws Exception - an exception
   */
  public final synchronized SrvAccEntitySimple
    createSrvEmployee(final String pSrvName) throws Exception {
    SrvAccEntitySimple srvEntity = new SrvAccEntitySimple(Employee.class,
      factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    SrvEmployeeYearWage srvEntityOwned = new SrvEmployeeYearWage(
      factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    this.factoryAppBeans.getBeansMap().put("srv" + EmployeeYearWage.class
      .getSimpleName(), srvEntityOwned);
    return srvEntity;
  }

  /**
   * <p>Create SrvManufacturingProcess service.</p>
   * @param pSrvName Service Name
   * @return SrvManufacturingProcess ManufacturingProcess service
   * @throws Exception - an exception
   */
  public final synchronized SrvManufacturingProcess<RS>
    createSrvManufacturingProcess(final String pSrvName) throws Exception {
    SrvManufacturingProcess<RS> srvEntity =
      new SrvManufacturingProcess<RS>(factoryAppBeans
        .lazyGetSrvOrm(), lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
          factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
            lazyGetSrvWarehouseEntry(), lazyGetSrvUseMaterialEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    SrvUsedMaterialLine<RS> srvUsedMaterialLine =
      new SrvUsedMaterialLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry(),
            lazyGetSrvUseMaterialEntry());
    factoryAppBeans.getBeansMap().put("srv" + UsedMaterialLine.class
      .getSimpleName(), srvUsedMaterialLine);
    SrvAdditionCostLine<RS> srvAdditionCostLine =
      new SrvAdditionCostLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvTypeCodeSubacc());
    factoryAppBeans.getBeansMap().put("srv" + AdditionCostLine.class
      .getSimpleName(), srvAdditionCostLine);
    return srvEntity;
  }

  /**
   * <p>Create SrvAccount service.</p>
   * @param pSrvName Service Name
   * @return SrvAccount Account service
   * @throws Exception - an exception
   */
  public final synchronized SrvAccount
    createSrvAccount(final String pSrvName) throws Exception {
    SrvAccount srvEntity = new SrvAccount(factoryAppBeans.lazyGetSrvOrm(),
      lazyGetSrvAccSettings(), lazyGetSrvTypeCodeSubacc());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    SrvSubaccountLine<RS> srvSubaccountLine =
      new SrvSubaccountLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvTypeCodeSubacc(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + SubaccountLine.class
      .getSimpleName(), srvSubaccountLine);
    return srvEntity;
  }

  /**
   * <p>Create InvItem Tax Category service.</p>
   * @param pSrvName Service Name
   * @return SrvAccEntitySimple<InvItemTaxCategory> InvItem Tax Category service
   * @throws Exception - an exception
   */
  public final synchronized SrvAccEntitySimple<InvItemTaxCategory>
    createSrvInvItemTaxCat(final String pSrvName) throws Exception {
    SrvAccEntitySimple<InvItemTaxCategory> srvEntity =
      new SrvAccEntitySimple<InvItemTaxCategory>(InvItemTaxCategory.class,
        factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvInvItemTaxCatLine =
      new SrvInvItemTaxCategoryLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + InvItemTaxCategoryLine
      .class.getSimpleName(), srvInvItemTaxCatLine);
    return srvEntity;
  }

  /**
   * <p>Create MoveItems service.</p>
   * @param pSrvName Service Name
   * @return SrvAccEntitySimple<MoveItems> MoveItems service
   * @throws Exception - an exception
   */
  public final synchronized SrvAccEntitySimple<MoveItems>
    createSrvMoveItems(final String pSrvName) throws Exception {
    SrvAccEntitySimple<MoveItems> srvEntity =
      new SrvAccEntitySimple<MoveItems>(MoveItems.class,
        factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvMoveItemsLine =
      new SrvMoveItemsLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry());
    factoryAppBeans.getBeansMap().put("srv" + MoveItemsLine
      .class.getSimpleName(), srvMoveItemsLine);
    return srvEntity;
  }

  /**
   * <p>Create wage service.</p>
   * @param pSrvName Service Name
   * @return SrvWage wage service
   * @throws Exception - an exception
   */
  public final synchronized SrvWage<RS>
    createSrvWage(final String pSrvName) throws Exception {
    SrvWage<RS> srvEntity = new SrvWage<RS>(factoryAppBeans.lazyGetSrvOrm(),
      lazyGetSrvAccSettings(), lazyGetSrvAccEntry(), factoryAppBeans
        .lazyGetSrvI18n(), lazyGetEntryDateFormatter(), this.factoryAppBeans);
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvWageTaxLine =
      new SrvWageTaxLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + WageTaxLine.class
      .getSimpleName(), srvWageTaxLine);
    Object srvWageLine =
      new SrvWageLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + WageLine.class
      .getSimpleName(), srvWageLine);
    return srvEntity;
  }

  /**
   * <p>Create ReplicationAccMethod service.</p>
   * @param pSrvName Service Name
   * @return SrvReplicationAccMethod ReplicationAccMethod service
   * @throws Exception - an exception
   */
  public final synchronized SrvReplicationAccMethod<RS>
    createSrvReplicationAccMethod(final String pSrvName) throws Exception {
    SrvReplicationAccMethod<RS> srvEntity = new SrvReplicationAccMethod<RS>(
      factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvReplExcludeAccountsDebit =
      new SrvReplExcludeAccountsDebitCredit<RS, ReplExcludeAccountsDebit>(
        ReplExcludeAccountsDebit.class, factoryAppBeans.lazyGetSrvOrm(),
          lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + ReplExcludeAccountsDebit.class
      .getSimpleName(), srvReplExcludeAccountsDebit);
    Object srvReplExcludeAccountsCredit =
      new SrvReplExcludeAccountsDebitCredit<RS, ReplExcludeAccountsCredit>(
        ReplExcludeAccountsCredit.class, factoryAppBeans.lazyGetSrvOrm(),
          lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + ReplExcludeAccountsCredit.class
      .getSimpleName(), srvReplExcludeAccountsCredit);
    return srvEntity;
  }

  /**
   * <p>Create Beginning Inventory service.</p>
   * @param pSrvName Service Name
   * @return SrvBeginningInventory Beginning Inventory service
   * @throws Exception - an exception
   */
  public final synchronized SrvBeginningInventory<RS>
    createSrvBeginningInventory(final String pSrvName) throws Exception {
    SrvBeginningInventory<RS> srvEntity =
      new SrvBeginningInventory<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
          factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
            lazyGetSrvWarehouseEntry(), lazyGetSrvUseMaterialEntry(),
              lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvEntityLine =
      new SrvBeginningInventoryLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry());
    factoryAppBeans.getBeansMap().put("srv" + BeginningInventoryLine
      .class.getSimpleName(), srvEntityLine);
    return srvEntity;
  }

  /**
   * <p>Create Purchase Invoice service.</p>
   * @param pSrvName Service Name
   * @return SrvPurchaseInvoice Purchase Invoice service
   * @throws Exception - an exception
   */
  public final synchronized SrvPurchaseInvoice<RS>
    createSrvPurchaseInvoice(final String pSrvName) throws Exception {
    SrvPurchaseInvoice<RS> srvEntity =
      new SrvPurchaseInvoice<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
          factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
            lazyGetSrvWarehouseEntry(), lazyGetSrvUseMaterialEntry(),
              lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    UtlPurchaseGoodsServiceLine<RS> utlPurchaseGoodsServiceLine =
      new UtlPurchaseGoodsServiceLine<RS>();
    utlPurchaseGoodsServiceLine.setSrvDatabase(factoryAppBeans
      .lazyGetSrvDatabase());
    utlPurchaseGoodsServiceLine.setSrvOrm(factoryAppBeans
      .lazyGetSrvOrm());
    utlPurchaseGoodsServiceLine.setSrvAccSettings(lazyGetSrvAccSettings());
    Object srvEntityLine =
      new SrvPurchaseInvoiceLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        utlPurchaseGoodsServiceLine,
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry());
    factoryAppBeans.getBeansMap().put("srv" + PurchaseInvoiceLine
      .class.getSimpleName(), srvEntityLine);
    Object srvEntitySrvLine =
      new SrvPurchaseInvoiceServiceLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        utlPurchaseGoodsServiceLine, lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + PurchaseInvoiceServiceLine
      .class.getSimpleName(), srvEntitySrvLine);
    Object srvTaxLine =
      new SrvAccEntityOwnedSimple<PurchaseInvoiceTaxLine,
        PurchaseInvoice>(PurchaseInvoiceTaxLine.class,
          PurchaseInvoice.class, factoryAppBeans.lazyGetSrvOrm(),
            lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + PurchaseInvoiceTaxLine
      .class.getSimpleName(), srvTaxLine);
    return srvEntity;
  }

  /**
   * <p>Create Sales Invoice service.</p>
   * @param pSrvName Service Name
   * @return SrvSalesInvoice Sales Invoice service
   * @throws Exception - an exception
   */
  public final synchronized SrvSalesInvoice<RS>
    createSrvSalesInvoice(final String pSrvName) throws Exception {
    SrvSalesInvoice<RS> srvEntity = new SrvSalesInvoice<RS>(factoryAppBeans
      .lazyGetSrvOrm(), lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
        factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
          lazyGetSrvWarehouseEntry(), lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    UtlSalesGoodsServiceLine<RS> utlSalesGoodsServiceLine =
      new UtlSalesGoodsServiceLine<RS>();
    utlSalesGoodsServiceLine.setSrvDatabase(factoryAppBeans
      .lazyGetSrvDatabase());
    utlSalesGoodsServiceLine.setSrvOrm(factoryAppBeans
      .lazyGetSrvOrm());
    utlSalesGoodsServiceLine.setSrvAccSettings(lazyGetSrvAccSettings());
    Object srvEntitySrvLine =
      new SrvSalesInvoiceServiceLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        utlSalesGoodsServiceLine, lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + SalesInvoiceServiceLine
      .class.getSimpleName(), srvEntitySrvLine);
    Object srvEntityLine =
      new SrvSalesInvoiceLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        utlSalesGoodsServiceLine,
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry(),
            lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put("srv" + SalesInvoiceLine
      .class.getSimpleName(), srvEntityLine);
    Object srvTaxLine =
      new SrvAccEntityOwnedSimple<SalesInvoiceTaxLine, SalesInvoice>(
        SalesInvoiceTaxLine.class, SalesInvoice.class,
          factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + SalesInvoiceTaxLine
      .class.getSimpleName(), srvTaxLine);
    return srvEntity;
  }

  /**
   * <p>Create GoodsLoss service.</p>
   * @param pSrvName Service Name
   * @return SrvGoodsLoss GoodsLoss service
   * @throws Exception - an exception
   */
  public final synchronized SrvGoodsLoss<RS>
    createSrvGoodsLoss(final String pSrvName) throws Exception {
    SrvGoodsLoss<RS> srvEntity = new SrvGoodsLoss<RS>(factoryAppBeans
      .lazyGetSrvOrm(), lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
        factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
          lazyGetSrvWarehouseEntry(), lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvEntityLine =
      new SrvGoodsLossLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry(),
            lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put("srv" + GoodsLossLine
      .class.getSimpleName(), srvEntityLine);
    return srvEntity;
  }

  /**
   * <p>Create Sales return service.</p>
   * @param pSrvName Service Name
   * @return SrvSalesReturn SalesReturn service
   * @throws Exception - an exception
   */
  public final synchronized SrvSalesReturn<RS>
    createSrvSalesReturn(final String pSrvName) throws Exception {
    SrvSalesReturn<RS> srvEntity =
      new SrvSalesReturn<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
          factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
            lazyGetSrvWarehouseEntry(), lazyGetSrvUseMaterialEntry(),
              lazyGetSrvCogsEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvEntityLine =
      new SrvSalesReturnLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry());
    factoryAppBeans.getBeansMap().put("srv" + SalesReturnLine
      .class.getSimpleName(), srvEntityLine);
    Object srvTaxLine =
      new SrvAccEntityOwnedSimple<SalesReturnTaxLine,
        SalesReturn>(SalesReturnTaxLine.class,
          SalesReturn.class, factoryAppBeans.lazyGetSrvOrm(),
            lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + SalesReturnTaxLine
      .class.getSimpleName(), srvTaxLine);
    return srvEntity;
  }

  /**
   * <p>Create Purchase return service.</p>
   * @param pSrvName Service Name
   * @return SrvPurchaseReturn PurchaseReturn service
   * @throws Exception - an exception
   */
  public final synchronized SrvPurchaseReturn<RS>
    createSrvPurchaseReturn(final String pSrvName) throws Exception {
    SrvPurchaseReturn<RS> srvEntity =
      new SrvPurchaseReturn<RS>(factoryAppBeans.lazyGetSrvOrm(),
        lazyGetSrvAccSettings(), lazyGetSrvAccEntry(),
          factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter(),
            lazyGetSrvWarehouseEntry(), lazyGetSrvUseMaterialEntry());
    factoryAppBeans.getBeansMap().put(pSrvName, srvEntity);
    Object srvEntityLine =
      new SrvPurchaseReturnLine<RS>(factoryAppBeans.lazyGetSrvOrm(),
        factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvWarehouseEntry(),
            lazyGetSrvUseMaterialEntry(), factoryAppBeans.lazyGetSrvI18n(),
              lazyGetEntryDateFormatter());
    factoryAppBeans.getBeansMap().put("srv" + PurchaseReturnLine
      .class.getSimpleName(), srvEntityLine);
    Object srvTaxLine =
      new SrvAccEntityOwnedSimple<PurchaseReturnTaxLine,
        PurchaseReturn>(PurchaseReturnTaxLine.class,
          PurchaseReturn.class, factoryAppBeans.lazyGetSrvOrm(),
            lazyGetSrvAccSettings());
    factoryAppBeans.getBeansMap().put("srv" + PurchaseReturnTaxLine
      .class.getSimpleName(), srvTaxLine);
    return srvEntity;
  }

  /**
   * <p>Get entryDateFormatter in lazy mode.</p>
   * @return entryDateFormatter for derived entry description
   * @throws Exception - an exception
   */
  public final synchronized DateFormat
    lazyGetEntryDateFormatter() throws Exception {
    if (this.entryDateFormatter == null) {
      this.entryDateFormatter = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    }
    return this.entryDateFormatter;
  }

  /**
   * <p>Get SrvBalanceStd in lazy mode.</p>
   * @return SrvBalanceStd - SrvBalanceStd
   * @throws Exception - an exception
   */
  public final synchronized SrvBalanceStd<RS>
    lazyGetSrvBalanceStd() throws Exception {
    if (this.srvBalanceStd == null) {
      this.srvBalanceStd = new SrvBalanceStd<RS>(factoryAppBeans
        .lazyGetSrvOrm(), factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), factoryAppBeans.lazyGetLogger());
    }
    return this.srvBalanceStd;
  }

  /**
   * <p>Get SrvWarehouseRests in lazy mode.</p>
   * @return SrvWarehouseRests - SrvWarehouseRests
   * @throws Exception - an exception
   */
  public final synchronized SrvWarehouseRests<RS>
    lazyGetSrvWarehouseRests() throws Exception {
    if (this.srvWarehouseRests == null) {
      this.srvWarehouseRests = new SrvWarehouseRests<RS>(factoryAppBeans
        .lazyGetSrvDatabase());
    }
    return this.srvWarehouseRests;
  }

  /**
   * <p>Get SrvWarehouseSiteRests in lazy mode.</p>
   * @return SrvWarehouseSiteRests - SrvWarehouseSiteRests
   * @throws Exception - an exception
   */
  public final synchronized SrvWarehouseSiteRests<RS>
    lazyGetSrvWarehouseSiteRests() throws Exception {
    if (this.srvWarehouseSiteRests == null) {
      this.srvWarehouseSiteRests = new SrvWarehouseSiteRests<RS>(
        factoryAppBeans.lazyGetSrvDatabase());
    }
    return this.srvWarehouseSiteRests;
  }

  /**
   * <p>Get SrvWageTaxPercentageTable in lazy mode.</p>
   * @return SrvWageTaxPercentageTable - SrvWageTaxPercentageTable
   * @throws Exception - an exception
   */
  public final synchronized SrvWageTaxPercentageTable<RS>
    lazyGetSrvWageTaxPercentageTable() throws Exception {
    if (this.srvWageTaxPercentageTable == null) {
      this.srvWageTaxPercentageTable =
        new SrvWageTaxPercentageTable<RS>(factoryAppBeans
          .lazyGetSrvOrm(), factoryAppBeans.lazyGetSrvDatabase(),
            lazyGetSrvAccSettings());
    }
    return this.srvWageTaxPercentageTable;
  }

  /**
   * <p>Get SrvLedger in lazy mode.</p>
   * @return SrvLedger - SrvLedger
   * @throws Exception - an exception
   */
  public final synchronized SrvLedger<RS>
    lazyGetSrvLedger() throws Exception {
    if (this.srvLedger == null) {
      this.srvLedger = new SrvLedger<RS>(factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), lazyGetSrvBalanceStd());
    }
    return this.srvLedger;
  }

  /**
   * <p>Get SrvWarehouseEntry in lazy mode.</p>
   * @return SrvWarehouseEntry - SrvWarehouseEntry
   * @throws Exception - an exception
   */
  public final synchronized SrvWarehouseEntry<RS>
    lazyGetSrvWarehouseEntry() throws Exception {
    if (this.srvWarehouseEntry == null) {
      this.srvWarehouseEntry =
        new SrvWarehouseEntry<RS>(factoryAppBeans.lazyGetSrvOrm(),
          lazyGetSrvTypeCodeAccSources(),
            factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter());
    }
    return this.srvWarehouseEntry;
  }

  /**
   * <p>Get SrvAccEntry in lazy mode.</p>
   * @return SrvAccEntry - SrvAccEntry
   * @throws Exception - an exception
   */
  public final synchronized SrvAccEntry<RS> lazyGetSrvAccEntry()
    throws Exception {
    if (this.srvAccEntry == null) {
      this.srvAccEntry = new SrvAccEntry<RS>(factoryAppBeans.lazyGetSrvOrm(),
          factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvTypeCodeAccSources(),
            lazyGetSrvAccSettings(), lazyGetSrvBalanceStd(),
              factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter());
    }
    return this.srvAccEntry;
  }

  /**
   * <p>Get SrvUseMaterialEntry in lazy mode.</p>
   * @return SrvUseMaterialEntry - SrvUseMaterialEntry
   * @throws Exception - an exception
   */
  public final synchronized SrvUseMaterialEntry<RS>
    lazyGetSrvUseMaterialEntry() throws Exception {
    if (this.srvUseMaterialEntry == null) {
      this.srvUseMaterialEntry = new SrvUseMaterialEntry<RS>(
        factoryAppBeans.lazyGetSrvOrm(),
          factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvTypeCodeAccSources(),
            lazyGetSrvAccSettings(), factoryAppBeans.lazyGetSrvI18n(),
              lazyGetEntryDateFormatter());
    }
    return this.srvUseMaterialEntry;
  }

  /**
   * <p>Get SrvCogsEntry in lazy mode.</p>
   * @return SrvCogsEntry - SrvCogsEntry
   * @throws Exception - an exception
   */
  public final synchronized SrvCogsEntry<RS>
    lazyGetSrvCogsEntry() throws Exception {
    if (this.srvCogsEntry == null) {
      this.srvCogsEntry = new SrvCogsEntry<RS>(
        factoryAppBeans.lazyGetSrvOrm(),
          factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvTypeCodeAccSources(),
            lazyGetSrvAccSettings(), factoryAppBeans.lazyGetSrvI18n(),
              lazyGetEntryDateFormatter());
    }
    return this.srvCogsEntry;
  }

  /**
   * <p>Get SrvAccSettings in lazy mode.</p>
   * @return SrvAccSettings - SrvAccSettings
   * @throws Exception - an exception
   */
  public final synchronized SrvAccSettings lazyGetSrvAccSettings()
    throws Exception {
    if (this.srvAccSettings == null) {
      this.srvAccSettings = new SrvAccSettings(factoryAppBeans.lazyGetSrvOrm());
      this.factoryAppBeans.getBeansMap().put("srv" + AccEntriesSourcesLine
        .class.getSimpleName(), new SrvAccSettingsLine<AccEntriesSourcesLine,
          AccSettings>(AccEntriesSourcesLine.class, AccSettings.class,
            factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings()));
      this.factoryAppBeans.getBeansMap().put("srv" + CogsItemSourcesLine
        .class.getSimpleName(),
          new SrvAccSettingsLine<CogsItemSourcesLine, AccSettings>(
            CogsItemSourcesLine.class, AccSettings.class,
              factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings()));
      this.factoryAppBeans.getBeansMap().put("srv" + DrawMaterialSourcesLine
        .class.getSimpleName(),
          new SrvAccSettingsLine<DrawMaterialSourcesLine, AccSettings>(
            DrawMaterialSourcesLine.class, AccSettings.class,
              factoryAppBeans.lazyGetSrvOrm(), lazyGetSrvAccSettings()));
    }
    return this.srvAccSettings;
  }

  /**
   * <p>Get ReplicatorXmlHttp tax-market in lazy mode.</p>
   * @return ReplicatorXmlHttp - ReplicatorXmlHttp
   * @throws Exception - an exception
   */
  public final synchronized ReplicatorXmlHttp<RS>
    lazyGetReplicatorTaxMarket() throws Exception {
    @SuppressWarnings("unchecked")
    ReplicatorXmlHttp<RS> srvReplTaxMarketXml =
      (ReplicatorXmlHttp<RS>) this.factoryAppBeans.getBeansMap()
        .get("srvReplTaxMarketXml");
    if (srvReplTaxMarketXml == null) {
      srvReplTaxMarketXml = new ReplicatorXmlHttp<RS>();
      SrvEntityReaderXml srvEntityReaderXml = new SrvEntityReaderXml();
      srvEntityReaderXml.setUtilXml(this.factoryAppBeans.lazyGetUtilXml());
      SrvEntityFieldFillerStd srvEntityFieldFillerStd =
        new SrvEntityFieldFillerStd();
      srvEntityFieldFillerStd.setUtilXml(this.factoryAppBeans.lazyGetUtilXml());
      srvEntityFieldFillerStd.setUtlReflection(this.factoryAppBeans
        .lazyGetUtlReflection());
      SrvEntityFieldPersistableBaseRepl srvEntityFieldPersistableBaseRepl =
        new SrvEntityFieldPersistableBaseRepl();
      srvEntityFieldPersistableBaseRepl
        .setUtlReflection(this.factoryAppBeans.lazyGetUtlReflection());
      SrvEntityFieldHasIdStringRepl srvEntityFieldHasIdStringRepl =
        new SrvEntityFieldHasIdStringRepl();
      srvEntityFieldHasIdStringRepl
        .setUtlReflection(this.factoryAppBeans.lazyGetUtlReflection());
      SrvEntityFieldHasIdLongRepl srvEntityFieldHasIdLongRepl =
        new SrvEntityFieldHasIdLongRepl();
      srvEntityFieldHasIdLongRepl
        .setUtlReflection(this.factoryAppBeans.lazyGetUtlReflection());
      Map<String, ISrvEntityFieldFiller> fieldsFillersMap =
        new HashMap<String, ISrvEntityFieldFiller>();
      fieldsFillersMap.put("SrvEntityFieldFillerStd",
        srvEntityFieldFillerStd);
      fieldsFillersMap.put("SrvEntityFieldPersistableBaseRepl",
        srvEntityFieldPersistableBaseRepl);
      fieldsFillersMap.put("SrvEntityFieldHasIdStringRepl",
        srvEntityFieldHasIdStringRepl);
      fieldsFillersMap.put("SrvEntityFieldHasIdLongRepl",
        srvEntityFieldHasIdLongRepl);
      srvEntityReaderXml.setFieldsFillersMap(fieldsFillersMap);
      srvEntityReaderXml.setMngSettings(lazyGetMngSettingsReplTaxMarket());
      Map<String, ISrvEntitySync> srvEntitySyncMap =
        new HashMap<String, ISrvEntitySync>();
      DatabaseReaderSyncStdXml<RS> databaseReaderSyncStdXml =
       new DatabaseReaderSyncStdXml<RS>();
      SrvEntitySyncHasId<RS> srvEntitySyncHasId = new SrvEntitySyncHasId<RS>();
      srvEntitySyncHasId.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      srvEntitySyncMap.put("SrvEntitySyncHasId", srvEntitySyncHasId);
      SrvEntitySyncHasVersion<RS> srvEntitySyncHasVersion =
        new SrvEntitySyncHasVersion<RS>();
      srvEntitySyncHasVersion.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      srvEntitySyncMap.put("SrvEntitySyncHasVersion", srvEntitySyncHasVersion);
      SrvEntitySyncPersistableBase<RS> srvEntitySyncPersistableBase =
        new SrvEntitySyncPersistableBase<RS>();
      srvEntitySyncPersistableBase.setSrvOrm(this.factoryAppBeans
        .lazyGetSrvOrm());
      srvEntitySyncMap.put("SrvEntitySyncPersistableBase",
        srvEntitySyncPersistableBase);
      SrvEntitySyncPersistableBaseVersion<RS>
        srvEntitySyncPersistableBaseVersion =
          new SrvEntitySyncPersistableBaseVersion<RS>();
      srvEntitySyncPersistableBaseVersion.setSrvOrm(this.factoryAppBeans
        .lazyGetSrvOrm());
      srvEntitySyncMap.put("SrvEntitySyncPersistableBaseVersion",
        srvEntitySyncPersistableBaseVersion);
      SrvEntitySyncAccEntry<RS> srvEntitySyncAccEntry =
          new SrvEntitySyncAccEntry<RS>();
      srvEntitySyncAccEntry.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      srvEntitySyncAccEntry.setSrvBalance(lazyGetSrvBalanceStd());
      srvEntitySyncMap.put("SrvEntitySyncAccEntry", srvEntitySyncAccEntry);
      databaseReaderSyncStdXml.setSrvEntitySyncMap(srvEntitySyncMap);
      databaseReaderSyncStdXml.setUtilXml(this.factoryAppBeans
        .lazyGetUtilXml());
      databaseReaderSyncStdXml.setSrvEntityReader(srvEntityReaderXml);
      databaseReaderSyncStdXml.setSrvDatabase(this.factoryAppBeans
        .lazyGetSrvDatabase());
      databaseReaderSyncStdXml.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      databaseReaderSyncStdXml
        .setMngSettings(lazyGetMngSettingsReplTaxMarket());
      databaseReaderSyncStdXml.setLogger(this.factoryAppBeans.lazyGetLogger());
      Map<String, IFilterEntities> filtersEntities =
        new HashMap<String, IFilterEntities>();
      FilterPersistableBaseImmutable<RS> filterPersistableBaseImmutable =
        new FilterPersistableBaseImmutable<RS>();
      filterPersistableBaseImmutable.setSrvDatabase(this.factoryAppBeans
        .lazyGetSrvDatabase());
      filtersEntities.put("FilterPersistableBaseImmutable",
        filterPersistableBaseImmutable);
      FilterAvoidAccDebtCredit<RS> filterAvoidAccDebtCredit =
        new FilterAvoidAccDebtCredit<RS>();
      filterAvoidAccDebtCredit.setSrvOrm(this.factoryAppBeans
        .lazyGetSrvOrm());
      filterAvoidAccDebtCredit.setFilterId(filterPersistableBaseImmutable);
      filtersEntities.put("FilterAvoidAccDebtCredit",
        filterAvoidAccDebtCredit);
      FilterLastVersionChanged filterLastVersionChanged =
        new FilterLastVersionChanged();
      filterLastVersionChanged
        .setLastReplicatedDateEvaluator(filterAvoidAccDebtCredit);
      filtersEntities.put("FilterLastVersionChanged", filterLastVersionChanged);
      SrvReplicationAccMethod<RS> srvReplicationAccMethod =
        (SrvReplicationAccMethod<RS>) lazyGet("srv"
          + ReplicationAccMethod.class.getSimpleName());
      srvReplicationAccMethod
        .addReplAccMethChangedHandler(filterAvoidAccDebtCredit);
      SrvReplExcludeAccountsDebitCredit<RS,
        ReplExcludeAccountsDebit> srvReplExcludeAccountsDebit =
          (SrvReplExcludeAccountsDebitCredit<RS, ReplExcludeAccountsDebit>)
            lazyGet("srv" + ReplExcludeAccountsDebit.class.getSimpleName());
      srvReplExcludeAccountsDebit
        .addReplAccMethChangedHandler(filterAvoidAccDebtCredit);
      SrvReplExcludeAccountsDebitCredit<RS,
        ReplExcludeAccountsCredit> srvReplExcludeAccountsCredit =
          (SrvReplExcludeAccountsDebitCredit<RS, ReplExcludeAccountsCredit>)
            lazyGet("srv" + ReplExcludeAccountsCredit.class.getSimpleName());
      srvReplExcludeAccountsCredit
        .addReplAccMethChangedHandler(filterAvoidAccDebtCredit);
      srvReplTaxMarketXml.setFiltersEntities(filtersEntities);
      srvReplTaxMarketXml.setDatabasePrepearerAfter(filterAvoidAccDebtCredit);
      srvReplTaxMarketXml.setUtilXml(this.factoryAppBeans.lazyGetUtilXml());
      srvReplTaxMarketXml.setSrvEntityReaderXml(srvEntityReaderXml);
      srvReplTaxMarketXml.setMngSettings(lazyGetMngSettingsReplTaxMarket());
      srvReplTaxMarketXml.setSrvDatabase(this.factoryAppBeans
        .lazyGetSrvDatabase());
      srvReplTaxMarketXml.setDatabaseReader(databaseReaderSyncStdXml);
      srvReplTaxMarketXml.setLogger(this.factoryAppBeans.lazyGetLogger());
      this.factoryAppBeans.lazyGetLogger().info(FactoryAccServices.class,
        "srvReplTaxMarketXml has been created.");
      this.factoryAppBeans.getBeansMap()
        .put("srvReplTaxMarketXml", srvReplTaxMarketXml);
    }
    return srvReplTaxMarketXml;
  }

  /**
   * <p>Get DatabaseWriterXml for tax-market in lazy mode.</p>
   * @return DatabaseWriterXml - DatabaseWriterXml
   * @throws Exception - an exception
   */
  public final synchronized DatabaseWriterXml<RS> lazyGetDbWriterReplTaxMarket(
    ) throws Exception {
   @SuppressWarnings("unchecked")
   DatabaseWriterXml<RS> dbWriterXmlReplTaxMarket =
      (DatabaseWriterXml<RS>) this.factoryAppBeans.getBeansMap()
        .get("dbWriterXmlReplTaxMarket");
    if (dbWriterXmlReplTaxMarket == null) {
      dbWriterXmlReplTaxMarket = new DatabaseWriterXml<RS>();
      SrvEntityWriterXml srvEntityWriterXml = new SrvEntityWriterXml();
      SrvFieldWriterXmlStd srvFieldWriterXmlStd = new SrvFieldWriterXmlStd();
      srvFieldWriterXmlStd.setUtilXml(this.factoryAppBeans.lazyGetUtilXml());
      SrvFieldHasIdWriterXml srvFieldHasIdWriterXml =
        new SrvFieldHasIdWriterXml();
      srvFieldHasIdWriterXml.setUtilXml(this.factoryAppBeans.lazyGetUtilXml());
      srvEntityWriterXml.setUtlReflection(this.factoryAppBeans
        .lazyGetUtlReflection());
      Map<String, ISrvFieldWriter> fieldsWritersMap =
        new HashMap<String, ISrvFieldWriter>();
      fieldsWritersMap.put("SrvFieldWriterXmlStd", srvFieldWriterXmlStd);
      fieldsWritersMap.put("SrvFieldHasIdWriterXml", srvFieldHasIdWriterXml);
      srvEntityWriterXml.setFieldsWritersMap(fieldsWritersMap);
      srvEntityWriterXml.setMngSettings(lazyGetMngSettingsReplTaxMarket());
      dbWriterXmlReplTaxMarket.setLogger(this.factoryAppBeans.lazyGetLogger());
      dbWriterXmlReplTaxMarket.setSrvEntityWriter(srvEntityWriterXml);
      dbWriterXmlReplTaxMarket.setSrvDatabase(this.factoryAppBeans
        .lazyGetSrvDatabase());
      dbWriterXmlReplTaxMarket.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      this.factoryAppBeans.lazyGetLogger().info(FactoryAccServices.class,
        "DatabaseWriterXml tax-market has been created.");
      this.factoryAppBeans.getBeansMap()
        .put("dbWriterXmlReplTaxMarket", dbWriterXmlReplTaxMarket);
    }
    return dbWriterXmlReplTaxMarket;
  }

  /**
   * <p>Get MngSettings for replicator tax-market acc in lazy mode.</p>
   * @return MngSettings - MngSettings replicator
   * @throws Exception - an exception
   */
  public final synchronized MngSettings lazyGetMngSettingsReplTaxMarket(
    ) throws Exception {
    MngSettings mngSettingsReplTaxMarket =
      (MngSettings) this.factoryAppBeans.getBeansMap()
        .get("mngSettingsReplTaxMarket");
    if (mngSettingsReplTaxMarket == null) {
      mngSettingsReplTaxMarket = new MngSettings();
      mngSettingsReplTaxMarket.setLogger(this.factoryAppBeans.lazyGetLogger());
      mngSettingsReplTaxMarket
        .loadConfiguration("tax-to-market", "base.xml");
      this.factoryAppBeans.lazyGetLogger().info(FactoryAccServices.class,
        "MngSettings tax-market replicator has been created.");
      this.factoryAppBeans.getBeansMap()
        .put("mngSettingsReplTaxMarket", mngSettingsReplTaxMarket);
    }
    return mngSettingsReplTaxMarket;
  }

  /**
   * <p>Get SrvReqBalanceSheet  in lazy mode.</p>
   * @return SrvReqBalanceSheet - SrvReqBalanceSheet
   * @throws Exception - an exception
   */
  public final synchronized SrvReqBalanceSheet<RS> lazyGetSrvReqBalanceSheet(
    ) throws Exception {
    @SuppressWarnings("unchecked")
    SrvReqBalanceSheet<RS> srvReqBalanceSheet =
      (SrvReqBalanceSheet<RS>) this.factoryAppBeans.getBeansMap()
        .get("srvReqBalanceSheet");
    if (srvReqBalanceSheet == null) {
      srvReqBalanceSheet = new SrvReqBalanceSheet<RS>();
      srvReqBalanceSheet.setSrvDatabase(this.factoryAppBeans
        .lazyGetSrvDatabase());
      srvReqBalanceSheet.setSrvAccSettings(lazyGetSrvAccSettings());
      SrvBalanceSheet<RS> srvBalanceSheet = new SrvBalanceSheet<RS>(this
        .factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvAccSettings(),
          lazyGetSrvBalanceStd());
      srvReqBalanceSheet.setSrvBalanceSheet(srvBalanceSheet);
      this.factoryAppBeans.lazyGetLogger().info(FactoryAccServices.class,
        "SrvReqBalanceSheet has been created.");
      this.factoryAppBeans.getBeansMap()
        .put("srvReqBalanceSheet", srvReqBalanceSheet);
    }
    return srvReqBalanceSheet;
  }

  /**
   * <p>Get SrvAccEntryEditDescr in lazy mode.</p>
   * @return SrvAccEntryEditDescr - SrvAccEntryEditDescr
   * @throws Exception - an exception
   */
  public final synchronized SrvAccEntryEditDescr<RS>
    lazyGetSrvAccEntryEditDescr() throws Exception {
    if (this.srvAccEntryEditDescr == null) {
      this.srvAccEntryEditDescr = new SrvAccEntryEditDescr<RS>(factoryAppBeans
        .lazyGetSrvOrm(), lazyGetSrvAccSettings());
    }
    return this.srvAccEntryEditDescr;
  }

  /**
   * <p>Get SrvTypeCodeSubacc in lazy mode.</p>
   * @return SrvTypeCodeSubacc - SrvTypeCodeSubacc
   * @throws Exception - an exception
   */
  public final synchronized SrvTypeCodeSubacc lazyGetSrvTypeCodeSubacc()
    throws Exception {
    if (this.srvTypeCodeSubacc == null) {
      this.srvTypeCodeSubacc = new SrvTypeCodeSubacc();
    }
    return this.srvTypeCodeSubacc;
  }

  /**
   * <p>Get SrvTypeCodeAccSources in lazy mode.</p>
   * @return SrvTypeCodeAccSources - SrvTypeCodeAccSources
   * @throws Exception - an exception
   */
  public final synchronized SrvTypeCodeAccSources lazyGetSrvTypeCodeAccSources()
    throws Exception {
    if (this.srvTypeCodeAccSources == null) {
      this.srvTypeCodeAccSources = new SrvTypeCodeAccSources();
    }
    return this.srvTypeCodeAccSources;
  }

  /**
   * <p>Getter for factoryAppBeans.</p>
   * @return AFactoryAppBeans<RS>
   **/
  public final synchronized AFactoryAppBeans<RS> getFactoryAppBeans() {
    return this.factoryAppBeans;
  }

  /**
   * <p>Setter for factoryAppBeans.</p>
   * @param pFactoryAppBeans reference
   **/
  public final synchronized void setFactoryAppBeans(
    final AFactoryAppBeans<RS> pFactoryAppBeans) {
    this.factoryAppBeans = pFactoryAppBeans;
  }

  /**
   * <p>Getter for factoryOverBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final synchronized IFactoryAppBeans getFactoryOverBeans() {
    return this.factoryOverBeans;
  }

  /**
   * <p>Setter for factoryOverBeans.</p>
   * @param pFactoryOverBeans reference
   **/
  public final synchronized void setFactoryOverBeans(
    final IFactoryAppBeans pFactoryOverBeans) {
    this.factoryOverBeans = pFactoryOverBeans;
  }
}
