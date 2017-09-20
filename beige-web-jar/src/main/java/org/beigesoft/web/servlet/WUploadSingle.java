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
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.RequestDispatcher;

import org.beigesoft.log.ILogger;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.web.service.UtlJsp;
import org.beigesoft.web.model.HttpRequestData;
import org.beigesoft.handler.IHandlerRequest;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>Generic servlet that upload single file in multipath way with name
 * from parameter named <b>paramNameFileToUpload</b>, then add pass attributes
 * "fileToUploadInputStream"-"[InputStream]" and
 * "fileToUploadName"-"[file name]" in "HttpRequestData" and delegate this
 * requestData to a IHandlerRequest that can implements any business logic
 * to handle this. fileToUploadInputStream should be closed by delegate.
 * "paramNameFileToUpload" is name of filePath property of entity
 * e.g. itsPath of entity Eattachment.
 * Delete action required parameter <b>nameFieldPath</b> to retrieve
 * path with reflection to delete uploaded file.
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WUploadSingle extends HttpServlet {

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
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    try {
      HttpRequestData requestData = new HttpRequestData(pReq, pResp);
      // e.g. Eattachment.itsPath
      String fileParam = pReq.getParameter("paramNameFileToUpload");
      if (fileParam != null) { // create
        Part filePart = pReq.getPart(fileParam);
        String fileName = getSubmittedFileName(filePart);
        InputStream fileToUploadIs = filePart.getInputStream();
        requestData.setAttribute("fileToUploadInputStream", fileToUploadIs);
        requestData.setAttribute("fileToUploadName", fileName);
      } // else - delete
      String nameHandlerUpload = pReq.getParameter("nmHnd");
      IHandlerRequest srvHandleRequest = (IHandlerRequest) this
        .factoryAppBeans.lazyGet(nameHandlerUpload);
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

  /**
   * <p>Retrieve file name from part.</p>
   * @param pPart file data
   * @return file name
   * @throws Exception - an exception
   **/
  public final String getSubmittedFileName(
    final Part pPart) throws Exception {
    for (String cd : pPart.getHeader("content-disposition").split(";")) {
      if (cd.trim().startsWith("filename")) {
        String fileName = cd.substring(cd.indexOf('=') + 1)
          .trim().replace("\"", "");
        return fileName.substring(fileName.lastIndexOf('/') + 1)
          .substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
      }
    }
    throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
      "Can't retrieve file name");
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
