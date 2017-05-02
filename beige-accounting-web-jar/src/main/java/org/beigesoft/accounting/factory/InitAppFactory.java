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

import java.io.File;

import org.beigesoft.log.ILogger;
import org.beigesoft.delegate.IDelegateExc;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.web.model.FactoryAndServlet;
import org.beigesoft.web.factory.AFactoryAppBeans;

/**
 * <p>
 * Initialize app-factory with servlet parameters.
 * </p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class InitAppFactory<RS> implements IDelegateExc<FactoryAndServlet> {

  /**
   * <p>Make something with a model.</p>
   * @throws Exception - an exception
   * @param pFactoryAndServlet with make
   **/
  @Override
  public final synchronized void makeWith(
    final FactoryAndServlet pFactoryAndServlet) throws Exception {
    @SuppressWarnings("unchecked")
    AFactoryAppBeans<RS> factoryAppBeans =
      (AFactoryAppBeans<RS>) pFactoryAndServlet.getFactoryAppBeans();
    FactoryBldAccServices<RS> factoryBldAccServices =
      new FactoryBldAccServices<RS>();
    factoryBldAccServices.setFactoryAppBeans(factoryAppBeans);
    factoryAppBeans.setFactoryBldServices(factoryBldAccServices);
    FactoryAccServices<RS> factoryAccServices = new FactoryAccServices<RS>();
    factoryAccServices.setFactoryAppBeans(factoryAppBeans);
    factoryAccServices.setFactoryBldAccServices(factoryBldAccServices);
    factoryBldAccServices.setFactoryAccServices(factoryAccServices);
    factoryAppBeans.setFactoryOverBeans(factoryAccServices);
    String isShowDebugMessagesStr = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("isShowDebugMessages");
    factoryAppBeans.setIsShowDebugMessages(Boolean
      .valueOf(isShowDebugMessagesStr));
    ILogger logger = (ILogger) factoryAppBeans.lazyGet("ILogger");
    String newDatabaseIdStr = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("newDatabaseId");
    factoryAppBeans.setNewDatabaseId(Integer.parseInt(newDatabaseIdStr));
    String ormSettingsDir = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("ormSettingsDir");
    factoryAppBeans.setOrmSettingsDir(ormSettingsDir);
    String ormSettingsBaseFile = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("ormSettingsBaseFile");
    factoryAppBeans.setOrmSettingsBaseFile(ormSettingsBaseFile);
    String uvdSettingsDir = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("uvdSettingsDir");
    factoryAppBeans.setUvdSettingsDir(uvdSettingsDir);
    String uvdSettingsBaseFile = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("uvdSettingsBaseFile");
    factoryAppBeans.setUvdSettingsBaseFile(uvdSettingsBaseFile);
    String uploadDirectory = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("uploadDirectory");
    if (uploadDirectory != null) {
      factoryAppBeans.setUploadDirectory(uploadDirectory);
    }
    factoryAppBeans.setWebAppPath(pFactoryAndServlet.getHttpServlet()
      .getServletContext().getRealPath(""));
    File uploadDir = new File(factoryAppBeans.getWebAppPath() + File.separator
      + factoryAppBeans.getUploadDirectory());
    if (!uploadDir.exists()
      && !uploadDir.mkdirs()) { // e.g. run on maven-tomcat
      logger.error(InitAppFactory.class, "Can't create UD " + uploadDirectory);
    }
    String jdbcUrl = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("databaseName");
    if (jdbcUrl != null && jdbcUrl.contains(ISrvOrm.WORD_CURRENT_DIR)) {
      String currDir = System.getProperty("user.dir");
      jdbcUrl = jdbcUrl.replace(ISrvOrm.WORD_CURRENT_DIR,
        currDir + File.separator);
    } else if (jdbcUrl != null
      && jdbcUrl.contains(ISrvOrm.WORD_CURRENT_PARENT_DIR)) {
      File fcd = new File(System.getProperty("user.dir"));
      jdbcUrl = jdbcUrl.replace(ISrvOrm.WORD_CURRENT_PARENT_DIR,
        fcd.getParent() + File.separator);
    }
    factoryAppBeans.setDatabaseName(jdbcUrl);
    String databaseUser = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("databaseUser");
    factoryAppBeans.setDatabaseUser(databaseUser);
    String databasePassword = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("databasePassword");
    factoryAppBeans.setDatabasePassword(databasePassword);
    pFactoryAndServlet.getHttpServlet().getServletContext()
      .setAttribute("srvI18n", factoryAppBeans.lazyGet("ISrvI18n"));
    //to create/initialize database if need:
    factoryAppBeans.lazyGet("ISrvOrm");
  }
}
