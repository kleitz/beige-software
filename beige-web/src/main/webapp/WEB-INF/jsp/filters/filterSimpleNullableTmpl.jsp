<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
<c:set var="parVal1" value="${fltOrdPrefix}${fieldName}Val1"/>
<c:set var="parOpr1" value="${fltOrdPrefix}${fieldName}Opr1"/>
<c:set var="parVal2" value="${fltOrdPrefix}${fieldName}Val2"/>
<c:set var="parOpr2" value="${fltOrdPrefix}${fieldName}Opr2"/>
<div class="input-line">
  <c:if test="${filterMap[forcedFor].contains(fieldName)}">
    <b>${srvI18n.getMsg("forced")}</b>
    <label>${srvI18n.getMsg(fieldName)}</label>
    ${srvI18n.getMsg(filterMap[parOpr1])}
    <input type="hidden" name="${parOpr1}" value="${filterMap[parOpr1]}">
    ${filterMap[parVal1]}
    <input type="hidden" name="${parVal1}" value="${filterMap[parVal1]}">
    <c:if test="${not empty filterMap[parOpr2]}">
      <label>${srvI18n.getMsg("and")}</label>
      ${srvI18n.getMsg(filterMap[parOpr2])}
      <input type="hidden" name="${parOpr2}" value="${filterMap[parOpr2]}">
      ${filterMap[parVal2]}
      <input type="hidden" name="${parVal2}" value="${filterMap[parVal2]}">
    </c:if>
  </c:if>
  <c:if test="${!filterMap[forcedFor].contains(fieldName)}">
    <c:if test="${filterMap[parOpr1] eq 'gt'}"> <c:set var="selectedGt1" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr1] eq 'lt'}"> <c:set var="selectedLt1" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr1] eq 'eq'}"> <c:set var="selectedEq1" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr1] eq 'isnull'}"> <c:set var="selectedIsNull" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr1] eq 'isnotnull'}"> <c:set var="selectedIsNotNull" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr1] eq 'disabled' or empty filterMap[parOpr1]}"> <c:set var="selectedDisabled1" value="selected"/> </c:if>
    <label for="${parOpr1}">${srvI18n.getMsg(fieldName)}</label>
    <select name="${parOpr1}" onchange="filterOperChanged(this, '${parVal1}');">
      <option value="disabled" ${selectedDisabled1}>${srvI18n.getMsg("disabled")}</option>
      <option value="gt" ${selectedGt1}>&gt;</option>
      <option value="lt" ${selectedLt1}>&lt;</option>
      <option value="eq" ${selectedEq1}>=</option>
      <option value="isnull" ${selectedIsNull}>${srvI18n.getMsg("isnull")}</option>
      <option value="isnotnull" ${selectedIsNotNull}>${srvI18n.getMsg("isnotnull")}</option>
    </select>
    <c:if test="${empty selectedEq1 && empty selectedGt1 && empty selectedLt1}"> <c:set var="disabled" value="disabled"/> </c:if>
    <input id="${parVal1}"  type="${inputType}" ${disabled} required name="${parVal1}" value="${filterMap[parVal1]}" onchange="inputHasBeenChanged(this);" ${inputAdd}>

    <c:if test="${filterMap[parOpr2] eq 'gt'}"> <c:set var="selectedGt2" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr2] eq 'lt'}"> <c:set var="selectedLt2" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr2] eq 'eq'}"> <c:set var="selectedEq2" value="selected"/> </c:if>
    <c:if test="${filterMap[parOpr2] eq 'disabled' or empty filterMap[parOpr2]}"> <c:set var="selectedDisabled2" value="selected"/> </c:if>
    <label for="${parOpr2}">${srvI18n.getMsg("and")}</label>
    <select name="${parOpr2}" onchange="filterOperChanged(this, '${parVal2}');">
      <option value="disabled" ${selectedDisabled2}>${srvI18n.getMsg("disabled")}</option>
      <option value="gt" ${selectedGt2}>&gt;</option>
      <option value="lt" ${selectedLt2}>&lt;</option>
      <option value="eq" ${selectedEq2}>=</option>
    </select>
    <c:if test="${empty selectedEq2 && empty selectedGt2 && empty selectedLt2}"> <c:set var="disabled" value="disabled"/> </c:if>
    <input id="${parVal2}" type="${inputType}" ${disabled} required name="${parVal2}" value="${filterMap[parVal2]}" onchange="inputHasBeenChanged(this);" ${inputAdd}>
  </c:if>
</div>
