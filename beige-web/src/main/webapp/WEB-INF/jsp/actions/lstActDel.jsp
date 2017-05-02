<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <c:set var="entityIdStr" value="${cnvFtfs.toString(null, entity[objectIdName])}"/>
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=confirmDeleteEntityJson&nmsAct=entityConfirmDelete&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
</c:if>
