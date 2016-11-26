<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>Clear current database then get identical copy of another one</title>
  <link rel="stylesheet" href="../static/css/beige.common.css">
  <link rel="icon" type="image/png" href="../static/img/favicon.png">
</head>
<body style="padding: 20px;">
  <a class="btn" href="../">${srvI18n.getMsg("home")}</a>
  <div class="form-std form-70-33"  style="top: initial;">
    <div class="dialog-title">
      ${srvI18n.getMsg("get_db_copy")}
    </div>
    <form method="post" action="replicator">
      <table class="tbl-fieldset">
        <tr>
          <td>
            <label>${srvI18n.getMsg("user_name")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input name="userName" value="">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("user_pass")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input type="password" name="userPass" value="">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("url_source")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input name="urlSource" value="http://localhost:8080/beige-accounting-web/secure/sendEntities">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("max_records_per_transaction")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input type="number" name="maxRecords" value="100">
              <input type="hidden" name="replicatorName" value="importFullDatabaseCopy">
            </div>
          </td>
        </tr>
      </table>
      <div>
        <input type="submit" onclick="return confirm('${srvI18n.getMsg('clear_db_and_get_copy')}?');"/>
      </div>
    </form>
  </div>
</body>
</html>
