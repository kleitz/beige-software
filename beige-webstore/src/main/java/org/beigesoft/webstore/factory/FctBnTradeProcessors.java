package org.beigesoft.webstore.factory;

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
import java.util.Hashtable;

import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.service.IProcessor;
import org.beigesoft.service.ISrvPage;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.processor.PrcEntitiesPage;
import org.beigesoft.orm.factory.FctBnProcessors;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.orm.service.ISrvEntitiesPage;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.webstore.service.ISrvSettingsAdd;
import org.beigesoft.webstore.processor.PrcAssignGoodsToCatalog;
import org.beigesoft.webstore.processor.PrcTradeEntitiesPage;
import org.beigesoft.webstore.processor.PrcWebstorePage;
import org.beigesoft.webstore.processor.PrcRefreshGoodsInList;
import org.beigesoft.webstore.processor.PrcDelItemFromCart;
import org.beigesoft.webstore.processor.PrcItemInCart;
import org.beigesoft.webstore.service.ISrvTradingSettings;

/**
 * <p>Trade processors factory.
 * It is inner inside ACC-PF.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctBnTradeProcessors<RS>
  implements IFactoryAppBeansByName<IProcessor> {

  /**
   * <p>Factory non-ass processors.
   * Concrete factory for concrete bean name that is bean class
   * simple name. Any way any such factory must be no abstract.</p>
   **/
  private FctBnProcessors<RS> fctBnProcessors;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Page service.</p>
   **/
  private ISrvEntitiesPage srvEntitiesPage;

  /**
   * <p>Business service for additional settings.</p>
   **/
  private ISrvSettingsAdd srvSettingsAdd;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Business service for trading settings.</p>
   **/
  private ISrvTradingSettings srvTradingSettings;

  /**
   * <p>Page service.</p>
   */
  private ISrvPage srvPage;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private IMngSettings mngUvdSettings;

  /**
   * <p>Converters map "converter name"-"object' s converter".</p>
   **/
  private final Map<String, IProcessor>
    processorsMap =
      new Hashtable<String, IProcessor>();

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanName - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IProcessor lazyGet(//NOPMD
    final Map<String, Object> pAddParam,
      final String pBeanName) throws Exception {
    IProcessor proc =
      this.processorsMap.get(pBeanName);
    if (proc == null) {
      // locking:
      synchronized (this.processorsMap) {
        // make sure again whether it's null after locking:
        proc = this.processorsMap.get(pBeanName);
        if (proc == null) {
          if (pBeanName.equals(PrcTradeEntitiesPage.class.getSimpleName())) {
            proc = lazyGetPrcTradeEntitiesPage(pAddParam);
          } else if (pBeanName.equals(PrcDelItemFromCart
            .class.getSimpleName())) {
            proc = lazyGetPrcDelItemFromCart(pAddParam);
          } else if (pBeanName.equals(PrcItemInCart
            .class.getSimpleName())) {
            proc = lazyGetPrcItemInCart(pAddParam);
          } else if (pBeanName.equals(PrcWebstorePage
            .class.getSimpleName())) {
            proc = lazyGetPrcWebstorePage(pAddParam);
          } else if (pBeanName.equals(PrcRefreshGoodsInList
            .class.getSimpleName())) {
            proc = lazyGetPrcRefreshGoodsInList(pAddParam);
          } else if (pBeanName.equals(PrcAssignGoodsToCatalog
            .class.getSimpleName())) {
            proc = lazyGetPrcAssignGoodsToCatalog(pAddParam);
          }
        }
      }
    }
    return proc;
  }

  /**
   * <p>Set bean.</p>
   * @param pBeanName - bean name
   * @param pBean bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void set(final String pBeanName,
    final IProcessor pBean) throws Exception {
    this.processorsMap.put(pBeanName, pBean);
  }

  /**
   * <p>Lazy get PrcDelItemFromCart.</p>
   * @param pAddParam additional param
   * @return requested PrcDelItemFromCart
   * @throws Exception - an exception
   */
  protected final PrcDelItemFromCart<RS> lazyGetPrcDelItemFromCart(
    final Map<String, Object> pAddParam) throws Exception {
    @SuppressWarnings("unchecked")
    PrcDelItemFromCart<RS> proc = (PrcDelItemFromCart<RS>)
      this.processorsMap
        .get(PrcDelItemFromCart.class.getSimpleName());
    if (proc == null) {
      proc = new PrcDelItemFromCart<RS>();
      proc.setPrcWebstorePage(lazyGetPrcWebstorePage(pAddParam));
      //assigning fully initialized object:
      this.processorsMap
        .put(PrcDelItemFromCart.class.getSimpleName(), proc);
    }
    return proc;
  }

  /**
   * <p>Lazy get PrcItemInCart.</p>
   * @param pAddParam additional param
   * @return requested PrcItemInCart
   * @throws Exception - an exception
   */
  protected final PrcItemInCart<RS> lazyGetPrcItemInCart(
    final Map<String, Object> pAddParam) throws Exception {
    @SuppressWarnings("unchecked")
    PrcItemInCart<RS> proc = (PrcItemInCart<RS>)
      this.processorsMap
        .get(PrcItemInCart.class.getSimpleName());
    if (proc == null) {
      proc = new PrcItemInCart<RS>();
      proc.setPrcWebstorePage(lazyGetPrcWebstorePage(pAddParam));
      //assigning fully initialized object:
      this.processorsMap
        .put(PrcItemInCart.class.getSimpleName(), proc);
    }
    return proc;
  }

  /**
   * <p>Lazy get PrcWebstorePage.</p>
   * @param pAddParam additional param
   * @return requested PrcWebstorePage
   * @throws Exception - an exception
   */
  protected final PrcWebstorePage<RS> lazyGetPrcWebstorePage(
    final Map<String, Object> pAddParam) throws Exception {
    @SuppressWarnings("unchecked")
    PrcWebstorePage<RS> proc = (PrcWebstorePage<RS>)
      this.processorsMap
        .get(PrcWebstorePage.class.getSimpleName());
    if (proc == null) {
      proc = new PrcWebstorePage<RS>();
      proc.setSrvOrm(getSrvOrm());
      proc.setSrvDatabase(getSrvDatabase());
      proc.setSrvTradingSettings(getSrvTradingSettings());
      proc.setSrvAccSettings(getSrvAccSettings());
      proc.setSrvPage(getSrvPage());
      proc.setMngUvdSettings(getMngUvdSettings());
      //assigning fully initialized object:
      this.processorsMap
        .put(PrcWebstorePage.class.getSimpleName(), proc);
    }
    return proc;
  }

  /**
   * <p>Lazy get PrcRefreshGoodsInList.</p>
   * @param pAddParam additional param
   * @return requested PrcRefreshGoodsInList
   * @throws Exception - an exception
   */
  protected final PrcRefreshGoodsInList<RS>
    lazyGetPrcRefreshGoodsInList(
      final Map<String, Object> pAddParam) throws Exception {
    @SuppressWarnings("unchecked")
    PrcRefreshGoodsInList<RS> proc = (PrcRefreshGoodsInList<RS>)
      this.processorsMap
        .get(PrcRefreshGoodsInList.class.getSimpleName());
    if (proc == null) {
      proc = new PrcRefreshGoodsInList<RS>();
      proc.setSrvOrm(getSrvOrm());
      proc.setSrvDatabase(getSrvDatabase());
      proc.setSrvTradingSettings(getSrvTradingSettings());
      proc.setSrvAccSettings(getSrvAccSettings());
      proc.setSrvSettingsAdd(getSrvSettingsAdd());
      //assigning fully initialized object:
      this.processorsMap
        .put(PrcRefreshGoodsInList.class.getSimpleName(), proc);
    }
    return proc;
  }

  /**
   * <p>Lazy get PrcAssignGoodsToCatalog.</p>
   * @param pAddParam additional param
   * @return requested PrcAssignGoodsToCatalog
   * @throws Exception - an exception
   */
  protected final PrcAssignGoodsToCatalog<RS>
    lazyGetPrcAssignGoodsToCatalog(
      final Map<String, Object> pAddParam) throws Exception {
    @SuppressWarnings("unchecked")
    PrcAssignGoodsToCatalog<RS> proc = (PrcAssignGoodsToCatalog<RS>)
      this.processorsMap
        .get(PrcAssignGoodsToCatalog.class.getSimpleName());
    if (proc == null) {
      proc = new PrcAssignGoodsToCatalog<RS>();
      proc.setSrvOrm(getSrvOrm());
      proc.setSrvEntitiesPage(getSrvEntitiesPage());
      proc.setSrvTradingSettings(getSrvTradingSettings());
      proc.setSrvAccSettings(getSrvAccSettings());
      //assigning fully initialized object:
      this.processorsMap
        .put(PrcAssignGoodsToCatalog.class.getSimpleName(), proc);
    }
    return proc;
  }

  /**
   * <p>Lazy get PrcTradeEntitiesPage.</p>
   * @param pAddParam additional param
   * @return requested PrcTradeEntitiesPage
   * @throws Exception - an exception
   */
  protected final PrcTradeEntitiesPage<RS>
    lazyGetPrcTradeEntitiesPage(
      final Map<String, Object> pAddParam) throws Exception {
    @SuppressWarnings("unchecked")
    PrcTradeEntitiesPage<RS> proc = (PrcTradeEntitiesPage<RS>)
      this.processorsMap
        .get(PrcTradeEntitiesPage.class.getSimpleName());
    if (proc == null) {
      proc = new PrcTradeEntitiesPage<RS>();
      proc.setSrvTradingSettings(getSrvTradingSettings());
      proc.setSrvAccSettings(getSrvAccSettings());
      PrcEntitiesPage procDlg = (PrcEntitiesPage) this.fctBnProcessors
        .lazyGet(pAddParam, PrcEntitiesPage.class.getSimpleName());
      proc.setPrcEntitiesPage(procDlg);
      //assigning fully initialized object:
      this.processorsMap
        .put(PrcTradeEntitiesPage.class.getSimpleName(), proc);
    }
    return proc;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
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
   * <p>Getter for srvOrm.</p>
   * @return ASrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for srvEntitiesPage.</p>
   * @return ISrvEntitiesPage
   **/
  public final ISrvEntitiesPage getSrvEntitiesPage() {
    return this.srvEntitiesPage;
  }

  /**
   * <p>Setter for srvEntitiesPage.</p>
   * @param pSrvEntitiesPage reference
   **/
  public final void setSrvEntitiesPage(
    final ISrvEntitiesPage pSrvEntitiesPage) {
    this.srvEntitiesPage = pSrvEntitiesPage;
  }

  /**
   * <p>Getter for fctBnProcessors.</p>
   * @return IFactoryAppBeansByName<IProcessor>
   **/
  public final FctBnProcessors<RS> getFctBnProcessors() {
    return this.fctBnProcessors;
  }

  /**
   * <p>Setter for fctBnProcessors.</p>
   * @param pFctBnProcessors reference
   **/
  public final void setFctBnProcessors(
    final FctBnProcessors<RS> pFctBnProcessors) {
    this.fctBnProcessors = pFctBnProcessors;
  }

  /**
   * <p>Getter for srvSettingsAdd.</p>
   * @return ISrvSettingsAdd
   **/
  public final ISrvSettingsAdd getSrvSettingsAdd() {
    return this.srvSettingsAdd;
  }

  /**
   * <p>Setter for srvSettingsAdd.</p>
   * @param pSrvSettingsAdd reference
   **/
  public final void setSrvSettingsAdd(final ISrvSettingsAdd pSrvSettingsAdd) {
    this.srvSettingsAdd = pSrvSettingsAdd;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
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

  /**
   * <p>Getter for srvTradingSettings.</p>
   * @return ISrvTradingSettings
   **/
  public final ISrvTradingSettings getSrvTradingSettings() {
    return this.srvTradingSettings;
  }

  /**
   * <p>Setter for srvTradingSettings.</p>
   * @param pSrvTradingSettings reference
   **/
  public final void setSrvTradingSettings(
    final ISrvTradingSettings pSrvTradingSettings) {
    this.srvTradingSettings = pSrvTradingSettings;
  }

  /**
   * <p>Getter for srvPage.</p>
   * @return ISrvPage
   **/
  public final ISrvPage getSrvPage() {
    return this.srvPage;
  }

  /**
   * <p>Setter for srvPage.</p>
   * @param pSrvPage reference
   **/
  public final void setSrvPage(final ISrvPage pSrvPage) {
    this.srvPage = pSrvPage;
  }

  /**
   * <p>Getter for mngUvdSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngUvdSettings() {
    return this.mngUvdSettings;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngUvdSettings reference
   **/
  public final void setMngUvdSettings(final IMngSettings pMngUvdSettings) {
    this.mngUvdSettings = pMngUvdSettings;
  }
}
