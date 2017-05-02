<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
  <a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
  <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityJson&nmsAct=entityEdit&nmEnt=${classEntity.simpleName}&${classEntity.simpleName}.${objectIdName}=${entityIdStr}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
</c:if>
