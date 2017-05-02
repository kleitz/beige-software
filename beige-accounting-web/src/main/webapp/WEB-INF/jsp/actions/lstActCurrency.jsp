<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <c:if test="${entity.itsId != 978 && entity.itsId != 643 && entity.itsId != 840}">
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=Currency&Currency.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=Currency&Currency.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
  </c:if>
</c:if>
