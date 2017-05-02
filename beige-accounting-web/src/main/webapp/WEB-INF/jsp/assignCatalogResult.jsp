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
  <title>${srvI18n.getMsg("assignCatalogResult")} <fmt:formatDate value="${now}" type="both" timeStyle="short"/></title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${srvI18n.getMsg("assignCatalogResult")} <fmt:formatDate value="${now}" type="both" timeStyle="short"/><br></br>
      ${accSettings.organization}
    </h4>
  </div>
  <div style="padding: 5px;">
    <b>${srvI18n.getMsg('filterAppearance')}:</b>
    <ul>
      <c:forEach var="fltrAppStr" items="${filterAppearance}">
        <li>
          ${fltrAppStr}
        </li>
      </c:forEach>
    </ul>
  </div>
  <p>
    <table>
      <tr>
        <td>
          ${srvI18n.getMsg('totalFilteredItems')}:
        </td>
        <td>
            ${totalItems}
        </td>
      </tr>
      <tr>
        <td>
          ${srvI18n.getMsg('catalog')}:
        </td>
        <td>
            ${catalogOfGoods.itsName}
        </td>
      </tr>
      <tr>
        <td>
          ${srvI18n.getMsg('action')}:
        </td>
        <td>
            ${goodsCatalogAction}
        </td>
      </tr>
    </table>
  </p>
  ${srvI18n.getMsg("affected_items")}:
  <table>
    <tr>
      <th>${srvI18n.getMsg("itsId")}</th>
      <th>${srvI18n.getMsg("itsName")}</th>
    </tr>
    <c:forEach var="goods" items="${goodsList}">
      <tr>
        <td>
          <c:if test="${empty goods.idBirth}">
            ${goods.idDatabaseBirth}-${goods.itsId}
          </c:if>
          <c:if test="${not empty goods.idBirth}">
            ${goods.idDatabaseBirth}-${goods.idBirth}
          </c:if>
        </td>
        <td>
          ${goods.itsName}
        </td>
      </tr>
    </c:forEach>
  </table>
</body>
</html>
