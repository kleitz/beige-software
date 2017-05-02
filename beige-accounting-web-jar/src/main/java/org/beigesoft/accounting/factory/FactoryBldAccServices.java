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

import org.beigesoft.handler.HandlerEntityRequest;
import org.beigesoft.orm.factory.FctBnProcessors;
import org.beigesoft.orm.factory.FctBnEntitiesProcessors;
import org.beigesoft.web.factory.IFactoryBldServices;
import org.beigesoft.web.factory.AFactoryAppBeans;
import org.beigesoft.accounting.holder.HldAccProcessorNames;
import org.beigesoft.accounting.holder.HldAccEntitiesProcessorNames;
import org.beigesoft.webstore.holder.HldTradeProcessorNames;
import org.beigesoft.webstore.holder.HldTradeEntitiesProcessorNames;
import org.beigesoft.webstore.factory.FctBnTradeProcessors;
import org.beigesoft.webstore.factory.FctBnTradeEntitiesProcessors;

/**
 * <p>Sub-factory of business-logic dependent services.
 * It used inside main-factory.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FactoryBldAccServices<RS> implements IFactoryBldServices<RS> {

  /**
   * <p>Factory app-beans.</p>
   **/
  private AFactoryAppBeans<RS> factoryAppBeans;

  /**
   * <p>Factory accounting-beans.</p>
   **/
  private FactoryAccServices<RS> factoryAccServices;

  /**
   * <p>Get HandlerEntityRequest in lazy mode.</p>
   * @return HandlerEntityRequest - HandlerEntityRequest
   * @throws Exception - an exception
   */
  @Override
  public final HandlerEntityRequest<RS>
    lazyGetHandlerEntityRequest() throws Exception {
    String beanName = this.factoryAppBeans.getHandlerEntityRequestName();
    @SuppressWarnings("unchecked")
    HandlerEntityRequest<RS> hndlEntityReq = (HandlerEntityRequest<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (hndlEntityReq == null) {
      hndlEntityReq = new HandlerEntityRequest<RS>();
      hndlEntityReq
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      hndlEntityReq.setFillEntityFromReq(
        this.factoryAppBeans.lazyGetFillEntityFromReq());
      hndlEntityReq
        .setEntitiesFactoriesFatory(lazyGetFctBcFctSimpleEntities());
      hndlEntityReq
        .setProcessorsFactory(lazyGetFctBnProcessors());
      HldAccProcessorNames hldAccProcessorNames = new HldAccProcessorNames();
      hldAccProcessorNames
        .setHldAddProcessorNames(new HldTradeProcessorNames());
      hndlEntityReq
        .setProcessorsNamesHolder(hldAccProcessorNames);
      hndlEntityReq
        .setEntitiesProcessorsFactory(lazyGetFctBnEntitiesProcessors());
      HldAccEntitiesProcessorNames hldAccEntitiesProcessorNames =
        new HldAccEntitiesProcessorNames();
      hldAccEntitiesProcessorNames.setHldAddEntitiesProcessorNames(
        new HldTradeEntitiesProcessorNames());
      hndlEntityReq
        .setEntitiesProcessorsNamesHolder(hldAccEntitiesProcessorNames);
      hndlEntityReq.setEntitiesMap(this.factoryAppBeans.getEntitiesMap());
      this.factoryAppBeans.getBeansMap().put(beanName, hndlEntityReq);
      this.factoryAppBeans.lazyGetLogger().info(AFactoryAppBeans.class,
        beanName + " has been created.");
    }
    return hndlEntityReq;
  }

  /**
   * <p>Get FctBcFctSimpleAccEntities in lazy mode.</p>
   * @return FctBcFctSimpleAccEntities - FctBcFctSimpleAccEntities
   * @throws Exception - an exception
   */
  @Override
  public final FctBcFctSimpleAccEntities
    lazyGetFctBcFctSimpleEntities() throws Exception {
    String beanName = this.factoryAppBeans.getFctBcFctSimpleEntitiesName();
    FctBcFctSimpleAccEntities fctBcFctSimpleEntities =
      (FctBcFctSimpleAccEntities) this.factoryAppBeans.getBeansMap()
        .get(beanName);
    if (fctBcFctSimpleEntities == null) {
      fctBcFctSimpleEntities = new FctBcFctSimpleAccEntities();
      fctBcFctSimpleEntities
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      this.factoryAppBeans.getBeansMap().put(beanName, fctBcFctSimpleEntities);
      this.factoryAppBeans.lazyGetLogger().info(AFactoryAppBeans.class,
        beanName + " has been created.");
    }
    return fctBcFctSimpleEntities;
  }

  /**
   * <p>Get FctBnAccEntitiesProcessors in lazy mode.</p>
   * @return FctBnAccEntitiesProcessors - FctBnAccEntitiesProcessors
   * @throws Exception - an exception
   */
  @Override
  public final FctBnAccEntitiesProcessors<RS>
    lazyGetFctBnEntitiesProcessors() throws Exception {
    String beanName = getFctBnAccEntitiesProcessorsName();
    @SuppressWarnings("unchecked")
    FctBnAccEntitiesProcessors<RS> fctBnAccEntitiesProcessors =
      (FctBnAccEntitiesProcessors<RS>)
        this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBnAccEntitiesProcessors == null) {
      fctBnAccEntitiesProcessors = new FctBnAccEntitiesProcessors<RS>();
      fctBnAccEntitiesProcessors.setFactoryAppBeans(getFactoryAppBeans());
      fctBnAccEntitiesProcessors.setFctBnEntitiesProcessors(
        lazyGetFctBnEntitiesProcessorsBase());
      fctBnAccEntitiesProcessors
        .setSrvTypeCode(this.factoryAccServices.lazyGetSrvTypeCodeSubacc());
      fctBnAccEntitiesProcessors
        .setSrvBalance(this.factoryAccServices.lazyGetSrvBalanceStd());
      fctBnAccEntitiesProcessors
        .setSrvAccSettings(this.factoryAccServices.lazyGetSrvAccSettings());
      fctBnAccEntitiesProcessors
        .setSrvAccEntry(this.factoryAccServices.lazyGetSrvAccEntry());
      fctBnAccEntitiesProcessors.setSrvWarehouseEntry(this
        .factoryAccServices.lazyGetSrvWarehouseEntry());
      fctBnAccEntitiesProcessors
        .setSrvCogsEntry(this.factoryAccServices.lazyGetSrvCogsEntry());
      fctBnAccEntitiesProcessors.setSrvUseMaterialEntry(
        this.factoryAccServices.lazyGetSrvUseMaterialEntry());
      fctBnAccEntitiesProcessors
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      fctBnAccEntitiesProcessors
        .setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      fctBnAccEntitiesProcessors
        .setMngUvdSettings(this.factoryAppBeans.lazyGetMngUvdSettings());
      fctBnAccEntitiesProcessors
        .setSrvDate(this.factoryAppBeans.lazyGetSrvDate());
      fctBnAccEntitiesProcessors.setFieldConverterNamesHolder(
        this.factoryAppBeans.lazyGetHldFieldsCnvTfsNames());
      fctBnAccEntitiesProcessors.setConvertersFieldsFatory(
        this.factoryAppBeans.lazyGetFctConvertersToFromString());
      fctBnAccEntitiesProcessors
        .setSrvI18n(this.factoryAppBeans.lazyGetSrvI18n());
      fctBnAccEntitiesProcessors
        .setDateFormatter(this.factoryAccServices.lazyGetEntryDateFormatter());
      FctBnTradeEntitiesProcessors<RS> fctBnTradeEntitiesProcessors =
        new FctBnTradeEntitiesProcessors<RS>();
      fctBnTradeEntitiesProcessors.setUploadDirectory(
        this.factoryAppBeans.getUploadDirectory());
      fctBnTradeEntitiesProcessors.setWebAppPath(
        this.factoryAppBeans.getWebAppPath());
      fctBnTradeEntitiesProcessors.setSrvSettingsAdd(
        this.factoryAccServices.lazyGetSrvSettingsAdd());
      fctBnTradeEntitiesProcessors.setSrvTradingSettings(
        this.factoryAccServices.lazyGetSrvTradingSettings());
      fctBnTradeEntitiesProcessors.setSrvAccSettings(
        this.factoryAccServices.lazyGetSrvAccSettings());
      fctBnTradeEntitiesProcessors
        .setMngUvdSettings(this.factoryAppBeans.lazyGetMngUvdSettings());
      fctBnTradeEntitiesProcessors
        .setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      fctBnTradeEntitiesProcessors.setFctBnEntitiesProcessors(
        lazyGetFctBnEntitiesProcessorsBase());
      fctBnAccEntitiesProcessors.setAdditionalEpf(fctBnTradeEntitiesProcessors);
      this.factoryAppBeans.getBeansMap()
        .put(beanName, fctBnAccEntitiesProcessors);
    }
    return fctBnAccEntitiesProcessors;
  }

  /**
   * <p>Get fctBnAccEntitiesProcessors name.</p>
   * @return fctBnAccEntitiesProcessors name
   */
  public final String getFctBnAccEntitiesProcessorsName() {
    return "fctBnAccEntitiesProcessors";
  }

  /**
   * <p>Get FctBnAccProcessors in lazy mode.</p>
   * @return FctBnAccProcessors - FctBnAccProcessors
   * @throws Exception - an exception
   */
  @Override
  public final FctBnAccProcessors<RS>
    lazyGetFctBnProcessors() throws Exception {
    String beanName = getFctBnAccProcessorsName();
    @SuppressWarnings("unchecked")
    FctBnAccProcessors<RS> fctBnAccProcessors = (FctBnAccProcessors<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBnAccProcessors == null) {
      fctBnAccProcessors = new FctBnAccProcessors<RS>();
      fctBnAccProcessors
        .setSrvAccSettings(this.factoryAccServices.lazyGetSrvAccSettings());
      fctBnAccProcessors
        .setSrvTypeCode(this.factoryAccServices.lazyGetSrvTypeCodeSubacc());
      fctBnAccProcessors.setFctBnProcessors(lazyGetFctBnProcessorsBase());
      fctBnAccProcessors.setAdditionalPf(lazyGetFctBnTradeProcessors());
      this.factoryAppBeans.getBeansMap().put(beanName, fctBnAccProcessors);
    }
    return fctBnAccProcessors;
  }

  /**
   * <p>Get fctBnAccProcessors name.</p>
   * @return fctBnAccProcessors name
   */
  public final String getFctBnAccProcessorsName() {
    return "fctBnAccProcessors";
  }

  /**
   * <p>Get FctBnTradeProcessors in lazy mode.</p>
   * @return FctBnTradeProcessors - FctBnTradeProcessors
   * @throws Exception - an exception
   */
  public final FctBnTradeProcessors<RS>
    lazyGetFctBnTradeProcessors() throws Exception {
    String beanName = getFctBnTradeProcessorsName();
    @SuppressWarnings("unchecked")
    FctBnTradeProcessors<RS> fctBnTradeProcessors = (FctBnTradeProcessors<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBnTradeProcessors == null) {
      fctBnTradeProcessors = new FctBnTradeProcessors<RS>();
      fctBnTradeProcessors.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      fctBnTradeProcessors
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      fctBnTradeProcessors.setSrvEntitiesPage(this
        .factoryAppBeans.lazyGetSrvEntitiesPage());
      fctBnTradeProcessors.setSrvPage(
        this.factoryAppBeans.lazyGetSrvPage());
      fctBnTradeProcessors.setMngUvdSettings(
        this.factoryAppBeans.lazyGetMngUvdSettings());
      fctBnTradeProcessors.setSrvSettingsAdd(
        this.factoryAccServices.lazyGetSrvSettingsAdd());
      fctBnTradeProcessors.setSrvTradingSettings(
        this.factoryAccServices.lazyGetSrvTradingSettings());
      fctBnTradeProcessors.setSrvAccSettings(
        this.factoryAccServices.lazyGetSrvAccSettings());
      fctBnTradeProcessors.setFctBnProcessors(lazyGetFctBnProcessorsBase());
      this.factoryAppBeans.getBeansMap().put(beanName, fctBnTradeProcessors);
    }
    return fctBnTradeProcessors;
  }

  /**
   * <p>Get fctBnTradeProcessors name.</p>
   * @return fctBnTradeProcessors name
   */
  public final String getFctBnTradeProcessorsName() {
    return "fctBnTradeProcessors";
  }

  /**
   * <p>Get FctBnEntitiesProcessors in lazy mode.</p>
   * @return FctBnEntitiesProcessors - FctBnEntitiesProcessors
   * @throws Exception - an exception
   */
  public final FctBnEntitiesProcessors<RS>
    lazyGetFctBnEntitiesProcessorsBase() throws Exception {
    String beanName = getFctBnEntitiesProcessorsName();
    @SuppressWarnings("unchecked")
    FctBnEntitiesProcessors<RS> fctBnEntitiesProcessors =
      (FctBnEntitiesProcessors<RS>)
        this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBnEntitiesProcessors == null) {
      fctBnEntitiesProcessors = new FctBnEntitiesProcessors<RS>();
      fctBnEntitiesProcessors.setSrvOrm(this.factoryAppBeans.lazyGetSrvOrm());
      fctBnEntitiesProcessors
        .setMngUvdSettings(this.factoryAppBeans.lazyGetMngUvdSettings());
      fctBnEntitiesProcessors
        .setEntitiesFactoriesFatory(lazyGetFctBcFctSimpleEntities());
      fctBnEntitiesProcessors.setFillersFieldsFactory(
        this.factoryAppBeans.lazyGetFctFillersObjectFields());
      fctBnEntitiesProcessors.setUtlProperties(
        this.factoryAppBeans.lazyGetUtlProperties());
      fctBnEntitiesProcessors.setSrvDate(this.factoryAppBeans.lazyGetSrvDate());
      fctBnEntitiesProcessors.setFieldConverterNamesHolder(
        this.factoryAppBeans.lazyGetHldFieldsCnvTfsNames());
      fctBnEntitiesProcessors.setConvertersFieldsFatory(
        this.factoryAppBeans.lazyGetFctConvertersToFromString());
      fctBnEntitiesProcessors.setGettersRapiHolder(
        this.factoryAppBeans.lazyGetHolderRapiGetters());
      fctBnEntitiesProcessors.setSettersRapiHolder(
        this.factoryAppBeans.lazyGetHolderRapiSetters());
      fctBnEntitiesProcessors
        .setUploadDirectory(this.factoryAppBeans.getUploadDirectory());
      fctBnEntitiesProcessors
        .setWebAppPath(this.factoryAppBeans.getWebAppPath());
      fctBnEntitiesProcessors
        .setEmailSender(this.factoryAppBeans.lazyGetMailSender());
      this.factoryAppBeans.getBeansMap().put(beanName, fctBnEntitiesProcessors);
    }
    return fctBnEntitiesProcessors;
  }

  /**
   * <p>Get fctBnEntitiesProcessors name.</p>
   * @return fctBnEntitiesProcessors name
   */
  public final String getFctBnEntitiesProcessorsName() {
    return "fctBnEntitiesProcessors";
  }

  /**
   * <p>Get FctBnProcessors in lazy mode.</p>
   * @return FctBnProcessors - FctBnProcessors
   * @throws Exception - an exception
   */
  public final FctBnProcessors<RS>
    lazyGetFctBnProcessorsBase() throws Exception {
    String beanName = getFctBnProcessorsName();
    @SuppressWarnings("unchecked")
    FctBnProcessors<RS> fctBnProcessors = (FctBnProcessors<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBnProcessors == null) {
      fctBnProcessors = new FctBnProcessors<RS>();
      fctBnProcessors
        .setSrvEntitiesPage(this.factoryAppBeans.lazyGetSrvEntitiesPage());
      this.factoryAppBeans.getBeansMap().put(beanName, fctBnProcessors);
    }
    return fctBnProcessors;
  }

  /**
   * <p>Get fctBnProcessors name.</p>
   * @return fctBnProcessors name
   */
  public final String getFctBnProcessorsName() {
    return "fctBnProcessors";
  }

  // SGS:
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
   * <p>Getter for factoryAccServices.</p>
   * @return FactoryAccServices<RS>
   **/
  public final FactoryAccServices<RS> getFactoryAccServices() {
    return this.factoryAccServices;
  }

  /**
   * <p>Setter for factoryAccServices.</p>
   * @param pFactoryAccServices reference
   **/
  public final void setFactoryAccServices(
    final FactoryAccServices<RS> pFactoryAccServices) {
    this.factoryAccServices = pFactoryAccServices;
  }
}
