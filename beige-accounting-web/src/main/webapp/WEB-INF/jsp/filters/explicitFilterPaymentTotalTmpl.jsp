<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="forcedFor" value="${fltOrdPrefix}forcedFor"/>
<c:set var="parVal" value="${fltOrdPrefix}${fieldName}Val"/>
<c:set var="parValue" value="${filterMap[parVal]}"/>
<c:set var="selectedDisabled1" value="selected"/>
<c:set var="fltrAppearGteq30" value="<= 30%"/>
<c:set var="valGteq30" value="PAYMENTTOTAL/${docName}.ITSTOTAL gte 0.30"/>
<c:if test="${parValue eq valGteq30}">
  <c:set var="selectedGteq30" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearGteq30}"/>
</c:if>
<c:set var="fltrAppearGteq50" value="<= 50%"/>
<c:set var="valGteq50" value="PAYMENTTOTAL/${docName}.ITSTOTAL gte 0.50"/>
<c:if test="${parValue eq valGteq50}">
  <c:set var="selectedGteq50" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearGteq50}"/>
</c:if>
<c:set var="fltrAppearGteq100" value="<= 100%"/>
<c:set var="valGteq100" value="PAYMENTTOTAL/${docName}.ITSTOTAL gte 1"/>
<c:if test="${parValue eq valGteq100}">
  <c:set var="selectedGteq100" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearGteq100}"/>
</c:if>
<c:set var="fltrAppearLt30" value="< 30%"/>
<c:set var="valLt30" value="PAYMENTTOTAL/${docName}.ITSTOTAL lt 0.30"/>
<c:if test="${parValue eq valLt30}">
  <c:set var="selectedLt30" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearLt30}"/>
</c:if>
<c:set var="fltrAppearLt50" value="< 50%"/>
<c:set var="valLt50" value="PAYMENTTOTAL/${docName}.ITSTOTAL lt 0.50"/>
<c:if test="${parValue eq valLt50}">
  <c:set var="selectedLt50" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearLt50}"/>
</c:if>
<c:set var="fltrAppearLt100" value="< 100%"/>
<c:set var="valLt100" value="PAYMENTTOTAL/${docName}.ITSTOTAL lt 1"/>
<c:if test="${parValue eq valLt100}">
  <c:set var="selectedLt100" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearLt100}"/>
</c:if>
<c:set var="fltrAppearPlT" value="< total"/>
<c:set var="valPlT" value="PAYMENTTOTAL lt ${docName}.ITSTOTAL"/>
<c:if test="${parValue eq valPlT}">
  <c:set var="selectedPLtT" value="selected"/>
  <c:set var="selectedDisabled1" value=""/>
  <c:set var="fltrAppear" value="${fltrAppearPlT}"/>
</c:if>
<div class="input-line">
  <c:if test="${filterMap[forcedFor].contains(fieldName)}">
    <b>${srvI18n.getMsg("forced")}</b>
    <label>${srvI18n.getMsg(fieldName)}</label>
    ${fltrAppear}
    <input type="hidden" name="${parVal}" value="${parValue}">
  </c:if>
  <c:if test="${!filterMap[forcedFor].contains(fieldName)}">
    <label for="${parVal}">${srvI18n.getMsg(fieldName)}</label>
    <select id="${parVal}" name="${parVal}" onchange="inputHasBeenChanged(this);">
      <option value="disabled" ${selectedDisabled1}>${srvI18n.getMsg("disabled")}</option>
      <option value="${valGteq30}" ${selectedGteq30}>${fltrAppearGteq30}</option>
      <option value="${valGteq50}" ${selectedGteq50}>${fltrAppearGteq50}</option>
      <option value="${valGteq100}" ${selectedGteq100}>${fltrAppearGteq100}</option>
      <option value="${valLt30}" ${selectedLt30}>${fltrAppearLt30}</option>
      <option value="${valLt50}" ${selectedLt50}>${fltrAppearLt50}</option>
      <option value="${valLt100}" ${selectedLt100}>${fltrAppearLt100}</option>
      <option value="${valPlT}" ${selectedPLtT}>${fltrAppearPlT}</option>
    </select>
  </c:if>
</div>
