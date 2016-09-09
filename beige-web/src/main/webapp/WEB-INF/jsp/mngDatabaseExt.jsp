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
<body>
  <br>
  <a class="btn" href="../" style="margin: 10px;">${srvI18n.getMsg("home")}</a>

  <div style="text-align: center; margin-top: 10px; margin-bottom: 10px;">
    <h4>${srvI18n.getMsg("curr_db")}: ${currDb}</h4>
      <table>
      <tr>
        <th style="padding: 5px;">${srvI18n.getMsg("database")}</th>
        <th style="padding: 5px; width: 40%;">${srvI18n.getMsg("actions")}</th>
      </tr>
      <c:forEach var="database" items="${databases}">
        <tr>
          <td style="padding: 5px;">
            ${database}.sqlite
          </td>
          <td style="padding: 15px;">
            <a class="btn" href="?nameRenderer=mngDatabaseExt&nameAction=change&nameDatabase=${database}" onclick="return confirm('Change?');">${srvI18n.getMsg("change_db_curr")}</a>
            <br><br>
            <a class="btn" href="?nameRenderer=mngDatabaseExt&nameAction=backup&nameDatabase=${database}" onclick="return confirm('Backup?');">${srvI18n.getMsg("backup")}</a>
            <br><br>
            <a class="btn" href="?nameRenderer=mngDatabaseExt&nameAction=delete&nameDatabase=${database}" onclick="return confirm('Delete?');">${srvI18n.getMsg("Delete")}</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>

  <div style="text-align: center; margin-bottom: 15px;">
    <h4>${srvI18n.getMsg("backuped_db")} in ${backupDir}:</h4>
      <table>
      <tr>
        <th style="padding: 5px;">${srvI18n.getMsg("database")}</th>
        <th style="padding: 5px;">${srvI18n.getMsg("actions")}</th>
      </tr>
      <c:forEach var="database" items="${bkDatabases}">
        <tr>
          <td style="padding: 5px;">
            ${database}.sqlite
          </td>
          <td style="padding: 15px;">
            <a class="btn" href="?nameRenderer=mngDatabaseExt&nameAction=restore&nameDatabase=${database}" onclick="return confirm('Restore?');">${srvI18n.getMsg("restore")}</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </div>

  <div class="form-std form-33-33"  style="top: initial; width: initial;">
    <div class="dialog-title">
      ${srvI18n.getMsg("create_db_curr")}
    </div>
    <form method="post">
      <input type="hidden" name="nameAction" value="create">
      <input type="hidden" name="nameRenderer" value="mngDatabaseExt">
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

