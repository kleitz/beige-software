<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
<button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityFolJson&nmsAct=entityEdit&nmEnt=${entitySimpleName}&${entitySimpleName}.${objectIdName}=${entityIdStr}${ownerVersion}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
