<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="nameEnts" value="${classEntity.simpleName}s"/>
<div class="title-list">${srvI18n.getMsg(nameEnts)}
  <c:if test="${not empty mngUvds.classesSettings.get(classEntity).get('wdgFilterOrder')}">
    <button onclick="openDlg('${namePlace}FltOrdDlg');" class="btn">${srvI18n.getMsg("filterOrder")}</button>
  </c:if>
</div>
