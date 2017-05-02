<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.${fieldName}">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <c:set var="required" value=""/>
    <c:if test="${srvOrm.tablesMap[classEntity.simpleName].fieldsMap[fieldName].definition.contains('not null')}">
      <c:set var="required" value="required"/>
    </c:if>
    <c:set var="dtVal" value=""/>
    <c:if test="${entity[fieldName] != null}">
      <c:set var="dtVal" value="${srvDate.toIso8601DateTimeNoTz(entity[fieldName], null)}"/>
    </c:if>
    <input type="datetime-local" disabled value="${dtVal}" onchange="inputHasBeenChanged(this);"/> 
    <input type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${dtVal}"/> 
  </td>
</tr>
