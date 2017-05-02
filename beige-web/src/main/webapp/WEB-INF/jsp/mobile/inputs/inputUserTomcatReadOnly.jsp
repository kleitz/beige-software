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
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${entity[fieldName].itsUser}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${fieldName}Id" ${required} type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsUser}">
    </div>
  </td>
</tr>
