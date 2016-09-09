package org.beigesoft.web.listener;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
      logger.info(WebContextCleaner.class,
        "Try to shutdown HikariDataSource...");
      if (ds != null) {
        ds.close();
        logger.info(WebContextCleaner.class, "HikariDataSource is shutdown!");
      } else {
        logger.info(WebContextCleaner.class, "HikariDataSource was NULL!!!");
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
