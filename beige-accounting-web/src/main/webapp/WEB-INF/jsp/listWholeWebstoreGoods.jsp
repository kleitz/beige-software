<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="wdgListFooter" value="wdgListFooterWebstoreGoods" scope="request"/>
<div id="${namePlace}list">
  <jsp:include page="list.jsp"/>
</div>
<div id="${namePlace}assignGoodCat">
  <jsp:include page="assignGoodsToCatalogForm.jsp"/>
</div>
<jsp:include page="filterOrder/${mngUvds.classesSettings.get(classEntity).get('wdgFilterOrder')}.jsp"/>
