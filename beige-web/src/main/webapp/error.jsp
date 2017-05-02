<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${empty short_message}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg('an_error')}
</c:if>
<c:if test="${short_message != null}">
    ${pageContext.servletContext.getAttribute("srvI18n").getMsg(short_message)}
</c:if>
