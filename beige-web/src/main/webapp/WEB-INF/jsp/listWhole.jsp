<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="${namePlace}list">
  <jsp:include page="list.jsp"/>
</div>
<c:if test="${not empty mngUvds.classesSettings.get(classEntity.canonicalName).get('wdgFilterOrder')}">
  <jsp:include page="filterOrder/${mngUvds.classesSettings.get(classEntity.canonicalName).get('wdgFilterOrder')}.jsp"/>
</c:if>
