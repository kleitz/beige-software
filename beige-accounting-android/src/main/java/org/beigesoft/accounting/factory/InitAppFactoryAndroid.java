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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.delegate.IDelegateExc;
import org.beigesoft.web.model.FactoryAndServlet;

/**
 * <p>
 * Initialize app-factory with servlet parameters.
 * </p>
 *
 * @author Yury Demidenko
 */
public class InitAppFactoryAndroid implements IDelegateExc<FactoryAndServlet> {

  /**
   * <p>Make something with a model.</p>
   * @throws Exception - an exception
   * @param pFactoryAndServlet with make
   **/
  @Override
  public final synchronized void makeWith(
    final FactoryAndServlet pFactoryAndServlet) throws Exception {
    FactoryAppBeansAndroid factoryAppBeans =
      (FactoryAppBeansAndroid) pFactoryAndServlet.getFactoryAppBeans();
    String isShowDebugMessagesStr = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("isShowDebugMessages");
    factoryAppBeans.setIsShowDebugMessages(Boolean
      .valueOf(isShowDebugMessagesStr));
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
    String databaseName = pFactoryAndServlet.getHttpServlet()
      .getInitParameter("databaseName");
    factoryAppBeans.setDatabaseName(databaseName);
    FactoryAccServices factoryAccServices = new FactoryAccServices();
    factoryAccServices.setFactoryAppBeans(factoryAppBeans);
    factoryAppBeans.setFactoryOverBeans(factoryAccServices);
    android.content.Context aContext = (android.content.Context)
      pFactoryAndServlet.getHttpServlet().getServletContext()
        .getAttribute("android.content.Context");
    if (aContext == null) {
      throw new ExceptionWithCode(
        ExceptionWithCode.CONFIGURATION_MISTAKE,
          "Servlet context attribute android.content.Context is null!!!");
    }
    factoryAppBeans.setContext(aContext);
    pFactoryAndServlet.getHttpServlet().getServletContext()
      .setAttribute("srvI18n", factoryAppBeans.lazyGet("ISrvI18n"));
    //to create/initialize database if need:
    factoryAppBeans.lazyGet("ISrvOrm");
  }
}
