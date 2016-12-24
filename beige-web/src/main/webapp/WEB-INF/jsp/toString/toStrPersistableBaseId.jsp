<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty entity.idBirth}">
  ${entity.idDatabaseBirth}-${entity.itsId}
</c:if>
<c:if test="${not empty entity.idBirth}">
  ${entity.idDatabaseBirth}-${entity.idBirth}
</c:if>
