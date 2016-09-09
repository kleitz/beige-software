<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="entryFieldSettings" items="${fieldsForList}">
  <c:set var="fieldSettings" value="${entryFieldSettings.value}" scope="request"/>
  <c:set var="model" value="${entity[entryFieldSettings.key]}" scope="request"/>
  <jsp:include page="${fieldSettings.get('wdgToCellDetail')}.jsp"/>
</c:forEach>
