package org.beigesoft.web.servlet;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.web.service.UtlJsp;
import org.beigesoft.web.service.IMngDatabase;

/**
 * <p>
 * Database manager for file-based RDBMS (SQLite/H2).
 * It can create and change current database (file).
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WMngDatabase extends HttpServlet {

  /**
   * <p>Folder for redirected JSP, e.g. "JSP WEB-INF/jsp/".
   * Settled through init params.</p>
   **/
  private String dirJsp;

  @Override
  public final void init() throws ServletException {
    this.dirJsp = getInitParameter("dirJsp");
  }

  @Override
  public final void doGet(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    System.out.println("Response encoding : " + pResp.getCharacterEncoding());
    try {
      IFactoryAppBeans factoryAppBeans  = (IFactoryAppBeans) getServletContext()
          .getAttribute("IFactoryAppBeans");
      IMngDatabase mngDatabase = (IMngDatabase) factoryAppBeans
        .lazyGet("IMngDatabase");
      String nameAction = pReq.getParameter("nameAction");
      String nameDatabase = pReq.getParameter("nameDatabase");
      if (nameDatabase != null && nameDatabase.length() > 2
        && "change".equals(nameAction)) {
        mngDatabase.changeDatabase(nameDatabase);
      }
      List<String> databases = mngDatabase.retrieveList();
      ISrvI18n srvI18n = (ISrvI18n) factoryAppBeans.lazyGet("ISrvI18n");
      UtlJsp utlJsp = (UtlJsp) factoryAppBeans.lazyGet("UtlJsp");
      pReq.setAttribute("databases", databases);
      pReq.setAttribute("currDb", mngDatabase.retrieveCurrentDbName());
      pReq.setAttribute("srvI18n", srvI18n);
      pReq.setAttribute("utlJsp", utlJsp);
      String nmRnd = pReq.getParameter("nmRnd");
      String path = dirJsp + nmRnd + ".jsp";
      RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
      rd.include(pReq, pResp);
    } catch (Exception e) {
      e.printStackTrace();
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


  @Override
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    System.out.println("Response encoding : " + pResp.getCharacterEncoding());
    try {
      IFactoryAppBeans factoryAppBeans  = (IFactoryAppBeans) getServletContext()
          .getAttribute("IFactoryAppBeans");
      IMngDatabase mngDatabase = (IMngDatabase) factoryAppBeans
        .lazyGet("IMngDatabase");
      String nameAction = pReq.getParameter("nameAction");
      String nameDatabase = pReq.getParameter("nameDatabase");
      if (nameDatabase != null && nameDatabase.length() > 2) {
        if ("create".equals(nameAction)) {
          String idDatabaseStr = pReq.getParameter("idDatabase");
          int idDatabase = Integer.parseInt(idDatabaseStr);
          mngDatabase.createDatabase(nameDatabase, idDatabase);
        } else if ("change".equals(nameAction)) {
          mngDatabase.changeDatabase(nameDatabase);
        }
      }
      List<String> databases = mngDatabase.retrieveList();
      ISrvI18n srvI18n = (ISrvI18n) factoryAppBeans.lazyGet("ISrvI18n");
      UtlJsp utlJsp = (UtlJsp) factoryAppBeans.lazyGet("UtlJsp");
      pReq.setAttribute("databases", databases);
      pReq.setAttribute("currDb", mngDatabase.retrieveCurrentDbName());
      pReq.setAttribute("srvI18n", srvI18n);
      pReq.setAttribute("utlJsp", utlJsp);
      String nmRnd = pReq.getParameter("nmRnd");
      String path = dirJsp + nmRnd + ".jsp";
      RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
      rd.include(pReq, pResp);
    } catch (Exception e) {
      e.printStackTrace();
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
