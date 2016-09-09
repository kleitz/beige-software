<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.${fieldName}">${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <select id="${entity.getClass().simpleName}${fieldName}" name="${entity.getClass().simpleName}.${fieldName}" onchange="inputHasBeenChanged(this);">
        <c:if test="${empty entity[fieldName]}"> <c:set var="selectedNothing" value="selected"/> </c:if>
        <option value="null" ${selectedNothing}>-</option>
        <c:forEach var="entry" items="${typeCodeSubaccMap}">
          <c:if test="${entity[fieldName] == entry.key}"> <c:set var="selected" value="selected"/> </c:if>
          <c:if test="${entity[fieldName] != entry.key}"> <c:set var="selected" value=""/> </c:if>
          <option value="${entry.key}" ${selected}>${srvI18n.getMsg(entry.value.simpleName)}</option>
        </c:forEach>
      </select>
    </div>
  </td>
</tr>
