<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cnvFtfsName" value="${hldCnvFtfsNames.getFor(entity.getClass(), fieldName)}"/>
<c:set var="cnvFtfs" value="${fctCnvFtfs.lazyGet(null, cnvFtfsName)}"/>
<input type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${cnvFtfs.toString(null, entity[fieldName])}">
<c:set var="ownerVersionParam" value="${entity[fieldName].getClass().simpleName}.ownerVersion"/>
<input type="hidden" name="${ownerVersionParam}" value="${param[ownerVersionParam]}">
