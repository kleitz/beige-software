<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.isExtractSalesTaxFromSales}">
  <th>${srvI18n.getMsg(fieldName)}</th>
</c:if>
