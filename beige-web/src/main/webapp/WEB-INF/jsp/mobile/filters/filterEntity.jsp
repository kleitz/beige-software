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
    <c:set var="selectedDisabled" value=""/>
    <c:if test="${filterMap[parOpr] eq 'disabled'}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
    <c:set var="selectedEq" value=""/>
    <c:if test="${filterMap[parOpr] eq 'eq'}"> <c:set var="selectedEq" value="selected"/> </c:if>
    <c:set var="selectedEeq" value=""/>
    <c:if test="${filterMap[parOpr] eq 'ne'}"> <c:set var="selectedNe" value="selected"/> </c:if>
    <label>${srvI18n.getMsg(fieldName)}</label>
    <select id="${parOpr}" name="${parOpr}" onchange="filterOperChanged(this, '${parVal}');">
      <option value="disabled" ${selectedDisabled}>${srvI18n.getMsg("disabled")}</option>
      <option value="eq" ${selectedEq}>=</option>
      <option value="ne" ${selectedNe}>!=</option>
    </select>
    <input id="${parValAppearance}Visible" disabled="disabled" type="text" value="${filterMap[parValAppearance]}" onchange="inputHasBeenChanged(this);">
    <input id="${parValAppearance}" name="${parValAppearance}" type="hidden" value="${filterMap[parValAppearance]}">
    <input id="${parValId}" type="hidden" name="${parValId}" value="${filterMap[parValId]}">
    <button type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[param.nmEnt].fieldsMap[fieldName].foreignEntity}', '${fltOrdPrefix}', '${fieldName}Val', '&nmHnd=${param.nmHnd}&mobile=${param.mobile}');">...</button>
    <button type="button" class="btn" onclick="clearSelectedEntity('${fltOrdPrefix}${fieldName}Val');">X</button>
  </c:if>
</div>
