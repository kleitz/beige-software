package org.beigesoft.web.listener;

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

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import com.zaxxer.hikari.HikariDataSource;

import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.log.ILogger;

/**
 * <p>Invoke HicariCP.shutdown().
 * </p>
 *
 * @author Yury Demidenko
 */
public class WebContextCleaner implements ServletContextListener {

  @Override
  public final void contextDestroyed(final ServletContextEvent sce) {
    try {
      IFactoryAppBeans factoryAppBeans = (IFactoryAppBeans) sce
        .getServletContext().getAttribute("IFactoryAppBeans");
      HikariDataSource ds =
        (HikariDataSource) factoryAppBeans.lazyGet("DataSource");
      ILogger logger = (ILogger) factoryAppBeans.lazyGet("ILogger");
      logger.info(null, WebContextCleaner.class,
        "Try to shutdown HikariDataSource...");
      if (ds != null) {
        ds.close();
        logger.info(null, WebContextCleaner.class,
          "HikariDataSource is shutdown!");
      } else {
        logger.info(null, WebContextCleaner.class,
          "HikariDataSource was NULL!!!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public final void contextInitialized(final ServletContextEvent sce) {
    //nothing
  }
}
