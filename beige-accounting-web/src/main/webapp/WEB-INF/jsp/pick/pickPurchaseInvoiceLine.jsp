<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty entity.idBirth}">
  <c:set var="docLineAppr" value="# ${entity.idDatabaseBirth}-${entity.itsId}, ${entity.invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity.itsCost}, ${srvI18n.getMsg('rest_was')}=${entity.theRest}"/>
</c:if>
<c:if test="${not empty entity.idBirth}">
  <c:set var="docLineAppr" value="# ${entity.idDatabaseBirth}-${entity.idBirth}, ${entity.invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity.itsCost}, ${srvI18n.getMsg('rest_was')}=${entity.theRest}"/>
</c:if>
<button class="btn btn-sm" onClick="selectEntity('${entity.itsId}', '${docLineAppr}', '${namePlace}${param.nameEntity}')">${srvI18n.getMsg("Pick")}</button>
