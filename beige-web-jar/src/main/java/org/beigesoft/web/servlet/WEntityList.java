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

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.web.service.ISrvWebEntity;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.delegate.IDelegateExc;
import org.beigesoft.web.model.FactoryAndServlet;

/**
 * <p>Generic Entity servlet.
 * Get Entity page. This is startup serlet that initialize application.
 * Take parameters for Get:
 * <ul>
 *  <li>nameRenderer - renderer name, e.g. ListMain</li>
 * </ul>
 * It's made according Beigesoft WEB interface specification version #1.
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WEntityList extends HttpServlet {

  /**
   * <p>App-beans factory.</p>
   **/
  private IFactoryAppBeans factoryAppBeans;

  /**
   * <p>Folder for redirected JSP, e.g. "WEB-INF/jsp/".
   * Settled through init params.</p>
   **/
  private String dirJsp;

  @Override
  public final void init() throws ServletException {
    this.dirJsp = getInitParameter("dirJsp");
    try {
      factoryAppBeans = (IFactoryAppBeans) getServletContext()
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
    makeResponse(pReq, pResp);
  }

  @Override
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    makeResponse(pReq, pResp);
  }

  /**
   * <p>Perform same very simple, businessless logic for both
   * GET and POST requests.</p>
   * @param pReq - HttpServletRequest
   * @param pResp - HttpServletResponse
   * @throws ServletException an ServletException
   * @throws IOException an IOException
   **/
  public final void makeResponse(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    try {
      //Get generic transactional Entity service:
      ISrvWebEntity srvWebEntity = (ISrvWebEntity) this.factoryAppBeans
        .lazyGet("ISrvWebEntity");
      srvWebEntity.retrievePage(pReq);
      String nameRenderer = pReq.getParameter("nameRenderer");
      String path = dirJsp + nameRenderer + ".jsp";
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
