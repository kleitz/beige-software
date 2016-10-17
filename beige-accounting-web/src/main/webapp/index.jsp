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
  <script type="text/javascript" src="static/js/beige.ajax.js"></script>
  <script type="text/javascript" src="static/js/beige.form.js"></script>
  <script type="text/javascript" src="static/js/beige.accounting.js"></script>
  <script type="text/javascript" src="static/js/beige.i18n.en.js"></script>
  <link rel="stylesheet" href="static/css/beige.common.css" />
  <title>BeigeAccounting</title>
</head>
<body>

  <div class="navbar">
    <a class="navbar-brand" href="secure/main.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Standard")}</a>
    <a class="navbar-brand" href="secure/mainMobile.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Mobile")}</a>
    <a class="navbar-brand" href="mngSoftware/?nameRenderer=mngSoftware">Software management</a>
    <a class="navbar-brand" href="secure/getDatabaseCopyForm.jsp">${pageContext.servletContext.getAttribute("srvI18n").getMsg("clear_db_and_get_copy")}</a>
    <div class="nav-right">
      <a href="http://www.beigesoft.org/" target="_blank">Beigesoft ™</a>
    </div>
  </div>  

</body>
</html>
