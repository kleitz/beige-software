package org.beigesoft.replicator.service;

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
import java.util.Date;

import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that release AppFactory beans.</p>
 *
 * @author Yury Demidenko
 */
public class PrepareDbAfterGetCopy
  implements IPrepareDbAfterImport {

  /**
   * <p>Factory App-Beans.</p>
   **/
  private IFactoryAppBeans factoryAppBeans;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>It prepares database after import.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void prepareDbAfterImport(
    final Map<String, Object> pAddParam) throws Exception {
    this.factoryAppBeans.releaseBeans();
    pAddParam.put("statusPrepareAfterImport", new Date().toString() + ", "
      + PrepareDbAfterGetCopyPostgresql.class.getSimpleName()
        + "App-factory beans has released");
    this.logger.info(PrepareDbAfterGetCopyPostgresql.class,
      "App-factory beans has released");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for factoryAppBeans.</p>
   * @return IFactoryAppBeans
   **/
  public final IFactoryAppBeans getFactoryAppBeans() {
    return this.factoryAppBeans;
  }

  /**
   * <p>Setter for factoryAppBeans.</p>
   * @param pFactoryAppBeans reference
   **/
  public final void setFactoryAppBeans(
    final IFactoryAppBeans pFactoryAppBeans) {
    this.factoryAppBeans = pFactoryAppBeans;
  }

  /**
   * <p>Getter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }
}
