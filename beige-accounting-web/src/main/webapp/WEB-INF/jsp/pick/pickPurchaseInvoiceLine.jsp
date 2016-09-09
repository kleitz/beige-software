<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="docLineAppr" value="# ${entity.itsId}, ${entity.invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity.itsCost}, ${srvI18n.getMsg('rest_was')}=${entity.theRest}"/>
<button class="btn btn-sm" onClick="selectEntity('${entity.itsId}', '${docLineAppr}', '${namePlace}${param.nameEntity}')">${srvI18n.getMsg("Pick")}</button>
