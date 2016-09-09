<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>    
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>Beige WEB login</title>
  <link rel="stylesheet" href="../static/css/beige.common.css">
  <link rel="icon" type="image/png" href="../static/img/favicon.png">
</head>
<body>
  <div class="alert-20">${loginErrorJsp}</div>
  
  <div class="form-std form-33-33">
    <div class="dialog-title">
      Authorization
    </div>
    <form id="formLogin" action="<%= response.encodeURL("j_security_check") %>" method="post">
      <label>User</label>
      <input type="text" name="j_username"/>
      <label>Password</label>
      <input type="password" name="j_password"/>
      <div class="form-actions">
        <input type="submit" value="Login"/>
        <a href="../">Close</a>
      </div>
    </form>
  </div>
</body>
</html>

