package org.beigesoft.replicator.service;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.util.Date;
import java.io.Writer;

import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.delegate.IDelegator;
import org.beigesoft.log.ILogger;

/**
 * <p>Service that release AppFactory beans.</p>
 *
 * @author Yury Demidenko
 */
public class PrepareDbAfterGetCopy
  implements IDelegator {

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
   * @param pAddParams additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void make(
    final Map<String, Object> pAddParams) throws Exception {
    this.factoryAppBeans.releaseBeans();
    Writer htmlWriter = (Writer) pAddParams.get("htmlWriter");
    if (htmlWriter != null) {
      htmlWriter.write("<h4>" + new Date().toString() + ", "
      + PrepareDbAfterGetCopy.class.getSimpleName()
        + ", app-factory beans has released" + "</h4>");
    }
    this.logger.info(null, PrepareDbAfterGetCopy.class,
      "app-factory beans has released");
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
