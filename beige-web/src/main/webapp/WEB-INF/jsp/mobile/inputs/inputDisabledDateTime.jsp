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
      <c:set var="dtVal" value=""/>
      <c:if test="${entity[fieldName] != null}">
        <c:set var="dtVal" value="${srvDate.toIso8601DateTimeNoTz(entity[fieldName], null)}"/>
      </c:if>
      <input type="datetime-local" disabled value="${dtVal}"/> 
    </div>
  </td>
</tr>
