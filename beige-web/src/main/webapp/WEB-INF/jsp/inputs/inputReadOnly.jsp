<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label>${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <input disabled value="${entity[fieldName]}"/> 
      <input type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName]}"/> 
    </div>
  </td>
</tr>
