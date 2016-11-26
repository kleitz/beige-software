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
  <title>${srvI18n.getMsg("balance_sheet")} <fmt:formatDate value="${balanceSheet.itsDate}" type="date" dateStyle="LONG"/></title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${accSettings.organization} <br>
      ${srvI18n.getMsg("balance_sheet")} <br>
      <fmt:formatDate value="${balanceSheet.itsDate}" type="date" dateStyle="LONG"/>
     </h4>
  </div>
  <table>
    <tr>
      <td colspan="2"><b>${srvI18n.getMsg("AssetsTitle")}</b></th>
      <td colspan="2"><b>${srvI18n.getMsg("LiabilitiesTitle")}</b></th>
    </tr>
    <c:forEach var="currIdx" begin="1" end="${balanceSheet.detailRowsCount}">
      <tr>
        <c:if test="${currIdx le balanceSheet.totalLinesAssets}">
          <td>
            ${balanceSheet.itsLines.get(currIdx - 1).accName}
          </td>
          <td align="right">
            <c:if test="${balanceSheet.itsLines.get(currIdx - 1).debit.doubleValue() gt 0}">
              ${balanceSheet.itsLines.get(currIdx - 1).debit}
            </c:if>
            <c:if test="${balanceSheet.itsLines.get(currIdx - 1).credit.doubleValue() gt 0}">
              (${balanceSheet.itsLines.get(currIdx - 1).credit})
            </c:if>
          </td>
        </c:if>
        <c:if test="${currIdx gt balanceSheet.totalLinesAssets}">
          <td></td>
          <td></td>
        </c:if>

        <c:if test="${currIdx le balanceSheet.totalLinesLiabilities}">
          <td>
            ${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 1).accName}
          </td>
          <td align="right">
            <c:if test="${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 1).credit.doubleValue() gt 0}">
              ${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 1).credit}
            </c:if>
            <c:if test="${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 1).debit.doubleValue() gt 0}">
              (${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 1).debit})
            </c:if>
          </td>
        </c:if>
        <c:if test="${currIdx == balanceSheet.totalLinesLiabilities + 1}">
          <td align="center"><b>${srvI18n.getMsg("total_l")}</b></td>
          <td align="right"><b>${balanceSheet.totalLiabilities}</b></td>
        </c:if>
        <c:if test="${currIdx == balanceSheet.totalLinesLiabilities + 2}">
          <td colspan="2"><b>${srvI18n.getMsg("OwnersEquityTitle")}</b></td>
        </c:if>
        <c:if test="${(currIdx gt balanceSheet.totalLinesLiabilities + 2) && (currIdx lt balanceSheet.totalLinesLiabilities + balanceSheet.totalLinesOwnersEquity + 3)}">
          <td>
            ${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 3).accName}
          </td>
          <td align="right">
            <c:if test="${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 3).credit.doubleValue() gt 0}">
              ${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 3).credit}
            </c:if>
            <c:if test="${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 3).debit.doubleValue() gt 0}">
              (${balanceSheet.itsLines.get(balanceSheet.totalLinesAssets + currIdx - 3).debit})
            </c:if>
          </td>
        </c:if>
        <c:if test="${currIdx == balanceSheet.totalLinesLiabilities + balanceSheet.totalLinesOwnersEquity + 3}">
          <td align="center"><b>${srvI18n.getMsg("total_oe")}</b></td>
          <td align="right"><b>${balanceSheet.totalOwnersEquity}</b></td>
        </c:if>
        <c:if test="${currIdx gt balanceSheet.totalLinesLiabilities + balanceSheet.totalLinesOwnersEquity + 3}">
          <td></td>
          <td></td>
        </c:if>
      </tr>
    </c:forEach>
    <tr>
      <td align="center"><b>${srvI18n.getMsg("total_assets")}</b></td>
      <td align="right"><b>${balanceSheet.totalAssets} ${accSettings.currency.itsName}</b></td>
      <td align="center"><b>${srvI18n.getMsg("total_l_oe")}</b></td>
      <td align="right"><b>${balanceSheet.totalLiabilities.add(balanceSheet.totalOwnersEquity)} ${accSettings.currency.itsName}</b></td>
    </tr>
  </table>
</body>
</html>
