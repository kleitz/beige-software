<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${not empty entity[fieldName]}">
  <fmt:formatDate value="${entity[fieldName].itsDate}" type="both" timeStyle="medium" var="dateAppr"/>
  <c:if test="${empty entity[fieldName].idBirth}">
    <c:set var="docAppr" value="# ${entity[fieldName].idDatabaseBirth}-${entity[fieldName].itsId}, ${dateAppr}, ${entity[fieldName].itsTotal}"/>
  </c:if>
  <c:if test="${not empty entity[fieldName].idBirth}">
    <c:set var="docAppr" value="# ${entity[fieldName].idDatabaseBirth}-${entity[fieldName].idBirth}, ${dateAppr}, ${entity[fieldName].itsTotal}"/>
  </c:if>
</c:if>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${docAppr}">
      <input id="${entity.getClass().simpleName}${fieldName}Id"type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsId}">
    </div>
  </td>
</tr>
