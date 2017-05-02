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
import java.text.DateFormat;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.handler.TransactionalRequestHandler;
import org.beigesoft.handler.SimpleRequestHandler;
import org.beigesoft.web.factory.AFactoryAppBeans;
import org.beigesoft.accounting.service.SrvWarehouseEntry;
import org.beigesoft.accounting.service.SrvBalanceStd;
import org.beigesoft.accounting.service.SrvLedger;
import org.beigesoft.accounting.service.SrvUseMaterialEntry;
import org.beigesoft.accounting.service.SrvCogsEntry;
import org.beigesoft.accounting.service.SrvAccSettings;
import org.beigesoft.accounting.service.SrvAccEntry;
import org.beigesoft.accounting.service.SrvWarehouseRests;
import org.beigesoft.accounting.service.SrvWarehouseSiteRests;
import org.beigesoft.accounting.service.SrvTypeCodeSubacc;
import org.beigesoft.accounting.service.SrvTypeCodeAccSources;
import org.beigesoft.replicator.processor.PrcReplicationAccMethodSave;
import org.beigesoft.replicator.processor.PrcReplExcludeAccountsDebitCreditSave;
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
import org.beigesoft.webstore.service.SrvSettingsAdd;
import org.beigesoft.webstore.service.SrvTradingSettings;

/**
 * <p>
 * Factory accounting/trade services.
 * This is factory other beans in the factory app beans.
 * </p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FactoryAccServices<RS> implements IFactoryAppBeans {

  /**
   * <p>Factory app-beans.</p>
   **/
  private AFactoryAppBeans<RS> factoryAppBeans;

  /**
   * <p>Factory accounting processors.</p>
   **/
  private FactoryBldAccServices<RS> factoryBldAccServices;

  /**
   * <p>Get other bean in lazy mode (if bean is null then initialize it).
   * This method invoked if factoryAppBeans.beansMap doesn't contains
   * of required bean, and this map is already locked.</p>
   * @param pBeanName - bean name
   * @return Object - requested bean or null
   * @throws Exception - an exception
   */
  @Override
  public final Object lazyGet(
    final String pBeanName) throws Exception {
    if (getHndTrdSmpReqName().equals(pBeanName)) {
      return lazyGetHndTrdSmpReq();
    } else if (getHndTrdTrnsReqName().equals(pBeanName)) {
      return lazyGetHndTrdTrnsReq();
    } else if (getEntryDateFormatterName().equals(pBeanName)) {
      return lazyGetEntryDateFormatter();
    } else if (getSrvReqBalanceSheetName().equals(pBeanName)) {
      return lazyGetSrvReqBalanceSheet();
    } else if (getSrvBalanceName().equals(pBeanName)) {
      return lazyGetSrvBalanceStd();
    } else if (getSrvWarehouseRestsName().equals(pBeanName)) {
      return lazyGetSrvWarehouseRests();
    } else if (getSrvWarehouseSiteRestsName().equals(pBeanName)) {
      return lazyGetSrvWarehouseSiteRests();
    } else if (getSrvLedgerName().equals(pBeanName)) {
      return lazyGetSrvLedger();
    } else if (getSrvWarehouseEntryName().equals(pBeanName)) {
      return lazyGetSrvWarehouseEntry();
    } else if (getSrvAccEntryName().equals(pBeanName)) {
      return lazyGetSrvAccEntry();
    } else if (getSrvUseMaterialEntryName().equals(pBeanName)) {
      return lazyGetSrvUseMaterialEntry();
    } else if (getSrvCogsEntryName().equals(pBeanName)) {
      return lazyGetSrvCogsEntry();
    } else if (getSrvSettingsAddName().equals(pBeanName)) {
      return lazyGetSrvSettingsAdd();
    } else if (getSrvTradingSettingsName().equals(pBeanName)) {
      return lazyGetSrvTradingSettings();
    } else if (getSrvAccSettingsName().equals(pBeanName)) {
      return lazyGetSrvAccSettings();
    } else if (getSrvTypeCodeSubaccName().equals(pBeanName)) {
      return lazyGetSrvTypeCodeSubacc();
    } else if (getSrvTypeCodeAccSourcesName().equals(pBeanName)) {
      return lazyGetSrvTypeCodeAccSources();
    } else if (getReplicatorTaxMarketName().equals(pBeanName)) {
      return lazyGetReplicatorTaxMarket();
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no bean with name " + pBeanName);
    }
  }

  /**
   * <p>Release beans (memory). This is "memory friendly" factory</p>
   * @throws Exception - an exception
   */
  @Override
  public final void releaseBeans() throws Exception {
    // nothing cause all beans are in [main factory].beanMap
  }

  /**
   * <p>Lazy get webstore SimpleRequestHandler.</p>
   * @return requested SimpleRequestHandler
   * @throws Exception - an exception
   */
  public final SimpleRequestHandler
    lazyGetHndTrdSmpReq() throws Exception {
    String beanName = getHndTrdSmpReqName();
    SimpleRequestHandler hndTrdSmpReq =
      (SimpleRequestHandler)
        this.factoryAppBeans.getBeansMap().get(beanName);
    if (hndTrdSmpReq == null) {
      hndTrdSmpReq = new SimpleRequestHandler();
      hndTrdSmpReq.setProcessorsFactory(this
        .factoryBldAccServices.lazyGetFctBnTradeProcessors());
      //assigning fully initialized object:
      this.factoryAppBeans.getBeansMap().put(beanName, hndTrdSmpReq);
    }
    return hndTrdSmpReq;
  }

  /**
   * <p>Get webstore SimpleRequestHandler name.</p>
   * @return SimpleRequestHandler name
   */
  public final String getHndTrdSmpReqName() {
    return "hndTrdSmpReq";
  }

  /**
   * <p>Lazy get webstore TransactionalRequestHandler.</p>
   * @return requested TransactionalRequestHandler
   * @throws Exception - an exception
   */
  public final TransactionalRequestHandler<RS>
    lazyGetHndTrdTrnsReq() throws Exception {
    String beanName = getHndTrdTrnsReqName();
    @SuppressWarnings("unchecked")
    TransactionalRequestHandler<RS> hndTrdTrnsReq =
      (TransactionalRequestHandler<RS>)
        this.factoryAppBeans.getBeansMap().get(beanName);
    if (hndTrdTrnsReq == null) {
      hndTrdTrnsReq = new TransactionalRequestHandler<RS>();
      hndTrdTrnsReq
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      hndTrdTrnsReq.setProcessorsFactory(this
        .factoryBldAccServices.lazyGetFctBnTradeProcessors());
      //assigning fully initialized object:
      this.factoryAppBeans.getBeansMap().put(beanName, hndTrdTrnsReq);
    }
    return hndTrdTrnsReq;
  }

  /**
   * <p>Get webstore TransactionalRequestHandler name.</p>
   * @return TransactionalRequestHandler name
   */
  public final String getHndTrdTrnsReqName() {
    return "hndTrdTrnsReq";
  }

  /**
   * <p>Get entryDateFormatter in lazy mode.</p>
   * @return entryDateFormatter for derived entry description
   * @throws Exception - an exception
   */
  public final DateFormat
    lazyGetEntryDateFormatter() throws Exception {
    String beanName = getEntryDateFormatterName();
    DateFormat entryDateFormatter = (DateFormat)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (entryDateFormatter == null) {
      entryDateFormatter = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
      this.factoryAppBeans.getBeansMap().put(beanName, entryDateFormatter);
    }
    return entryDateFormatter;
  }

  /**
   * <p>Get entryDateFormatter name.</p>
   * @return entryDateFormatter name
   */
  public final String getEntryDateFormatterName() {
    return "entryDateFormatter";
  }

  /**
   * <p>Get SrvBalanceStd in lazy mode.</p>
   * @return SrvBalanceStd - SrvBalanceStd
   * @throws Exception - an exception
   */
  public final SrvBalanceStd<RS>
    lazyGetSrvBalanceStd() throws Exception {
    String beanName = getSrvBalanceName();
    @SuppressWarnings("unchecked")
    SrvBalanceStd<RS> srvBalanceStd = (SrvBalanceStd<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvBalanceStd == null) {
      srvBalanceStd = new SrvBalanceStd<RS>(this.factoryAppBeans
        .lazyGetSrvOrm(), this.factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvAccSettings(), this.factoryAppBeans.lazyGetLogger());
      this.factoryAppBeans.getBeansMap().put(beanName, srvBalanceStd);
    }
    return srvBalanceStd;
  }

  /**
   * <p>Get ISrvBalance name.</p>
   * @return ISrvBalance name
   */
  public final String getSrvBalanceName() {
    return "ISrvBalance";
  }

  /**
   * <p>Get SrvWarehouseRests in lazy mode.</p>
   * @return SrvWarehouseRests - SrvWarehouseRests
   * @throws Exception - an exception
   */
  public final SrvWarehouseRests<RS>
    lazyGetSrvWarehouseRests() throws Exception {
    String beanName = getSrvWarehouseRestsName();
    @SuppressWarnings("unchecked")
    SrvWarehouseRests<RS> srvWarehouseRests = (SrvWarehouseRests<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvWarehouseRests == null) {
      srvWarehouseRests = new SrvWarehouseRests<RS>(factoryAppBeans
        .lazyGetSrvDatabase());
      this.factoryAppBeans.getBeansMap().put(beanName, srvWarehouseRests);
    }
    return srvWarehouseRests;
  }

  /**
   * <p>Get ISrvWarehouseRests name.</p>
   * @return ISrvWarehouseRests name
   */
  public final String getSrvWarehouseRestsName() {
    return "ISrvWarehouseRests";
  }

  /**
   * <p>Get SrvWarehouseSiteRests in lazy mode.</p>
   * @return SrvWarehouseSiteRests - SrvWarehouseSiteRests
   * @throws Exception - an exception
   */
  public final SrvWarehouseSiteRests<RS>
    lazyGetSrvWarehouseSiteRests() throws Exception {
    String beanName = getSrvWarehouseSiteRestsName();
    @SuppressWarnings("unchecked")
    SrvWarehouseSiteRests<RS> srvWarehouseSiteRests =
      (SrvWarehouseSiteRests<RS>)
        this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvWarehouseSiteRests == null) {
      srvWarehouseSiteRests = new SrvWarehouseSiteRests<RS>(
        this.factoryAppBeans.lazyGetSrvDatabase());
      this.factoryAppBeans.getBeansMap().put(beanName, srvWarehouseSiteRests);
    }
    return srvWarehouseSiteRests;
  }

  /**
   * <p>Get ISrvWarehouseSiteRests name.</p>
   * @return ISrvWarehouseSiteRests name
   */
  public final String getSrvWarehouseSiteRestsName() {
    return "ISrvWarehouseSiteRests";
  }

  /**
   * <p>Get SrvLedger in lazy mode.</p>
   * @return SrvLedger - SrvLedger
   * @throws Exception - an exception
   */
  public final SrvLedger<RS>
    lazyGetSrvLedger() throws Exception {
    String beanName = getSrvLedgerName();
    @SuppressWarnings("unchecked")
    SrvLedger<RS> srvLedger = (SrvLedger<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvLedger == null) {
      srvLedger = new SrvLedger<RS>(this.factoryAppBeans
        .lazyGetSrvDatabase(), lazyGetSrvAccSettings(), lazyGetSrvBalanceStd());
      this.factoryAppBeans.getBeansMap().put(beanName, srvLedger);
    }
    return srvLedger;
  }

  /**
   * <p>Get ISrvLedger name.</p>
   * @return ISrvLedger name
   */
  public final String getSrvLedgerName() {
    return "ISrvLedger";
  }

  /**
   * <p>Get SrvWarehouseEntry in lazy mode.</p>
   * @return SrvWarehouseEntry - SrvWarehouseEntry
   * @throws Exception - an exception
   */
  public final SrvWarehouseEntry<RS>
    lazyGetSrvWarehouseEntry() throws Exception {
    String beanName = getSrvWarehouseEntryName();
    @SuppressWarnings("unchecked")
    SrvWarehouseEntry<RS> srvWarehouseEntry = (SrvWarehouseEntry<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvWarehouseEntry == null) {
      srvWarehouseEntry =
        new SrvWarehouseEntry<RS>(this.factoryAppBeans.lazyGetSrvOrm(),
          lazyGetSrvTypeCodeAccSources(),
            this.factoryAppBeans.lazyGetSrvI18n(), lazyGetEntryDateFormatter());
      this.factoryAppBeans.getBeansMap().put(beanName, srvWarehouseEntry);
    }
    return srvWarehouseEntry;
  }

  /**
   * <p>Get ISrvWarehouseEntry name.</p>
   * @return ISrvWarehouseEntry name
   */
  public final String getSrvWarehouseEntryName() {
    return "ISrvWarehouseEntry";
  }

  /**
   * <p>Get SrvAccEntry in lazy mode.</p>
   * @return SrvAccEntry - SrvAccEntry
   * @throws Exception - an exception
   */
  public final SrvAccEntry<RS> lazyGetSrvAccEntry()
    throws Exception {
    String beanName = getSrvAccEntryName();
    @SuppressWarnings("unchecked")
    SrvAccEntry<RS> srvAccEntry = (SrvAccEntry<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvAccEntry == null) {
      srvAccEntry = new SrvAccEntry<RS>(this.factoryAppBeans
        .lazyGetSrvOrm(), this.factoryAppBeans.lazyGetSrvDatabase(),
          lazyGetSrvTypeCodeAccSources(), lazyGetSrvAccSettings(),
            lazyGetSrvBalanceStd(), this.factoryAppBeans.lazyGetSrvI18n(),
              lazyGetEntryDateFormatter());
      this.factoryAppBeans.getBeansMap().put(beanName, srvAccEntry);
    }
    return srvAccEntry;
  }

  /**
   * <p>Get ISrvAccEntry name.</p>
   * @return ISrvAccEntry name
   */
  public final String getSrvAccEntryName() {
    return "ISrvAccEntry";
  }

  /**
   * <p>Get SrvUseMaterialEntry in lazy mode.</p>
   * @return SrvUseMaterialEntry - SrvUseMaterialEntry
   * @throws Exception - an exception
   */
  public final SrvUseMaterialEntry<RS>
    lazyGetSrvUseMaterialEntry() throws Exception {
    String beanName = getSrvUseMaterialEntryName();
    @SuppressWarnings("unchecked")
    SrvUseMaterialEntry<RS> srvUseMaterialEntry = (SrvUseMaterialEntry<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvUseMaterialEntry == null) {
      srvUseMaterialEntry = new SrvUseMaterialEntry<RS>();
      srvUseMaterialEntry.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      srvUseMaterialEntry
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      srvUseMaterialEntry.setEntitiesFactoriesFatory(
        this.factoryBldAccServices.lazyGetFctBcFctSimpleEntities());
      srvUseMaterialEntry.setSrvTypeCode(lazyGetSrvTypeCodeAccSources());
      srvUseMaterialEntry.setSrvAccSettings(lazyGetSrvAccSettings());
      srvUseMaterialEntry.setSrvI18n(this.factoryAppBeans.lazyGetSrvI18n());
      srvUseMaterialEntry.setDateFormatter(lazyGetEntryDateFormatter());
      srvUseMaterialEntry
        .setSettersRapiHolder(this.factoryAppBeans.lazyGetHolderRapiSetters());
      this.factoryAppBeans.getBeansMap().put(beanName, srvUseMaterialEntry);
    }
    return srvUseMaterialEntry;
  }

  /**
   * <p>Get ISrvUseMaterialEntry name.</p>
   * @return ISrvUseMaterialEntry name
   */
  public final String getSrvUseMaterialEntryName() {
    return "ISrvUseMaterialEntry";
  }

  /**
   * <p>Get SrvCogsEntry in lazy mode.</p>
   * @return SrvCogsEntry - SrvCogsEntry
   * @throws Exception - an exception
   */
  public final SrvCogsEntry<RS>
    lazyGetSrvCogsEntry() throws Exception {
    String beanName = getSrvCogsEntryName();
    @SuppressWarnings("unchecked")
    SrvCogsEntry<RS> srvCogsEntry = (SrvCogsEntry<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvCogsEntry == null) {
      srvCogsEntry = new SrvCogsEntry<RS>();
      srvCogsEntry.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      srvCogsEntry.setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      srvCogsEntry.setSrvTypeCode(lazyGetSrvTypeCodeAccSources());
      srvCogsEntry.setEntitiesFactoriesFatory(
        this.factoryBldAccServices.lazyGetFctBcFctSimpleEntities());
      srvCogsEntry.setSrvAccSettings(lazyGetSrvAccSettings());
      srvCogsEntry.setSrvI18n(this.factoryAppBeans.lazyGetSrvI18n());
      srvCogsEntry.setDateFormatter(lazyGetEntryDateFormatter());
      srvCogsEntry
        .setSettersRapiHolder(this.factoryAppBeans.lazyGetHolderRapiSetters());
      this.factoryAppBeans.getBeansMap().put(beanName, srvCogsEntry);
    }
    return srvCogsEntry;
  }

  /**
   * <p>Get ISrvCogsEntry name.</p>
   * @return ISrvCogsEntry name
   */
  public final String getSrvCogsEntryName() {
    return "ISrvCogsEntry";
  }

  /**
   * <p>Get SrvSettingsAdd in lazy mode.</p>
   * @return SrvSettingsAdd - SrvSettingsAdd
   * @throws Exception - an exception
   */
  public final SrvSettingsAdd<RS> lazyGetSrvSettingsAdd()
    throws Exception {
    String beanName = getSrvSettingsAddName();
    @SuppressWarnings("unchecked")
    SrvSettingsAdd<RS> srvSettingsAdd = (SrvSettingsAdd<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvSettingsAdd == null) {
      srvSettingsAdd =
        new SrvSettingsAdd<RS>(this.factoryAppBeans.lazyGetSrvOrm());
      this.factoryAppBeans.getBeansMap().put(beanName, srvSettingsAdd);
    }
    return srvSettingsAdd;
  }

  /**
   * <p>Get ISrvSettingsAdd name.</p>
   * @return ISrvSettingsAdd name
   */
  public final String getSrvSettingsAddName() {
    return "ISrvSettingsAdd";
  }

  /**
   * <p>Get SrvTradingSettings in lazy mode.</p>
   * @return SrvTradingSettings - SrvTradingSettings
   * @throws Exception - an exception
   */
  public final SrvTradingSettings<RS> lazyGetSrvTradingSettings()
    throws Exception {
    String beanName = getSrvTradingSettingsName();
    @SuppressWarnings("unchecked")
    SrvTradingSettings<RS> srvTradingSettings = (SrvTradingSettings<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvTradingSettings == null) {
      srvTradingSettings =
        new SrvTradingSettings<RS>(this.factoryAppBeans.lazyGetSrvOrm());
      this.factoryAppBeans.getBeansMap().put(beanName, srvTradingSettings);
    }
    return srvTradingSettings;
  }

  /**
   * <p>Get ISrvTradingSettings name.</p>
   * @return ISrvTradingSettings name
   */
  public final String getSrvTradingSettingsName() {
    return "ISrvTradingSettings";
  }

  /**
   * <p>Get SrvAccSettings in lazy mode.</p>
   * @return SrvAccSettings - SrvAccSettings
   * @throws Exception - an exception
   */
  public final SrvAccSettings<RS> lazyGetSrvAccSettings()
    throws Exception {
    String beanName = getSrvAccSettingsName();
    @SuppressWarnings("unchecked")
    SrvAccSettings<RS> srvAccSettings = (SrvAccSettings<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvAccSettings == null) {
      srvAccSettings =
        new SrvAccSettings<RS>(this.factoryAppBeans.lazyGetSrvOrm());
      this.factoryAppBeans.getBeansMap().put(beanName, srvAccSettings);
    }
    return srvAccSettings;
  }

  /**
   * <p>Get ISrvAccSettings name.</p>
   * @return ISrvAccSettings name
   */
  public final String getSrvAccSettingsName() {
    return "ISrvAccSettings";
  }

  /**
   * <p>Get ReplicatorXmlHttp tax-market in lazy mode.</p>
   * @return ReplicatorXmlHttp - ReplicatorXmlHttp
   * @throws Exception - an exception
   */
  public final ReplicatorXmlHttp<RS>
    lazyGetReplicatorTaxMarket() throws Exception {
    String beanName = getReplicatorTaxMarketName();
    @SuppressWarnings("unchecked")
    ReplicatorXmlHttp<RS> srvReplTaxMarketXml =
      (ReplicatorXmlHttp<RS>) this.factoryAppBeans.getBeansMap()
        .get(beanName);
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
      @SuppressWarnings("unchecked")
      PrcReplicationAccMethodSave<RS> prcReplicationAccMethodSave =
        (PrcReplicationAccMethodSave<RS>) this.factoryBldAccServices
          .lazyGetFctBnEntitiesProcessors().lazyGet(null,
          PrcReplicationAccMethodSave.class.getSimpleName());
      prcReplicationAccMethodSave
        .addReplAccMethChangedHandler(filterAvoidAccDebtCredit);
      @SuppressWarnings("unchecked")
      PrcReplExcludeAccountsDebitCreditSave<RS> prcReplExclAccDcSave =
        (PrcReplExcludeAccountsDebitCreditSave<RS>) this.factoryBldAccServices
          .lazyGetFctBnEntitiesProcessors().lazyGet(null,
          PrcReplExcludeAccountsDebitCreditSave.class.getSimpleName());
      prcReplExclAccDcSave
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
        .put(beanName, srvReplTaxMarketXml);
    }
    return srvReplTaxMarketXml;
  }

  /**
   * <p>Get replicatorTaxMarket name.</p>
   * @return replicatorTaxMarket name
   */
  public final String getReplicatorTaxMarketName() {
    return "replicatorTaxMarket";
  }

  /**
   * <p>Get DatabaseWriterXml for tax-market in lazy mode.</p>
   * @return DatabaseWriterXml - DatabaseWriterXml
   * @throws Exception - an exception
   */
  public final DatabaseWriterXml<RS> lazyGetDbWriterReplTaxMarket(
    ) throws Exception {
    String beanName = getDbWriterXmlReplTaxMarketName();
    @SuppressWarnings("unchecked")
    DatabaseWriterXml<RS> dbWriterXmlReplTaxMarket =
      (DatabaseWriterXml<RS>) this.factoryAppBeans.getBeansMap()
        .get(beanName);
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
        .put(beanName, dbWriterXmlReplTaxMarket);
    }
    return dbWriterXmlReplTaxMarket;
  }

  /**
   * <p>Get dbWriterXmlReplTaxMarket name.</p>
   * @return dbWriterXmlReplTaxMarket name
   */
  public final String getDbWriterXmlReplTaxMarketName() {
    return "dbWriterXmlReplTaxMarket";
  }

  /**
   * <p>Get MngSettings for replicator tax-market acc in lazy mode.</p>
   * @return MngSettings - MngSettings replicator
   * @throws Exception - an exception
   */
  public final MngSettings lazyGetMngSettingsReplTaxMarket(
    ) throws Exception {
    String beanName = getMngSettingsReplTaxMarketName();
    MngSettings mngSettingsReplTaxMarket =
      (MngSettings) this.factoryAppBeans.getBeansMap()
        .get(beanName);
    if (mngSettingsReplTaxMarket == null) {
      mngSettingsReplTaxMarket = new MngSettings();
      mngSettingsReplTaxMarket.setLogger(this.factoryAppBeans.lazyGetLogger());
      mngSettingsReplTaxMarket
        .loadConfiguration("tax-to-market", "base.xml");
      this.factoryAppBeans.lazyGetLogger().info(FactoryAccServices.class,
        "MngSettings tax-market replicator has been created.");
      this.factoryAppBeans.getBeansMap()
        .put(beanName, mngSettingsReplTaxMarket);
    }
    return mngSettingsReplTaxMarket;
  }

  /**
   * <p>Get mngSettingsReplTaxMarket name.</p>
   * @return mngSettingsReplTaxMarket name
   */
  public final String getMngSettingsReplTaxMarketName() {
    return "mngSettingsReplTaxMarket";
  }

  /**
   * <p>Get SrvReqBalanceSheet  in lazy mode.</p>
   * @return SrvReqBalanceSheet - SrvReqBalanceSheet
   * @throws Exception - an exception
   */
  public final SrvReqBalanceSheet<RS> lazyGetSrvReqBalanceSheet(
    ) throws Exception {
    String beanName = getSrvReqBalanceSheetName();
    @SuppressWarnings("unchecked")
    SrvReqBalanceSheet<RS> srvReqBalanceSheet =
      (SrvReqBalanceSheet<RS>) this.factoryAppBeans.getBeansMap()
        .get(beanName);
    if (srvReqBalanceSheet == null) {
      srvReqBalanceSheet = new SrvReqBalanceSheet<RS>();
      srvReqBalanceSheet.setSrvDatabase(this.factoryAppBeans
        .lazyGetSrvDatabase());
      srvReqBalanceSheet.setSrvDate(this.factoryAppBeans
        .lazyGetSrvDate());
      srvReqBalanceSheet.setSrvAccSettings(lazyGetSrvAccSettings());
      SrvBalanceSheet<RS> srvBalanceSheet = new SrvBalanceSheet<RS>(this
        .factoryAppBeans.lazyGetSrvDatabase(), lazyGetSrvAccSettings(),
          lazyGetSrvBalanceStd());
      srvReqBalanceSheet.setSrvBalanceSheet(srvBalanceSheet);
      this.factoryAppBeans.lazyGetLogger().info(FactoryAccServices.class,
        "SrvReqBalanceSheet has been created.");
      this.factoryAppBeans.getBeansMap()
        .put(beanName, srvReqBalanceSheet);
    }
    return srvReqBalanceSheet;
  }

  /**
   * <p>Get srvReqBalanceSheet name.</p>
   * @return srvReqBalanceSheet name
   */
  public final String getSrvReqBalanceSheetName() {
    return "srvReqBalanceSheet";
  }

  /**
   * <p>Get SrvTypeCodeSubacc in lazy mode.</p>
   * @return SrvTypeCodeSubacc - SrvTypeCodeSubacc
   * @throws Exception - an exception
   */
  public final SrvTypeCodeSubacc lazyGetSrvTypeCodeSubacc()
    throws Exception {
    String beanName = getSrvTypeCodeSubaccName();
    SrvTypeCodeSubacc srvTypeCodeSubacc = (SrvTypeCodeSubacc)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvTypeCodeSubacc == null) {
      srvTypeCodeSubacc = new SrvTypeCodeSubacc();
      this.factoryAppBeans.getBeansMap().put(beanName, srvTypeCodeSubacc);
    }
    return srvTypeCodeSubacc;
  }

  /**
   * <p>Get srvTypeCodeSubacc name.</p>
   * @return srvTypeCodeSubacc name
   */
  public final String getSrvTypeCodeSubaccName() {
    return "srvTypeCodeSubacc";
  }

  /**
   * <p>Get SrvTypeCodeAccSources in lazy mode.</p>
   * @return SrvTypeCodeAccSources - SrvTypeCodeAccSources
   * @throws Exception - an exception
   */
  public final SrvTypeCodeAccSources lazyGetSrvTypeCodeAccSources()
    throws Exception {
    String beanName = getSrvTypeCodeAccSourcesName();
    SrvTypeCodeAccSources srvTypeCodeAccSources = (SrvTypeCodeAccSources)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (srvTypeCodeAccSources == null) {
      srvTypeCodeAccSources = new SrvTypeCodeAccSources();
      this.factoryAppBeans.getBeansMap().put(beanName, srvTypeCodeAccSources);
    }
    return srvTypeCodeAccSources;
  }

  /**
   * <p>Get srvTypeCodeAccSources name.</p>
   * @return srvTypeCodeAccSources name
   */
  public final String getSrvTypeCodeAccSourcesName() {
    return "srvTypeCodeAccSources";
  }

  //SGS:
  /**
   * <p>Getter for factoryAppBeans.</p>
   * @return AFactoryAppBeans<RS>
   **/
  public final AFactoryAppBeans<RS> getFactoryAppBeans() {
    return this.factoryAppBeans;
  }

  /**
   * <p>Setter for factoryAppBeans.</p>
   * @param pFactoryAppBeans reference
   **/
  public final void setFactoryAppBeans(
    final AFactoryAppBeans<RS> pFactoryAppBeans) {
    this.factoryAppBeans = pFactoryAppBeans;
  }

  /**
   * <p>Getter for factoryBldAccServices.</p>
   * @return FactoryBldAccServices<RS>
   **/
  public final FactoryBldAccServices<RS> getFactoryBldAccServices() {
    return this.factoryBldAccServices;
  }

  /**
   * <p>Setter for factoryBldAccServices.</p>
   * @param pFactoryBldAccServices reference
   **/
  public final void setFactoryBldAccServices(
    final FactoryBldAccServices<RS> pFactoryBldAccServices) {
    this.factoryBldAccServices = pFactoryBldAccServices;
  }
}
