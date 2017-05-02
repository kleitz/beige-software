<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
<a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
<c:if test="${entity.hasMadeAccEntries}">
  <a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&actionAdd=full&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("PrintFull")}</a>
</c:if>
<button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityCopy&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Copy")}</button>
<c:if test="${!entity.hasMadeAccEntries}">
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
</c:if>
<c:if test="${entity.itsTotal.doubleValue() == 0}">
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
</c:if>
<c:if test="${entity.hasMadeAccEntries && empty entity.reversedId}">
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=reverseDocJson&nmsAct=entityReverse&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Reverse")}</button>
</c:if>
