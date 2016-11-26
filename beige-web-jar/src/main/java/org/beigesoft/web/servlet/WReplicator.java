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
import org.beigesoft.replicator.service.IReplicator;

/**
 * <p>
 * Servlet that replicate database
 * through WEB service (WSendEntities).
 * </p>
 *
 * @author Yury Demidenko
 */
@SuppressWarnings("serial")
public class WReplicator extends HttpServlet {

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
      // e.g. importFullDatabaseCopy, replicatorTaxMarket
      String replicatorName = (String) pReq.getParameter("replicatorName");
      IReplicator replicator = (IReplicator) factoryAppBeans
        .lazyGet(replicatorName); // expected HTML replicator
      Map<String, Object> params = new HashMap<String, Object>();
      @SuppressWarnings("unchecked")
      Map<String, String[]> parameterMap =
        (Map<String, String[]>) pReq.getParameterMap();
      for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
        params.put(entry.getKey(), entry.getValue()[0]);
      }
      String urlSource = (String) pReq.getParameter("urlSource");
      String userName = pReq.getParameter("userName");
      if (userName != null && userName.trim().length() > 0) {
        params.put("authMethod", "form");
        String urlBase = urlSource.substring(0, urlSource.indexOf("secure") - 1);
        params.put("urlBase", urlBase);
        params.put("authUrl", urlBase + "/secure/j_security_check");
        params.put("urlGetAuthCookies", urlBase + "/secure/main.jsp");
        params.put("authUserName", "j_username");
        params.put("authUserPass", "j_password");
      }
      PrintWriter writer = pResp.getWriter();
      params.put("htmlWriter", writer);
      writer.println("<!DOCTYPE html>");
      writer.println("<html>");
      writer.println("<head>");
      writer.println("<meta charset=\"UTF-8\"/>");
      writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\"/>");
      writer.println("<link rel=\"shortcut icon\" href=\"../static/img/favicon.png\"/>");
      writer.println("<link rel=\"stylesheet\" href=\"../static/css/beige.common.css\"/>");
      writer.println("<title>Replication data</title>");
      writer.println("</head>");
      writer.println("<body style=\"padding: 20px;\">");
      writer.println("<a href=\"../\" class=\"btn\">Home</a>");
      writer.println("<div style=\"text-align: center;\">");
      writer.println("<h3>Replication data from " + urlSource + "</h3>");
      writer.println("</div>");
      writer.println("<div>");
      replicator.replicate(params);
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
