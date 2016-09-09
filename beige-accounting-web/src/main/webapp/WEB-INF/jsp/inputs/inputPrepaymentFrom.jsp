<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${not empty entity[fieldName]}">
  <fmt:formatDate value="${entity[fieldName].itsDate}" type="both" timeStyle="medium" var="dateAppr"/>
  <c:set var="docAppr" value="# ${entity[fieldName].itsId}, ${dateAppr}, ${entity[fieldName].itsTotal}"/>
</c:if>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${docAppr}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${fieldName}Id" type="hidden" name="${entity.getClass().simpleName}.${fieldName}.itsId" value="${entity[fieldName].itsId}">
      <button type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].foreignEntity}','${entity.getClass().simpleName}', '${fieldName}', '&fltordPhasMadeAccEntriesVal=true&fltordPsalesInvoiceIdOpr1=isnull&fltordPforcedFor=hasMadeAccEntries%2CsalesInvoiceId&mobile=${param.mobile}');">...</button>
      <button type="button" class="btn" onclick="clearSelectedEntity('${entity.getClass().simpleName}${fieldName}');">X</button>
    </div>
  </td>
</tr>
