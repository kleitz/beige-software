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
      <c:set var="required" value=""/>
      <c:if test="${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].definition.contains('not null')}">
        <c:set var="required" value="required"/>
      </c:if>
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${entity[fieldName].itsUser}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${fieldName}Id" ${required} type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsUser}">
      <button type="button" ${autofocus} class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].foreignEntity}','${entity.getClass().simpleName}','${fieldName}', '&nmHnd=${param.nmHnd}&mobile=${param.mobile}');">...</button>
      <c:set var="autofocus" value="" scope="request"/>
      <button type="button" class="btn" onclick="clearSelectedEntity('${entity.getClass().simpleName}${fieldName}');">X</button>
    </div>
  </td>
</tr>
