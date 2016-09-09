<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="parVal1" value="${fltOrdPrefix}${fieldName}Val1"/>
<c:set var="parOpr1" value="${fltOrdPrefix}${fieldName}Opr1"/>
<c:set var="gtCh" value="&gt;"/>
<c:if test="${filterMap[parOpr1] eq 'gt'}"> <c:set var="selectedGt1" value="selected"/> </c:if>
<c:if test="${filterMap[parOpr1] eq 'lt'}"> <c:set var="selectedLt1" value="selected"/> </c:if>
<c:if test="${filterMap[parOpr1] eq 'eq'}"> <c:set var="selectedEq" value="selected"/> </c:if>
<c:if test="${empty selectedEq1 && empty selectedGt1 && empty selectedLt1}"> <c:set var="selectedDisabled1" value="selected"/> </c:if>
<label for="${parOpr1}">${srvI18n.getMsg(fieldName)}</label>
<div class="input-line">
  <select name="${parOpr1}" onchange="filterOperChanged(this, '${parVal1}');">
    <option value="disabled" ${selectedDisabled1}>${srvI18n.getMsg("disabled")}</option>
    <option value="gt" ${selectedGt1}>&gt;</option>
    <option value="lt" ${selectedLt1}>&lt;</option>
    <option value="eq" ${selectedEq1}>=</option>
  </select>
  <c:if test="${empty selectedEq1 && empty selectedGt1 && empty selectedLt1}"> <c:set var="disabled" value="disabled"/> </c:if>
  <input id="${parVal1}" type="date" ${disabled} required name="${parVal1}" value="${utlJsp.fromMsToDateIso8601(filterMap[parVal1])}" onchange="inputHasBeenChanged(this);">
</div>

<c:set var="parVal2" value="${fltOrdPrefix}${fieldName}Val2"/>
<c:set var="parOpr2" value="${fltOrdPrefix}${fieldName}Opr2"/>
<c:set var="gtCh" value="&gt;"/>
<c:if test="${filterMap[parOpr2] eq 'gt'}"> <c:set var="selectedGt2" value="selected"/> </c:if>
<c:if test="${filterMap[parOpr2] eq 'lt'}"> <c:set var="selectedLt2" value="selected"/> </c:if>
<c:if test="${filterMap[parOpr2] eq '='}"> <c:set var="selectedEq" value="selected"/> </c:if>
<c:if test="${empty selectedEq2 && empty selectedGt2 && empty selectedLt2}"> <c:set var="selectedDisabled2" value="selected"/> </c:if>
<label for="${parOpr2}">${srvI18n.getMsg("and")}</label>
<div class="input-line">
  <select name="${parOpr2}" onchange="filterOperChanged(this, '${parVal2}');">
    <option value="disabled" ${selectedDisabled2}>${srvI18n.getMsg("disabled")}</option>
    <option value="gt" ${selectedGt2}>&gt;</option>
    <option value="lt" ${selectedLt2}>&lt;</option>
    <option value="eq" ${selectedEq2}>=</option>
  </select>
  <c:if test="${empty selectedEq2 && empty selectedGt2 && empty selectedLt2}"> <c:set var="disabled" value="disabled"/> </c:if>
  <input id="${parVal2}" type="date" ${disabled} required name="${parVal2}" value="${utlJsp.fromMsToDateIso8601(filterMap[parVal2])}" onchange="inputHasBeenChanged(this);">
</div>
