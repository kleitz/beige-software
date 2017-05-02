<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="entityIdStr" value="${cnvFtfsId.toString(null, entity[objectIdName])}"/>
<a href="service/?nmHnd=${param.nmHnd}&nmRnd=printEntity&nmsAct=entityPrint&nmEnt=${entitySimpleName}&${entitySimpleName}.${objectIdName}=${entityIdStr}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
