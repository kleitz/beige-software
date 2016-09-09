<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" media="print" href="../../static/css/beige.print-a4.css" />
  <link rel="stylesheet" type="text/css" href="../../static/css/beige.reports.css" />
  <link rel="icon" type="image/png" href="../../static/img/favicon.png">
  <title>${srvI18n.getMsg("chartOfAccounts")}</title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${srvI18n.getMsg("chartOfAccounts")} <br></br>
      ${accSettings.organization}
    </h4>
  </div>
  <table>
    <tr>
      <th>${srvI18n.getMsg("itsId")}</th>
      <th>${srvI18n.getMsg("itsNumber")}</th>
      <th>${srvI18n.getMsg("itsName")}</th>
      <th>${srvI18n.getMsg("subaccount")}</th>
      <th>${srvI18n.getMsg("normalBalanceType")}</th>
      <th>${srvI18n.getMsg("itsType")}</th>
      <th>${srvI18n.getMsg("description")}</th>
    </tr>
    <c:forEach var="account" items="${accounts}">
      <tr>
        <td>
          ${account.itsId}
        </td>
        <td>
          ${account.itsNumber}
        </td>
        <td>
          ${account.itsName}
        </td>
        <td>
          ${account.subacc}
        </td>
        <td>
          <c:if test="${not empty account.normalBalanceType}">
            ${srvI18n.getMsg(account.normalBalanceType)}
          </c:if>
        </td>
        <td>
          <c:if test="${not empty account.itsType}">
            ${srvI18n.getMsg(account.itsType)}
          </c:if>
        </td>
        <td>
          ${account.description}
        </td>
      </tr>
    </c:forEach>
  </table>
</body>
</html>
