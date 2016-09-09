<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.isExtractSalesTaxFromPurchase}">
  <jsp:include page="../${param.mobile}inputs/${fieldSettings.get(wdgName)}.jsp"/>
</c:if>
<c:if test="${!accSettings.isExtractSalesTaxFromPurchase}">
  <jsp:include page="../${param.mobile}inputs/inputHidden.jsp"/>
</c:if>
