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
  <title>${srvI18n.getMsg("RefreshGoodsInList")} <fmt:formatDate value="${now}" type="both" timeStyle="short"/></title>
</head>
<body>
  <div style="text-align: center;">
    <h4>
      ${srvI18n.getMsg("RefreshGoodsInList")} <fmt:formatDate value="${now}" type="both" timeStyle="short"/><br></br>
      ${accSettings.organization}
    </h4>
  </div>
  <p>
    <table>
      <tr>
        <td>
          ${srvI18n.getMsg('totalUpdatedGdSp')}:
        </td>
        <td>
          ${totalUpdatedGdSp}
        </td>
      </tr>
      <tr>
        <td>
          ${srvI18n.getMsg('totalUpdatedGdAv')}:
        </td>
        <td>
          ${totalUpdatedGdAv}
        </td>
      </tr>
      <tr>
        <td>
          ${srvI18n.getMsg('totalUpdatedGdPr')}:
        </td>
        <td>
            ${totalUpdatedGdPr}
        </td>
      </tr>
    </table>
  </p>
</body>
</html>
