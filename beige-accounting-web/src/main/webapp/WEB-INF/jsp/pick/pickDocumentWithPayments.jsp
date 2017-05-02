<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:formatDate var="dateAppr" value="${entity.itsDate}"  type="date" timeStyle="short"/>
<c:if test="${empty entity.idBirth}">
  <c:set var="docAppr" value="# ${entity.idDatabaseBirth}-${entity.itsId}, ${dateAppr}, ${entity.itsTotal} / ${entity.paymentTotal}"/>
</c:if>
<c:if test="${not empty entity.idBirth}">
  <c:set var="docAppr" value="# ${entity.idDatabaseBirth}-${entity.idBirth}, ${dateAppr}, ${entity.itsTotal} / ${entity.paymentTotal}"/>
</c:if>
<button class="btn btn-sm" onClick="selectEntity('${entity.itsId}', '${docAppr}', '${namePlace}${param.nmEnt}')">${srvI18n.getMsg("Pick")}</button>
