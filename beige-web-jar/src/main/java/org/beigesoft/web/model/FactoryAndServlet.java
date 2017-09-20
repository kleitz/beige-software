package org.beigesoft.web.model;

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

import javax.servlet.http.HttpServlet;

import org.beigesoft.factory.IFactoryAppBeans;

/**
 * <p>Container of app-factory and initialized servlet.</p>
 *
 * @author Yury Demidenko
 */
public class FactoryAndServlet {

  /**
   * <p>App-beans factory.</p>
   **/
  private final IFactoryAppBeans factoryAppBeans;

  /**
   * <p>Http Servlet.</p>
   **/
  private final HttpServlet httpServlet;

  /**
   * <p>Only constructor.</p>
   * @param pFactoryAppBeans reference
   * @param pHttpServlet reference
   **/
  public FactoryAndServlet(final IFactoryAppBeans pFactoryAppBeans,
    final HttpServlet pHttpServlet) {
    this.factoryAppBeans = pFactoryAppBeans;
    this.httpServlet = pHttpServlet;
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
   * <p>Getter for httpServlet.</p>
   * @return HttpServlet
   **/
  public final HttpServlet getHttpServlet() {
    return this.httpServlet;
  }
}
