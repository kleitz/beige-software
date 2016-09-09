<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<th>
  <c:forEach var="entryFieldSettings" items="${fieldsForList}" varStatus="varStatus">
    <c:set var="fieldName" value="${entryFieldSettings.key}" scope="request"/>
    <c:set var="fieldSettings" value="${entryFieldSettings.value}" scope="request"/>
    <c:set var="varStatus" value="${varStatus}" scope="request"/>
    <jsp:include page="${fieldSettings.get('wdgToCellHeader')}.jsp"/>
  </c:forEach>
</th>
