<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
</head>
<body>

  <h4>List products:</h4>
  <table>
    <tr>
      <th>Name:</th>
    </tr>
    <c:forEach var="good" items="${goods}">
      <tr>
        <td>
          ${good.itsName}
        </td>
      </tr>
    </c:forEach>
  </table>
</body>
</html>
