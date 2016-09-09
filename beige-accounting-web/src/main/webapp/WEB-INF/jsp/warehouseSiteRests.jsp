<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" media="print" href="../../static/css/beige.print-a4.css" />
  <link rel="stylesheet" type="text/css" href="../../static/css/beige.reports.css" />
  <link rel="icon" type="image/png" href="../../static/img/favicon.png">
  <title>${srvI18n.getMsg("warehouse_site_rests")} <fmt:formatDate value="${now}"/></title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${srvI18n.getMsg("warehouse_site_rests")} <fmt:formatDate value="${now}"/> <br>
      ${accSettings.organization}
    </h4>
  </div>
  <table>
    <tr>
      <th>${srvI18n.getMsg("warehouse")}</th>
      <th>${srvI18n.getMsg("warehouseSite")}</th>
      <th>${srvI18n.getMsg("invItem")}</th>
      <th>${srvI18n.getMsg("unitOfMeasure")}</th>
      <th>${srvI18n.getMsg("theRest")}</th>
    </tr>
    <c:set var="warehouseCurr" value=""/>
    <c:set var="warehouseSiteCurr" value=""/>
    <c:forEach var="warehouseSiteRestsLine" items="${warehouseSiteRestsLines}">
      <tr>
        <td>
          <c:if test="${warehouseSiteRestsLine.warehouse != warehouseCurr}">
            <c:set var="warehouseCurr" value="${warehouseSiteRestsLine.warehouse}"/>
            ${warehouseSiteRestsLine.warehouse}
          </c:if>
        </td>
        <td>
          <c:if test="${warehouseSiteRestsLine.warehouseSite != warehouseSiteCurr}">
            <c:set var="warehouseSiteCurr" value="${warehouseSiteRestsLine.warehouseSite}"/>
            ${warehouseSiteRestsLine.warehouseSite}
          </c:if>
        </td>
        <td>${warehouseSiteRestsLine.invItem}</td>
        <td>${warehouseSiteRestsLine.unitOfMeasure}</td>
        <td>${warehouseSiteRestsLine.theRest}</td>
      </tr>
    </c:forEach>
  </table>
</body>
</html>
