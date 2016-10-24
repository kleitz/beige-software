<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
<c:set var="parVal" value="${fltOrdPrefix}${fieldName}Val"/>
<c:set var="parOpr" value="${fltOrdPrefix}${fieldName}Opr"/>
<div class="input-line">
  <c:if test="${filterMap[forcedFor].contains(fieldName)}">
    <b>${srvI18n.getMsg("forced")}</b>
    <label>${srvI18n.getMsg(fieldName)}</label>
    ${filterMap[parOpr]}
    <input type="hidden" name="${parOpr}" value="${filterMap[parOpr]}">
    ${filterMap[parVal]}
    <input type="hidden" name="${parVal}" value="${filterMap[parVal]}">
  </c:if>
  <c:if test="${!filterMap[forcedFor].contains(fieldName)}">
    <c:if test="${filterMap[parOpr] eq 'eq'}"> <c:set var="selectedEq" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr] eq 'like'}"> <c:set var="selectedLike" value="selected"/> </c:if>
    <label for="${parOpr}">${srvI18n.getMsg(fieldName)}</label>
    <c:if test="${empty selectedEq && empty selectedLike}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
    <select name="${parOpr}" onchange="filterStringChanged(this, '${parVal}');">
      <option value="disabled" ${selectedDisabled}>${srvI18n.getMsg("disabled")}</option>
      <option value="eq" ${selectedEq}>=</option>
      <option value="like" ${selectedLike}>Like</option>
    </select>
    <c:if test="${empty selectedEq && empty selectedLike}"> <c:set var="disabled" value="disabled"/> </c:if>
    <input id="${parVal}" name="${parVal}" ${disabled} type="text" value="${filterMap[parVal]}" onchange="inputHasBeenChanged(this);">
  </c:if>
</div>
