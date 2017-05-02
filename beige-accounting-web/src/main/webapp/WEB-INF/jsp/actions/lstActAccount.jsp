<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=Account&Account.itsId=${entity.itsId}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=Account&Account.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
  <c:if test="${entity.isCreatedByUser}">
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=Account&Account.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
  </c:if>
</c:if>
