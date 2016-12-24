<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${not empty model.itsId}">
 # ${model.idDatabaseBirth}-
  <c:if test="${not idBirth}">
    ${model.itsId}
  </c:if>
  <c:if test="${not empty idBirth}">
    ${model.idBirth} (${model.itsId})
  </c:if>
  , <fmt:formatDate value="${model.itsDate}" type="both" timeStyle="short"/>, ${model.itsTotal}
</c:if>
