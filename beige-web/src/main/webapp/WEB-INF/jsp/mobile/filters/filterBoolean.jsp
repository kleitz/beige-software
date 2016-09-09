<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="input-line">
  <c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
  <c:set var="parVal" value="${fltOrdPrefix}${fieldName}Val"/>
  <c:if test="${filterMap[forcedFor].contains(fieldName)}">
    <b>${srvI18n.getMsg("forced")}</b>
    ${srvI18n.getMsg(fieldName)}: ${srvI18n.getMsg(filterMap[parVal])}
    <input type="hidden" name="${parVal}" value="${filterMap[parVal]}">
  </c:if>
  <c:if test="${!filterMap[forcedFor].contains(fieldName)}">
    <label for="${parVal}">${srvI18n.getMsg(fieldName)}</label>
    <select name="${parVal}" onchange="inputHasBeenChanged(this);">
      <c:set var="selectedDisabled" value=""/>
      <c:if test="${empty filterMap[parVal]}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
      <option value="null" ${selectedDisabled}>${srvI18n.getMsg("disabled")}</option>
      <c:set var="selectedDisabled" value=""/>
      <c:if test="${filterMap[parVal].toString() eq 'true'}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
      <option value="true" ${selectedDisabled}>${srvI18n.getMsg("Yes")}</option>
      <c:set var="selectedDisabled" value=""/>
      <c:if test="${filterMap[parVal].toString() eq 'false'}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
      <option value="false" ${selectedDisabled}>${srvI18n.getMsg("No")}</option>
    </select>
  </c:if>
</div>
