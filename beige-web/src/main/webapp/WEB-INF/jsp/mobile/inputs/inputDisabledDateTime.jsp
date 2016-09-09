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
      <input type="datetime-local" disabled value="${utlJsp.dateTimeToIso8601(entity[fieldName])}"/> 
    </div>
  </td>
</tr>
