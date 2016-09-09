<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.isExtractSalesTaxFromPurchase || accSettings.isExtractSalesTaxFromSales}">
  <td><jsp:include page="../toString/${fieldSettings.get('wdgToString')}.jsp"/></td>
</c:if>
