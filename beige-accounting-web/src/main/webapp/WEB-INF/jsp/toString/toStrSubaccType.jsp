<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty model}">
  ${srvI18n.getMsg(typeCodeSubaccMap.get(model).simpleName)}
</c:if>
