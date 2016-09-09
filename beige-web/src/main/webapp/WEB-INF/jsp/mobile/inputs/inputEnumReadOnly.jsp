<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.${fieldName}">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <c:set var="valVisible" value=""/>
      <c:if test="${not empty entity[fieldName]}">
        <c:set var="valVisible" value="${srvI18n.getMsg(entity[fieldName])}"/>
      </c:if>
      <input type="text" disabled value="${valVisible}" onchange="inputHasBeenChanged(this);"/>
      <input type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName]}"/>
    </div>
  </td>
</tr>
