<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  if (request.getParameter("logoff") != null) {
    request.getSession().invalidate();
    response.sendRedirect(request.getContextPath());
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <link rel="shortcut icon" href="static/img/favicon.png">
  <link rel="stylesheet" href="static/css/beige.common.css" />
  <title>Beige-Accounting</title>
</head>
<body>

  <div class="navbar">
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Appearance")}</a>
      <div class="dropdown-content">
        <a href="secure/main.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Standard")}</a>
        <a href="secure/mainMobile.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Mobile")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Admin")}</a>
      <div class="dropdown-content">
        <a href="secure/getDatabaseCopyForm.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("ImportDatabase")}</a>
        <a href="secure/webStoreAdmin.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("WEBStoreAdmin")}</a>
        <c:if test="${pageContext.servletContext.getInitParameter('webAppFor') eq 'AJetty'}">
          <a href="shutdown?token=stop" class="navbar-brand">Stop A-Jetty</a>
          <a href="mngDatabase/?nmRnd=mngDatabase" class="navbar-brand">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Databases")}</a>          
        </c:if>
        <c:if test="${pageContext.servletContext.getInitParameter('webAppFor') eq 'Android'}">
          <a href="mngDatabase/?nmRnd=mngDatabaseExt" class="btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Databases")}</a>
        </c:if>
        <a href="mngSoftware/?nmRnd=mngSoftware">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SoftwareManagement")}</a>
        <a href="secure/service?nmHnd=hndTrdTrnsReq&nmRnd=webstore&nmPrc=PrcWebstorePage" target="_blank">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Webstore")}</a>
      </div>
    </div>
    <div class="nav-right">
      <a href="http://www.beigesoft.org/" target="_blank">Beigesoft ™</a>
    </div>
  </div>  

</body>
</html>
