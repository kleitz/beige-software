<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>Beige TEST</title>
</head>
<body>
  <p>contextAttrs:</p>
  <c:forEach var="entry" items="${contextAttrs}">
    ${entry.key} - ${entry.value} <br>
  </c:forEach>
</body>
</html>
