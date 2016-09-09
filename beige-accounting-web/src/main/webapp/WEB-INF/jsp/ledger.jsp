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
  <title>${srvI18n.getMsg("ledger")}</title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${srvI18n.getMsg("ledger")} ${srvI18n.getMsg("from")} <fmt:formatDate value="${date1}"/>
      ${srvI18n.getMsg("to")} <fmt:formatDate value="${date2}"/> <br>
      ${accSettings.organization} <br>
      ${srvI18n.getMsg("account")}: ${account.itsName}
      <c:if test="${not empty subaccName}">
        , <c:out value="${subaccName}"/>
      </c:if>
    </h4>
  </div>
  <table>
    <tr>
      <th>${srvI18n.getMsg("itsDate")}</th>
      <th>${srvI18n.getMsg("description")}</th>
      <th>${srvI18n.getMsg("coracc")}</th>
      <th class="debit-credit-th">${srvI18n.getMsg("Debit")}</th>
      <th class="debit-credit-th">${srvI18n.getMsg("Credit")}</th>
      <th class="debit-credit-th">${srvI18n.getMsg("Balance")}</th>
    </tr>
    <c:if test="${subaccName == null && account.subaccType != null}">
      <c:forEach var="entry" items="${ledgerPrevious.linesMap}">
        <tr>
          <td colspan="3" class="total">${entry.key}:</td>
          <td class="total">${entry.value.debit}</td>
          <td class="total">${entry.value.credit}</td>
          <td class="total">${entry.value.balance}</td>
        </tr>
      </c:forEach>
    </c:if>
    <tr>
      <td colspan="3" class="total"><b>${srvI18n.getMsg("Previous")}:</b></td>
      <td class="total"><b>${ledgerPrevious.debitAcc}</b></td>
      <td class="total"><b>${ledgerPrevious.creditAcc}</b></td>
      <td class="total"><b>${ledgerPrevious.balanceAcc}</b></td>
    </tr>
    <c:forEach var="ledgerLine" items="${ledgerDetail.itsLines}">
      <c:set var="subAccAppear" value=""/>
      <c:if test="${subaccName == null && account.subaccType != null}">
        <c:set var="subAccAppear" value="${ledgerLine.subaccName}, ${ledgerLine.balanceSubacc.add(ledgerPrevious.linesMap[ledgerLine.subaccName].balance)},"/>
      </c:if>
      <tr>
        <td><fmt:formatDate value="${ledgerLine.itsDate}" type="both" timeStyle="short"/></td>
        <td>${subAccAppear} ${ledgerLine.description}</td>
        <td>${ledgerLine.corrAccName} ${ledgerLine.corrSubaccName}</td>
        <td class="debit-credit">${ledgerLine.debit}</td>
        <td class="debit-credit">${ledgerLine.credit}</td>
        <td class="debit-credit">${ledgerLine.balance.add(ledgerPrevious.balanceAcc)}</td>
      </tr>
    </c:forEach>
    <c:if test="${subaccName == null && account.subaccType != null}">
      <c:forEach var="entry" items="${ledgerPrevious.linesMap}">
        <tr>
          <td colspan="3" class="total">${entry.key}:</td>
          <td class="total">${entry.value.debit.add(ledgerDetail.subaccDebitTotal[entry.key])}</td>
          <td class="total">${entry.value.credit.add(ledgerDetail.subaccCreditTotal[entry.key])}</td>
          <td class="total">${entry.value.balance.add(ledgerDetail.subaccBalanceTotal[entry.key])}</td>
        </tr>
      </c:forEach>
    </c:if>
    <tr>
      <td colspan="3" class="total"><b>${srvI18n.getMsg("itsTotal")}:</b></td>
      <td class="total"><b>${ledgerPrevious.debitAcc.add(ledgerDetail.debitAcc)} ${accSettings.currency.itsName}</b></td>
      <td class="total"><b>${ledgerPrevious.creditAcc.add(ledgerDetail.creditAcc)} ${accSettings.currency.itsName}</b></td>
      <td class="total"><b>${ledgerPrevious.balanceAcc.add(ledgerDetail.balanceAcc)} ${accSettings.currency.itsName}</b></td>
    </tr>
  </table>
</body>
</html>
