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
  <link rel="stylesheet" href="static/css/beige.common.css" />
  <link rel="icon" type="image/png"  href="static/img/favicon.png">
  <title>Beige WEB</title>
</head>
<body>
  <div class="navbar">
    <a class="navbar-brand" href="secure/main.jsp">Main</a>
    <a class="navbar-brand" href="secure/mainMobile.jsp">Main mobile</a>
    <a class="navbar-brand" href="mngSoftware/?nmRnd=mngSoftware">Software management</a>
    <a class="navbar-brand" href="mngDatabase/?nmRnd=mngDatabase">Database management</a>
    <div class="nav-right">
      <a href="http://www.beigesoft.org/" target="_blank">Beigesoft ™</a>
    </div>
  </div>  
</body>
</html>
