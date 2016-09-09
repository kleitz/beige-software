<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
<c:set var="parOpr" value="${fltOrdPrefix}${fieldName}Opr"/>
<c:set var="parValId" value="${fltOrdPrefix}${fieldName}ValId"/>
<c:set var="parValAppearance" value="${fltOrdPrefix}${fieldName}ValAppearance"/>
<div class="input-line">
  <c:if test="${filterMap[forcedFor].contains(fieldName)}">
    <b>${srvI18n.getMsg("forced")}</b>
    ${srvI18n.getMsg(fieldName)}
    ${srvI18n.getMsg(filterMap[parOpr])}
    ${filterMap[parValAppearance]}
    <input type="hidden" name="${parValAppearance}" value="${filterMap[parValAppearance]}">
    <input type="hidden" name="${parOpr}" value="${filterMap[parOpr]}">
    <input type="hidden" name="${parValId}" value="${filterMap[parValId]}">
  </c:if>
  <c:if test="${!filterMap[forcedFor].contains(fieldName)}">
    <label>${srvI18n.getMsg(fieldName)}=</label>
    <input id="${parValAppearance}Visible" disabled="disabled" type="text" value="${filterMap[parValAppearance]}" onchange="inputHasBeenChanged(this);">
    <input id="${parValAppearance}" type="hidden" value="${filterMap[parValAppearance]}">
    <input id="${parValId}" type="hidden" name="${parValId}" value="${filterMap[parValId]}">
    <input type="hidden" name="${parOpr}" value="eq">
    <button type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[param.nameEntity].fieldsMap[fieldName].foreignEntity}', '${fltOrdPrefix}', '${fieldName}Val', '&mobile=${param.mobile}');">...</button>
    <button type="button" class="btn" onclick="clearSelectedEntity('${fltOrdPrefix}${fieldName}Val');">X</button>
  </c:if>
</div>
