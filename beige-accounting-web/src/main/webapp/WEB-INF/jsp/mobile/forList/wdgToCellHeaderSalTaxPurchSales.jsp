<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.isExtractSalesTaxFromPurchase || accSettings.isExtractSalesTaxFromSales}">
  <c:if test="${!varStatus.isFirst()}">, </c:if>
  ${srvI18n.getMsg(fieldName)}
</c:if>
