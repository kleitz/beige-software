package org.beigesoft.web.servlet;

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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.beigesoft.log.ILogger;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.service.ISrvDate;
import org.beigesoft.web.service.UtlJsp;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Generic Report servlet.
 * It just put common services into attributes and forward to given JSP
 * that is usually report form.
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WReport extends HttpServlet {

  /**
   * <p>App beans factort.</p>
   **/
  private IFactoryAppBeans factoryAppBeans;

  /**
   * <p>Folder for redirected JSP, e.g. "JSP WEB-INF/jsp/".
   * Settled through init params.</p>
   **/
  private String dirJsp;

  @Override
  public final void init() throws ServletException {
    this.dirJsp = getInitParameter("dirJsp");
    try {
      factoryAppBeans = (IFactoryAppBeans) getServletContext()
        .getAttribute("IFactoryAppBeans");
    } catch (Exception e) {
      if (this.factoryAppBeans != null) {
        ILogger logger = null;
        try {
          logger = (ILogger) this.factoryAppBeans.lazyGet("ILogger");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        if (logger != null) {
          logger.error(null, getClass(), "INIT", e);
        }
      }
      throw new ServletException(e);
    }
  }

  @Override
  public final void doGet(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    try {
      String nmRnd = pReq.getParameter("nmRnd");
      String path = dirJsp + nmRnd + ".jsp";
      RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
      ISrvI18n srvI18n = (ISrvI18n) this.factoryAppBeans.lazyGet("ISrvI18n");
      UtlJsp utlJsp = (UtlJsp) this.factoryAppBeans.lazyGet("UtlJsp");
      ISrvDate srvDate = (ISrvDate) this.factoryAppBeans.lazyGet("ISrvDate");
      pReq.setAttribute("srvDate", srvDate);
      pReq.setAttribute("srvI18n", srvI18n);
      pReq.setAttribute("utlJsp", utlJsp);
      rd.include(pReq, pResp);
    } catch (Exception e) {
      if (this.factoryAppBeans != null) {
        ILogger logger = null;
        try {
          logger = (ILogger) this.factoryAppBeans.lazyGet("ILogger");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        if (logger != null) {
          logger.error(null, getClass(), "WORK", e);
        } else {
          e.printStackTrace();
        }
      } else {
        e.printStackTrace();
      }
      if (e instanceof ExceptionWithCode) {
        pReq.setAttribute("error_code",
          ((ExceptionWithCode) e).getCode());
        pReq.setAttribute("short_message",
          ((ExceptionWithCode) e).getShortMessage());
      } else {
        pReq.setAttribute("error_code",
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      pReq.setAttribute("javax.servlet.error.status_code",
        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pReq.setAttribute("javax.servlet.error.exception", e);
      pReq.setAttribute("javax.servlet.error.request_uri",
        pReq.getRequestURI());
      pReq.setAttribute("javax.servlet.error.servlet_name", this.getClass()
        .getCanonicalName());
      pResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for factoryAppBeans.</p>
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
   * <p>Geter for dirJsp.</p>
   * @return String
   **/
  public final String getDirJsp() {
    return this.dirJsp;
  }

  /**
   * <p>Setter for dirJsp.</p>
   * @param pDirJsp reference
   **/
  public final void setDirJsp(final String pDirJsp) {
    this.dirJsp = pDirJsp;
  }
}
