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
  <script type="text/javascript" src="static/js/beige.ajax.js"></script>
  <script type="text/javascript" src="static/js/beige.form.js"></script>
  <script type="text/javascript" src="static/js/beige.i18n.en.js"></script>
  <link rel="stylesheet" href="static/css/beige.common.css" />
  <link rel="icon" type="image/png"  href="static/img/favicon.png">
  <title>Beige WEB</title>
</head>
<body>
  <div class="navbar">
    <a class="navbar-brand" href="secure/main.jsp">Main</a>
    <a class="navbar-brand" href="secure/mainMobile.jsp">Main mobile</a>
    <a class="navbar-brand" href="mngSoftware/?nameRenderer=mngSoftware">Software management</a>
    <a class="navbar-brand" href="mngDatabase/?nameRenderer=mngDatabase">Database management</a>
    <div class="nav-right">
      <a href="http://www.beigesoft.org/" target="_blank">Beigesoft ™</a>
    </div>
  </div>  

  <dialog id="dlgError" class="dlg">
      <div class="form-std">
        <div class="dialog-title">
          Error!
          <button onclick="document.getElementById('dlgError').close()" class="btn-close">x</button>
        </div>
        <div id="errorPlace">
        </div>
     </div>
  </dialog>

</body>
</html>
