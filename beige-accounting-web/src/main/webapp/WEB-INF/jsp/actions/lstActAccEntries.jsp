<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=AccountingEntries&AccountingEntries.itsId=${entity.itsId}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
<a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&actionAdd=full&nmEnt=AccountingEntries&AccountingEntries.itsId=${entity.itsId}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("PrintFull")}</a>
<button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=AccountingEntries&AccountingEntries.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
<c:if test="${entity.totalDebit.doubleValue() == 0}">
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=AccountingEntries&AccountingEntries.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
</c:if>
