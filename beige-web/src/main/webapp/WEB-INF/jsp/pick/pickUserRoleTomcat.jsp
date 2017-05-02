<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cnvFtfsName" value="${hldCnvFtfsNames.getFor(entity.getClass(), 'itsId')}"/>
<c:set var="cnvFtfs" value="${fctCnvFtfs.lazyGet(null, cnvFtfsName)}"/>
<button class="btn btn-sm" onClick="selectEntity('${cnvFtfs.toString(null, entity.itsId)}', &quot;${entity.itsUser.itsUser} ${entity.itsRole}&quot;, '${namePlace}${param.nmEnt}')">${srvI18n.getMsg("Pick")}</button>
