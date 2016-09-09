<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <link rel="shortcut icon" href="../static/img/favicon.png">
  <link rel="stylesheet" href="../static/css/beige.common.css">
  <title>${srvI18n.getMsg("mngDatabase")}</title>
</head>
<body style="padding: 20px;" >

  <a class="btn" href="../">${srvI18n.getMsg("home")}</a>

  <div style="text-align: center; margin: 20px;">
    <h1>${srvI18n.getMsg("curr_db")}: ${currDb}</h1>
      <table>
      <tr>
        <th style="padding: 5px;">${srvI18n.getMsg("database")}</th>
        <th style="padding: 5px;">${srvI18n.getMsg("actions")}</th>
      </tr>
      <c:forEach var="database" items="${databases}">
          <tr>
            <td style="padding: 5px;">
              ${database}.sqlite
            </td>
            <td style="padding: 5px;">
              <a class="btn" href="?nameRenderer=mngDatabase&nameAction=change&nameDatabase=${database}" onclick="return confirm('Change?');">${srvI18n.getMsg("change_db_curr")}</a>
            </td>
          </tr>
      </c:forEach>
    </table>
  </div>


  <div class="form-std form-33-33"  style="top: initial;">
    <div class="dialog-title">
      ${srvI18n.getMsg("create_db_curr")}
    </div>
    <form method="post">
      <input type="hidden" name="nameAction" value="create">
      <input type="hidden" name="nameRenderer" value="mngDatabase">
      <table class="tbl-fieldset">
        <tr>
          <td>
            <label>${srvI18n.getMsg("create_label")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input name="nameDatabase" value="dbmy">.sqlite
            </div>
          </td>
        </tr>
      </table>
      <div>
        <input type="submit" onclick="return confirm('Create?');"/>
      </div>
    </form>
  </div>

</body>
</html>

