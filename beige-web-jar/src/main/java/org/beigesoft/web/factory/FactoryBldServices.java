package org.beigesoft.web.factory;

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
import org.beigesoft.orm.holder.HldProcessorNames;
import org.beigesoft.orm.holder.HldEntitiesProcessorNames;
import org.beigesoft.orm.factory.FctBnProcessors;
import org.beigesoft.orm.factory.FctBnEntitiesProcessors;
import org.beigesoft.orm.factory.FctBcFctSimpleEntities;

/**
 * <p>Sub-factory of business-logic dependent services.
 * It used inside main-factory.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FactoryBldServices<RS> implements IFactoryBldServices<RS> {

  /**
   * <p>Factory app-beans.</p>
   **/
  private AFactoryAppBeans<RS> factoryAppBeans;

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
      hndlEntityReq
        .setProcessorsNamesHolder(new HldProcessorNames());
      hndlEntityReq
        .setEntitiesProcessorsFactory(lazyGetFctBnEntitiesProcessors());
      hndlEntityReq
        .setEntitiesProcessorsNamesHolder(new HldEntitiesProcessorNames());
      hndlEntityReq.setEntitiesMap(this.factoryAppBeans.getEntitiesMap());
      this.factoryAppBeans.getBeansMap().put(beanName, hndlEntityReq);
      this.factoryAppBeans.lazyGetLogger().info(AFactoryAppBeans.class,
        beanName + " has been created.");
    }
    return hndlEntityReq;
  }

  /**
   * <p>Get FctBcFctSimpleEntities in lazy mode.</p>
   * @return FctBcFctSimpleEntities - FctBcFctSimpleEntities
   * @throws Exception - an exception
   */
  @Override
  public final FctBcFctSimpleEntities
    lazyGetFctBcFctSimpleEntities() throws Exception {
    String beanName = this.factoryAppBeans.getFctBcFctSimpleEntitiesName();
    FctBcFctSimpleEntities fctBcFctSimpleEntities =
      (FctBcFctSimpleEntities) this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBcFctSimpleEntities == null) {
      fctBcFctSimpleEntities = new FctBcFctSimpleEntities();
      fctBcFctSimpleEntities
        .setSrvDatabase(this.factoryAppBeans.lazyGetSrvDatabase());
      this.factoryAppBeans.getBeansMap().put(beanName, fctBcFctSimpleEntities);
      this.factoryAppBeans.lazyGetLogger().info(AFactoryAppBeans.class,
        beanName + " has been created.");
    }
    return fctBcFctSimpleEntities;
  }

  /**
   * <p>Get FctBnEntitiesProcessors in lazy mode.</p>
   * @return FctBnEntitiesProcessors - FctBnEntitiesProcessors
   * @throws Exception - an exception
   */
  @Override
  public final FctBnEntitiesProcessors<RS>
    lazyGetFctBnEntitiesProcessors() throws Exception {
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
  @Override
  public final FctBnProcessors<RS>
    lazyGetFctBnProcessors() throws Exception {
    String beanName = getFctBnProcessorsName();
    @SuppressWarnings("unchecked")
    FctBnProcessors<RS> fctBnProcessors = (FctBnProcessors<RS>)
      this.factoryAppBeans.getBeansMap().get(beanName);
    if (fctBnProcessors == null) {
      fctBnProcessors = new FctBnProcessors<RS>();
      fctBnProcessors.setSrvEntitiesPage(this.factoryAppBeans
        .lazyGetSrvEntitiesPage());
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
}
