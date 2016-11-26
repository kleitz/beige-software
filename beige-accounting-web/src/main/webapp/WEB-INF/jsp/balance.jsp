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
  <title>${srvI18n.getMsg("trial_balance")} <fmt:formatDate value="${date2}"/></title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${srvI18n.getMsg("trial_balance")} <fmt:formatDate value="${date2}" type="date" dateStyle="LONG"/> <br>
      ${accSettings.organization}
    </h4>
  </div>
  <table>
    <tr>
      <th colspan="2" align="left">${srvI18n.getMsg("Account")}</th>
      <th class="debit-credit-th">${srvI18n.getMsg("Debit")}</th>
      <th class="debit-credit-th">${srvI18n.getMsg("Credit")}</th>
    </tr>
    <c:set var="accCurr" value=""/>
    <c:set var="debitTotal" value="0"/>
    <c:set var="creditTotal" value="0"/>
    <c:forEach var="balanceLine" items="${balanceLines}">
      <tr>
        <td>
          <c:if test="${balanceLine.accNumber != accCurr}">
            <c:set var="accCurr" value="${balanceLine.accNumber}"/>
            ${balanceLine.accNumber}
            ${balanceLine.accName}
          </c:if>
        </td>
        <td class="subacc">
          ${balanceLine.subaccName}</td>
        <td class="debit-credit">${balanceLine.debit}</td>
        <td class="debit-credit">${balanceLine.credit}</td>
      </tr>
      <c:set var="debitTotal" value="${balanceLine.debit + debitTotal}"/>
      <c:set var="creditTotal" value="${balanceLine.credit + creditTotal}"/>
    </c:forEach>
    <tr>
      <td colspan="2" class="total"><b>${srvI18n.getMsg("itsTotal")}:</b></td>
      <td class="total"><b>${debitTotal} ${accSettings.currency.itsName}</b></td>
      <td class="total"><b>${creditTotal} ${accSettings.currency.itsName}</b></td>
    </tr>
  </table>
</body>
</html>
