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
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.replicator.service.IClearDbThenGetAnotherCopy;
import org.beigesoft.replicator.service.IPrepareDbAfterImport;

/**
 * <p>
 * Servlet that take get database copy request then invoke service
 * clearDbThenGetAnotherCopyHttp that clear database then get identical
 * copy of another one
 * (exclude tables with authentication User/Role/Tomcat/Jetty)
 * through WEB service (WSendDatabaseCopy).
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WGetDatabaseCopyResult extends HttpServlet {

  @Override
  public final void doPost(final HttpServletRequest pReq,
    final HttpServletResponse pResp) throws ServletException, IOException {
    pReq.setCharacterEncoding("UTF-8");
    pResp.setCharacterEncoding("UTF-8");
    pResp.setContentType("text/html; charset=UTF-8");
    pResp.setStatus(HttpServletResponse.SC_OK);
    try {
      IFactoryAppBeans factoryAppBeans  = (IFactoryAppBeans) getServletContext()
          .getAttribute("IFactoryAppBeans");
      IClearDbThenGetAnotherCopy clearDbThenGetAnotherCopyHttp = (IClearDbThenGetAnotherCopy) factoryAppBeans
        .lazyGet("clearDbThenGetAnotherCopyHttp");
      IPrepareDbAfterImport prepareDbAfterGetAnotherCopy = (IPrepareDbAfterImport) factoryAppBeans
          .lazyGet("prepareDbAfterGetAnotherCopy");
      Map<String, Object> params = new HashMap<String, Object>();
      String urlSource = (String) pReq.getParameter("urlSource");
      params.put("maxRecords", pReq.getParameter("maxRecords"));
      params.put("urlSource", urlSource);
      String userName = pReq.getParameter("userName");
      if (userName != null && userName.trim().length() > 0) {
        params.put("userName", userName);
        params.put("userPass", pReq.getParameter("userPass"));
        params.put("authMethod", "form");
        String urlBase = urlSource.substring(0, urlSource.indexOf("secure") - 1);
        params.put("urlBase", urlBase);
        params.put("authUrl", urlBase + "/secure/j_security_check");
        params.put("urlGetAuthCookies", urlBase + "/secure/main.jsp");
        params.put("authUserName", "j_username");
        params.put("authUserPass", "j_password");
      }
      PrintWriter writer = pResp.getWriter();
      writer.println("<!DOCTYPE html>");
      writer.println("<html>");
      writer.println("<head>");
      writer.println("<meta charset=\"UTF-8\"/>");
      writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\"/>");
      writer.println("<link rel=\"shortcut icon\" href=\"../static/img/favicon.png\"/>");
      writer.println("<link rel=\"stylesheet\" href=\"../static/css/beige.common.css\"/>");
      writer.println("<title>Clear current database then get identical copy of another one</title>");
      writer.println("</head>");
      writer.println("<body style=\"padding: 20px;\">");
      writer.println("<a href=\"../\" class=\"btn\">Home</a>");
      writer.println("<div style=\"text-align: center;\">");
      writer.println("<h3>Clear current database then get identical copy of another one from "
        + urlSource + "</h3>");
      writer.println("</div>");
      writer.println("<div>");
      clearDbThenGetAnotherCopyHttp.clearDbThenGetAnotherCopy(params);
      prepareDbAfterGetAnotherCopy.prepareDbAfterImport(params); //must exists, and does at least release beans
      //factoryAppBeans.releaseBeans();
      String statusString = (String) params.get("statusString");
      if (statusString != null) {
        writer.println("<h4>" + statusString + "</h4>");
      }
      String statusPrepareAfterImport = (String) params.get("statusPrepareAfterImport");
      if (statusPrepareAfterImport != null) {
        writer.println("<h4>" + statusPrepareAfterImport + "</h4>");
      }
      @SuppressWarnings("unchecked")
      Map<String, Integer> classesCounts = (Map<String, Integer>) params.get("classesCounts");
      if (classesCounts != null) {
        writer.println("<table>");
        writer.println("<tr><th style=\"padding: 5px;\">Class</th><th style=\"padding: 5px;\">Total records</th></tr>");
        for (Map.Entry<String, Integer> entry : classesCounts.entrySet()) {
          writer.println("<tr>");
          writer.println("<td>" + entry.getKey() + "</td>");
          writer.println("<td>" + entry.getValue() + "</td>");
          writer.println("</tr>");
        }
        writer.println("</table>");
      }
      writer.println("</div>");
      writer.println("</body>");
      writer.println("</html>");
      writer.close();
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
}
