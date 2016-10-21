<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="parVal" value="${fltOrdPrefix}${fieldName}Val"/>
<div class="input-line">
  <c:set var="selectedDisabled1" value="selected"/>
  <c:if test="${parVal eq 'PAYMENTTOTAL/docName.ITSTOTAL gteq 0.30'}"><c:set var="selectedGteq30" value="selected"/></c:if>
  <c:if test="${parVal eq 'PAYMENTTOTAL/docName.ITSTOTAL gteq 0.50'}"><c:set var="selectedGteq50" value="selected"/></c:if>
  <c:if test="${parVal eq 'PAYMENTTOTAL/docName.ITSTOTAL gteq 1'}"><c:set var="selectedGteq100" value="selected"/></c:if>
  <c:if test="${parVal eq 'PAYMENTTOTAL/docName.ITSTOTAL lt 0.30'}"><c:set var="selectedLt30" value="selected"/></c:if>
  <c:if test="${parVal eq 'PAYMENTTOTAL/docName.ITSTOTAL lt 0.50'}"><c:set var="selectedLt50" value="selected"/></c:if>
  <c:if test="${parVal eq 'PAYMENTTOTAL/docName.ITSTOTAL lt 1'}"><c:set var="selectedLt100" value="selected"/></c:if>
  <label for="${parVal}">${srvI18n.getMsg(fieldName)}</label>
  <select name="${parVal}" onchange="filterOperChanged(this, '${parVal}');">
    <option value="disabled" ${selectedDisabled1}>${srvI18n.getMsg("disabled")}</option>
    <option value="PAYMENTTOTAL/${docName}.ITSTOTAL gteq 0.30" ${selectedGteq5}>&gt;= 30%</option>
    <option value="PAYMENTTOTAL/${docName}.ITSTOTAL gteq 0.50" ${selectedGteq5}>&gt;= 50%</option>
    <option value="PAYMENTTOTAL/${docName}.ITSTOTAL gteq 1" ${selectedGteq5}>&gt;= 100%</option>
    <option value="PAYMENTTOTAL/${docName}.ITSTOTAL lt 0.30" ${selectedGteq5}>&lt; 30%</option>
    <option value="PAYMENTTOTAL/${docName}.ITSTOTAL lt 0.50" ${selectedGteq5}>&lt; 50%</option>
    <option value="PAYMENTTOTAL/${docName}.ITSTOTAL lt 1" ${selectedGteq5}>&lt; 100%</option>
  </select>
</div>
