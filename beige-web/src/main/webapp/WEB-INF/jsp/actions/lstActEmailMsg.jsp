<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=EmailMsg&EmailMsg.itsId=${entity.itsId}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityCopy&nmEnt=EmailMsg&EmailMsg.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Copy")}</button>
  <c:if test="${!entity.isSent}">
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=EmailMsg&EmailMsg.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=listWholeAfterListActionJson&nmsAct=entitySave&actionAdd=sendFromList&msgSuccess=send_ok&nmEnt=EmailMsg&EmailMsg.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Send")}</button>
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=EmailMsg&EmailMsg.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
  </c:if>
</c:if>
