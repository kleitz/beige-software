<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${model}">
  ${srvI18n.getMsg('Yes')}
</c:if>
<c:if test="${not model}">
  ${srvI18n.getMsg('No')}
</c:if>
