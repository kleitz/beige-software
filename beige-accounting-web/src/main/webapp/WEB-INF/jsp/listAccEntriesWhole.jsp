<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="${namePlace}list">
  <jsp:include page="listAccEntries.jsp"/>
</div>
<jsp:include page="filterOrder/filterOrderStd.jsp"/>
