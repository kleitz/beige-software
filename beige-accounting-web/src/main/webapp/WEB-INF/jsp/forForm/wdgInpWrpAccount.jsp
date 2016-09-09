<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${entity.isCreatedByUser}">
  <jsp:include page="../${param.mobile}inputs/${fieldSettings.get(wdgName)}.jsp"/>
</c:if>
<c:if test="${!entity.isCreatedByUser}">
  <jsp:include page="../${param.mobile}inputs/inputReadOnly.jsp"/>
</c:if>
