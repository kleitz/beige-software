<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity[fieldName]}">
  <c:set var="docLineAppr" value="# ${entity[fieldName].itsId}, ${entity[fieldName].invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity[fieldName].itsCost}, ${srvI18n.getMsg('rest_was')}=${entity[fieldName].theRest}"/>
  <c:if test="${empty entity[fieldName].idBirth}">
    <c:set var="docLineAppr" value="# ${entity[fieldName].idDatabaseBirth}-${entity[fieldName].itsId}, ${entity[fieldName].invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity[fieldName].itsCost}, ${srvI18n.getMsg('theRest')}=${entity[fieldName].theRest}"/>
  </c:if>
  <c:if test="${not empty entity[fieldName].idBirth}">
    <c:set var="docLineAppr" value="# ${entity[fieldName].idDatabaseBirth}-${entity[fieldName].idBirth}, ${entity[fieldName].invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity[fieldName].itsCost}, ${srvI18n.getMsg('theRest')}=${entity[fieldName].theRest}"/>
  </c:if>
</c:if>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled type="text" value="${docLineAppr}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${fieldName}Id" type="hidden" required name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsId}">
      <button id="${entity.getClass().simpleName}${fieldName}Choose" type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].foreignEntity}', '${entity.getClass().simpleName}', '${fieldName}', '&nmHnd=${param.nmHnd}&fltordPitsOwnerValId=${entity.itsOwner.purchaseInvoice.itsId}&fltordPitsOwnerOpr=eq&fltordPforcedFor=itsOwner&mobile=${param.mobile}');">...</button>
      <button id="${entity.getClass().simpleName}${fieldName}Clear" type="button" class="btn" onclick="clearSubaccLine('${entity.getClass().simpleName}');">X</button>
    </div>
  </td>
</tr>
