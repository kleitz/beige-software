<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
  <c:set var="isFileAction" value="${entity.specifics.itsType eq 'FILE' || entity.specifics.itsType eq 'IMAGE' || entity.specifics.itsType eq 'IMAGE_IN_SET' ||  entity.specifics.itsType eq 'FILE_EMBEDDED'}"/>
  <c:if test="${!(not empty entity.stringValue2 && isFileAction)}">
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=GoodsSpecific&GoodsSpecific.itsId=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
  </c:if>
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=GoodsSpecific&GoodsSpecific.itsId=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
</c:if>
