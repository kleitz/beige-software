<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.isExtractSalesTaxFromSales}">
  <c:set var="taxesTl" value="0"/>
  ${srvI18n.getMsg('taxes')}:
  <c:forEach var="entity" items="${ownedListsMapEntry.value}" varStatus="vs">
    <c:if test="${!vs.isFirst()}">, </c:if>
    ${entity.tax.itsName} ${entity.tax.itsPercentage}% = ${entity.itsTotal}
    <c:set var="taxesTl" value="${entity.itsTotal + taxesTl}"/>
  </c:forEach>
  <br> ${srvI18n.getMsg('totalTaxes')}: ${taxesTl}
</c:if>
