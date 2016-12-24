<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <link rel="shortcut icon" href="../static/img/favicon.png">
  <script type="text/javascript" src="../static/js/beige.form.js"></script>
  <script type="text/javascript" src="../static/js/beige.i18n.en.js"></script>
  <link rel="stylesheet" href="../static/css/beige.common.css">
  <title>${srvI18n.getMsg("mngSoftware")}</title>
</head>
<body style="padding: 20px;" >

  <a class="btn" href="../">${srvI18n.getMsg("home")}</a>

  <div style="text-align: center; margin: 20px;">
    <h1>${srvI18n.getMsg("mngSoftware")}</h1>
      <table>
      <tr>
        <th style="padding: 5px;">${srvI18n.getMsg("settings")}</th>
        <th style="padding: 5px;">${srvI18n.getMsg("actions")}</th>
      </tr>
      <tr>
        <td style="padding: 5px;">
          ${srvI18n.getMsg("isShowDebugMessages")}: ${mngSoftware.isShowDebugMessages}
        </td>
        <td style="padding: 5px;">
          <a class="btn" href="?nameRenderer=mngSoftware&isShowDebugMessages=change" onclick="confirmHref(this, 'Change?'); return false;">${srvI18n.getMsg("change")}</a>
        </td>
      </tr>
    </table>
  </div>

</body>
</html>

