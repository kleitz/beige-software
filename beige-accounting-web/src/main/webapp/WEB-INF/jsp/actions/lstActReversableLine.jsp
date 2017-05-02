<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty entity['reversedId']}">
  <c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
  <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityFolJson&nmsAct=entityCopy&nmEnt=${entitySimpleName}&${entitySimpleName}.${objectIdName}=${entityIdStr}${ownerVersion}${flyParams}');">${srvI18n.getMsg("Copy")}</button>
  <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=reverseLineJson&nmsAct=entityReverse&nmEnt=${entitySimpleName}&${entitySimpleName}.${objectIdName}=${entityIdStr}${ownerVersion}${flyParams}');">${srvI18n.getMsg("Reverse")}</button>
</c:if>
