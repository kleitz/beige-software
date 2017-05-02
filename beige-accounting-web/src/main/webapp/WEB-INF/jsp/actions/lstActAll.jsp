<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
<a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
<a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&actionAdd=full&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("PrintFull")}</a>
<button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityCopy&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Copy")}</button>
<button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
<button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
