<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <script type="text/javascript" src="../static/js/beige.ajax.js"></script>
  <script type="text/javascript" src="../static/js/beige.form.js"></script>
  <script type="text/javascript" src="../static/js/beige.i18n.en.js"></script>
  <link rel="stylesheet" href="../static/css/beige.common.css" />
  <link rel="icon" type="image/png"  href="../static/img/favicon.png">
  <title>Beige WEB</title>
</head>
<body>

  <div class="navbar">
    <a class="navbar-brand" href="main.jsp">Main</a>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">Entities</a>
      <div class="dropdown-content">
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=PersistableHead&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PersistableHeads")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=Department&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Departments")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodVersionTime&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodVersionTimes")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsRating&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsRatings")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=EmailConnect&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("EmailConnects")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=EmailMsg&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("EmailMsgs")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=UserTomcat&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("UserTomcats")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=UserRoleTomcat&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("UserRoleTomcats")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=UserRoleTomcatPriority&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("UserRoleTomcatPrioritys")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=UserJetty&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("UserJettys")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=RoleJetty&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("RoleJettys")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=UserRoleJetty&page=1&mobile=mobile/');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("UserRoleJettys")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmRnd=aboutJson&nmHnd=hndAbout');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("about")}</a>
      </div>
    </div>
    <div class="nav-right">
      <a href="http://www.beigesoft.org/" target="_blank">Beigesoft ™</a>
      <c:if test="${not empty pageContext['request'].userPrincipal}">
        <a href="../index.jsp?logoff=true">${pageContext['request'].userPrincipal} Logout</a>
      </c:if>
      <c:if test="${empty pageContext['request'].userPrincipal}">
        <a href="../index.jsp">Exit</a>
      </c:if>
    </div>
  </div>  

  <div id="lstMainPlace">
  </div>
  
  <div id="frmMainPlace">
  </div>

  <div id="frmSubPlace">
  </div>

  <div id="pickersPlace">
  </div>

  <div id="pickersPlaceDub">
  </div>

  <div id="targetInfo">
  </div>

  <dialog id="dlgError" class="dlg dlg-alert">
      <div class="error">
        <div class="dialog-title error-title">
          Error!
          <button onclick="document.getElementById('dlgError').close()" class="btn-close btn-error">x</button>
        </div>
        <div id="errorPlace" class="msg-place">
        </div>
     </div>
  </dialog>

  <dialog id="dlgWarning" class="dlg dlg-alert">
    <div class="warning">
      <div class="dialog-title warning-title">
        Warning!
        <button onclick="document.getElementById('dlgWarning').close()" class="btn-close btn-warning">x</button>
      </div>
      <div id="warningPlace" class="msg-place">
      </div>
   </div>
  </dialog>

  <div id="dlgSuccess" class="dlg-notifier">
    <div class="success">
      <div class="dialog-title success-title">
        Success!
        <button onclick="document.getElementById('dlgSuccess').close()" class="btn-close btn-success">x</button>
      </div>
      <div id="successPlace" class="msg-place">
      </div>
   </div>
  </div>

</body>
</html>
