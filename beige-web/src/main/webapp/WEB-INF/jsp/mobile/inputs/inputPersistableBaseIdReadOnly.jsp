<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label>${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <c:if test="${empty entity.idBirth}">
        ${entity.idDatabaseBirth}-${entity.itsId}
      </c:if>
      <c:if test="${not empty entity.idBirth}">
        ${entity.idDatabaseBirth}-${entity.idBirth}
      </c:if>
      <input type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName]}"/> 
    </div>
  </td>
</tr>
