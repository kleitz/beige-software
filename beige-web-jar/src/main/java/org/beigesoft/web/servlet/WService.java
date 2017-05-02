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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.web.service.UtlJsp;
import org.beigesoft.web.model.HttpRequestData;
import org.beigesoft.handler.IHandlerRequest;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.delegate.IDelegateExc;
import org.beigesoft.web.model.FactoryAndServlet;

/**
 * <p>Generic servlet that delegate request to a
 * IHandlerRequest that can implements any business logic
 * e.g. report, form edit, entities list, etc.
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WService extends HttpServlet {

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
      this.factoryAppBeans = (IFactoryAppBeans) getServletContext()
        .getAttribute("IFactoryAppBeans");
      if (factoryAppBeans == null) {
        String factoryAppBeansClass = getInitParameter("factoryAppBeansClass");
        Object factoryObject = Class.forName(factoryAppBeansClass)
          .newInstance();
        factoryAppBeans = (IFactoryAppBeans) factoryObject;
        getServletContext().setAttribute("IFactoryAppBeans",
          factoryAppBeans);
        String initFactoryClass = getInitParameter("initFactoryClass");
        Object initFactoryObject = Class.forName(initFactoryClass)
          .newInstance();
        @SuppressWarnings("unchecked")
        IDelegateExc<FactoryAndServlet> initFactory =
          (IDelegateExc<FactoryAndServlet>) initFactoryObject;
        initFactory.makeWith(new FactoryAndServlet(factoryAppBeans, this));
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  public final void doGet(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    doWork(pReq, pResp);
  }

  @Override
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    doWork(pReq, pResp);
  }


  /**
   * <p>Generic request handler.</p>
   * @param pReq Http Servlet Request
   * @param pResp Http Servlet Response
   * @throws ServletException ServletException
   * @throws IOException IOException
   **/
  public final void doWork(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    try {
      String nameHandler = pReq.getParameter("nmHnd");
      IHandlerRequest srvHandleRequest = (IHandlerRequest) this
        .factoryAppBeans.lazyGet(nameHandler);
      HttpRequestData requestData = new HttpRequestData(pReq, pResp);
      srvHandleRequest.handle(requestData);
      ISrvI18n srvI18n = (ISrvI18n) this.factoryAppBeans.lazyGet("ISrvI18n");
      UtlJsp utlJsp = (UtlJsp) this.factoryAppBeans.lazyGet("UtlJsp");
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
