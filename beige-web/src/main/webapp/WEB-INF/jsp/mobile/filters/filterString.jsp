<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
<c:set var="parVal1" value="${fltOrdPrefix}${fieldName}Val1"/>
<c:set var="parOpr1" value="${fltOrdPrefix}${fieldName}Opr1"/>
<c:if test="${filterMap[forcedFor].contains(fieldName)}">
  <b>${srvI18n.getMsg("forced")}</b>
  <label>${srvI18n.getMsg(fieldName)}</label>
  ${filterMap[parOpr1]}
  <input type="hidden" name="${parOpr1}" value="${filterMap[parOpr1]}">
  ${filterMap[parVal1]}
  <input type="hidden" name="${parVal1}" value="${filterMap[parVal1]}">
</c:if>
<c:if test="${!filterMap[forcedFor].contains(fieldName)}">
  <c:if test="${filterMap[parOpr1] eq 'eq'}"> <c:set var="selectedEq" value="selected"/> </c:if>
  <c:if test="${filterMap[parOpr1] eq 'like'}"> <c:set var="selectedLike" value="selected"/> </c:if>
  <label for="${parOpr1}">${srvI18n.getMsg(fieldName)}</label>
  <div class="input-line">
    <c:if test="${empty selectedEq && empty selectedLike}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
    <select name="${parOpr1}" onchange="filterStringChanged(this, '${parVal1}', '${parVal1}Rfl');">
      <option value="disabled" ${selectedDisabled}>${srvI18n.getMsg("disabled")}</option>
      <option value="eq" ${selectedEq}>=</option>
      <option value="like" ${selectedLike}>Like</option>
    </select>
    <c:if test="${empty selectedEq && empty selectedLike}"> <c:set var="disabled" value="disabled"/> </c:if>
    <c:if test="${not empty filterMap[parVal1]}"> <c:set var="valRemAps1" value="${utlJsp.removeApos(filterMap[parVal1])}"/> </c:if>
    <input id="${parVal1}" ${disabled} type="text" value="${valRemAps1}" onchange="inputHasBeenChanged(this); reflectString(this, '${parVal1}Rfl');">
    <input id="${parVal1}Rfl" ${disabled} type="hidden" name="${parVal1}" value="'${valRemAps1}'">
  </div>
</c:if>
