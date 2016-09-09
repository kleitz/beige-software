<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
  <c:when test="${error_code == 1151}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('dirty_read')}
  </c:when>
  <c:when test="${error_code == 403}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('forbidden')}
  </c:when>
  <c:otherwise>
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('an_error')}
  </c:otherwise>
</c:choose>
