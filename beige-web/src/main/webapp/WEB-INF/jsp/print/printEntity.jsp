<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" media="print" href="../../static/css/beige.print-a4.css" />
  <link rel="icon" type="image/png" href="../../static/img/favicon.png">
  <link rel="stylesheet" type="text/css" href="../../static/css/beige.reports.css" />
  <title>${srvI18n.getMsg(title)} ${srvI18n.getMsg(entity.getClass().simpleName)}</title>
</head>
<body>

<div class="entity-title">${srvI18n.getMsg(title)} ${srvI18n.getMsg(entity.getClass().simpleName)}</div>
<div class="entity">
  <jsp:include page="printHead.jsp"/>
  <jsp:include page="${printOwnedList}.jsp"/>
</div>
</body>
</html>
