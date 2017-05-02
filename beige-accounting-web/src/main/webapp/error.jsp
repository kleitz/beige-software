<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
  <c:when test="${error_code == 1151}">
    <c:if test="${param.nmEnt eq 'PurchaseInvoiceLine' or param.nmEnt eq 'SalesInvoiceLine'}">
      ${pageContext.servletContext.getAttribute("srvI18n").getMsg('dirty_read_or_buzy')}
    </c:if>
    <c:if test="${!(param.nmEnt eq 'PurchaseInvoiceLine' or param.nmEnt eq 'SalesInvoiceLine')}">
      ${pageContext.servletContext.getAttribute("srvI18n").getMsg('dirty_read')}
    </c:if>
  </c:when>
  <c:when test="${error_code == 1301}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('there_is_no_goods_in_stock')}
  </c:when>
  <c:when test="${error_code == 1302}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('PRODUCT_SUPPLIER_DIFFER')}
  </c:when>
  <c:when test="${error_code == 403}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('forbidden')}
  </c:when>
  <c:when test="${error_code == 1003}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg(short_message)}
  </c:when>
  <c:otherwise>
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('an_error')}
  </c:otherwise>
</c:choose>
