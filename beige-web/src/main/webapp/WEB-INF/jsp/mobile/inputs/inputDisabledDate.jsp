<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label>${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <c:set var="dtVal" value=""/>
    <c:if test="${entity[fieldName] != null}">
      <c:set var="dtVal" value="${srvDate.toIso8601DateNoTz(entity[fieldName], null)}"/>
    </c:if>
    <div class="input-line">
      <input type="date" disabled value="${dtVal}"/> 
    </div>
  </td>
</tr>
