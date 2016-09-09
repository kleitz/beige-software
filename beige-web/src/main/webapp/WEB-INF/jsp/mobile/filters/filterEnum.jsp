<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
<c:set var="parVal" value="${fltOrdPrefix}${fieldName}Val"/>
<c:set var="parOpr" value="${fltOrdPrefix}${fieldName}Opr"/>
<c:set var="parValAppearance" value="${fltOrdPrefix}${fieldName}ValAppearance"/>
<div class="input-line">
  <c:if test="${filterMap[forcedFor].contains(fieldName)}">
    <b>${srvI18n.getMsg("forced")}</b>
    <label>${srvI18n.getMsg(fieldName)}</label>
    ${filterMap[parOpr]}
    ${filterMap[parValAppearance]}
    <input type="hidden" name="${parValAppearance}" value="${filterMap[parValAppearance]}">
    <input type="hidden" name="${parVal}" value="${filterMap[parVal]}">
    <input type="hidden" name="${parOpr}" value="${filterMap[parOpr]}">
  </c:if>
  <c:if test="${!filterMap[forcedFor].contains(fieldName)}">
    <input type="hidden" name="${parOpr}" value="eq">
    <label for="${parVal}">${srvI18n.getMsg(fieldName)}</label>
    <c:set var="selectedDisabled" value=""/>
    <c:if test="${empty filterMap[parVal]}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
    <select name="${parVal}" onchange="inputHasBeenChanged(this);">
      <option value="" ${selectedDisabled}>${srvI18n.getMsg("disabled")}</option>
      <c:forEach var="enm" items="${utlReflection.retrieveField(entity.getClass(), fieldName).type.enumConstants}">
        <c:if test="${!filterMap[parVal].toString().equals(enm)}"> <c:set var="selectedDisabled" value=""/> </c:if>
        <c:if test="${filterMap[parVal].toString().equals(enm)}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
        <option value="${enm.ordinal()}" ${selectedDisabled}>${srvI18n.getMsg(enm)}</option>
      </c:forEach>
    </select>
  </c:if>
</div>
