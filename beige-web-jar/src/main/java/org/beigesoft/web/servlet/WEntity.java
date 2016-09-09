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

import org.beigesoft.web.service.ISrvWebEntity;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Generic Entity servlet.
 * Get Entity form for actions create/copy/view/edit/confirmDelete.
 * Post Entity for action save/delete
 * Take parameters for Get:
 * <ul>
 *  <li>nameAction - to resolve which business method to invoke.</li>
 *  <li>nameRenderer - renderer name, e.g. FormMain</li>
 * </ul>
 * It's made according Beigesoft WEB interface specification version #1.
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WEntity extends HttpServlet {

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
      throw new ServletException(e);
    }
  }

  @Override
  public final void doGet(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    try {
      //Get generic transactional Entity service:
      ISrvWebEntity srvWebEntity = (ISrvWebEntity) this.factoryAppBeans
        .lazyGet("ISrvWebEntity");
      String nameAction = pReq.getParameter("nameAction");
      if ("create".equals(nameAction)) {
        srvWebEntity.create(pReq);
      } else if ("createTransactional".equals(nameAction)) {
        srvWebEntity.createTransactional(pReq);
      } else if ("view".equals(nameAction)) {
        srvWebEntity.view(pReq);
      } else if ("copy".equals(nameAction)) {
        srvWebEntity.copy(pReq);
      } else if ("print".equals(nameAction)) {
        srvWebEntity.print(pReq);
      } else if ("edit".equals(nameAction)) {
        srvWebEntity.edit(pReq);
      } else if ("confirmDelete".equals(nameAction)) {
        srvWebEntity.confirmDelete(pReq);
      } else if ("createFromOwnedList".equals(nameAction)) {
        srvWebEntity.createFromOwnedList(pReq);
      } else if ("createFromOwnedListTransactional".equals(nameAction)) {
        srvWebEntity.createFromOwnedListTransactional(pReq);
      } else if ("copyFromOwnedList".equals(nameAction)) {
        srvWebEntity.copyFromOwnedList(pReq);
      } else if ("editFromOwnedList".equals(nameAction)) {
        srvWebEntity.editFromOwnedList(pReq);
      } else if ("confirmDeleteFromOwnedList".equals(nameAction)) {
        srvWebEntity.confirmDeleteFromOwnedList(pReq);
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Method GET action " + nameAction + " not supported!");
      }
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

  @Override
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    try {
      //Get generic transactional Entity service:
      ISrvWebEntity srvWebEntity = (ISrvWebEntity) this.factoryAppBeans
        .lazyGet("ISrvWebEntity");
      String nameAction = pReq.getParameter("nameAction");
      if ("save".equals(nameAction)) {
        srvWebEntity.save(pReq);
      } else if ("delete".equals(nameAction)) {
        srvWebEntity.delete(pReq);
      } else if ("saveFromOwnedList".equals(nameAction)) {
        srvWebEntity.saveFromOwnedList(pReq);
      } else if ("deleteFromOwnedList".equals(nameAction)) {
        srvWebEntity.deleteFromOwnedList(pReq);
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Method POST action " + nameAction + " not supported!");
      }
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
