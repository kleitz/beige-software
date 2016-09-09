<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    ${srvI18n.getMsg(fieldName)}: ${srvI18n.getMsg(entity[fieldName])}
  </td>
</tr>
