<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
    <c:set var="idColumnsString" value="${srvOrm.tablesMap[entity.getClass().simpleName].idColumnsString}"/>
    <c:if test="${idColumnsString.contains(fieldName)}">
      <input id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${entity[fieldName].itsName}" onchange="inputHasBeenChanged(this);">
      <input type="hidden" id="${entity.getClass().simpleName}${fieldName}Id" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsId}">
    </c:if>
    <c:if test="${!idColumnsString.contains(fieldName)}">
      <input disabled value="${entity[fieldName].itsName}"/> 
    </c:if>
    </div>
  </td>
</tr>
