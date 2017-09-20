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
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.log.ILogger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.replicator.service.IDatabaseWriter;

/**
 * <p>
 * Replicator's servlet that send required entities in XML/Json format.
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WSendEntities extends HttpServlet {

  /**
   * <p>Content type e.g. text/xml;
   * Settled through init params.</p>
   **/
  private String contentType;

  @Override
  public final void init() throws ServletException {
    this.contentType = getInitParameter("contentType");
  }

  @Override
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    pResp.setContentType(this.contentType + " charset=UTF-8");
    pResp.setStatus(HttpServletResponse.SC_OK);
    IFactoryAppBeans factoryAppBeans = null;
    try {
      factoryAppBeans = (IFactoryAppBeans) getServletContext()
          .getAttribute("IFactoryAppBeans");
      IDatabaseWriter databaseWriter = (IDatabaseWriter) factoryAppBeans
        .lazyGet("IDatabaseWriter");
      PrintWriter writer = pResp.getWriter();
      Class<?> entityClass = Class.forName(pReq.getParameter("entityName"));
      Map<String, Object> params = new HashMap<String, Object>();
      String requestingDatabaseVersion = (String) pReq
        .getParameter("requestingDatabaseVersion");
      params.put("requestingDatabaseVersion", requestingDatabaseVersion);
      String requestedDatabaseId = (String) pReq
        .getParameter("requestedDatabaseId");
      params.put("requestedDatabaseId", requestedDatabaseId);
      String conditions = (String) pReq.getParameter("conditions");
      params.put("conditions", conditions);
      databaseWriter.retrieveAndWriteEntities(params, entityClass, writer);
      writer.close();
    } catch (Exception e) {
      if (factoryAppBeans != null) {
        ILogger logger = null;
        try {
          logger = (ILogger) factoryAppBeans.lazyGet("ILogger");
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
   * <p>Getter for contentType.</p>
   * @return String
   **/
  public final String getContentType() {
    return this.contentType;
  }

  /**
   * <p>Setter for contentType.</p>
   * @param pContentType reference
   **/
  public final void setContentType(final String pContentType) {
    this.contentType = pContentType;
  }
}
