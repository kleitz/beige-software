<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:formatDate var="docDate" value="${entity.itsDate}" pattern="dd.MM.yy"/>
<c:set var="docAppr" value="# ${entity.itsId}, ${docDate}, ${entity.itsTotal}"/>
<button class="btn btn-sm" onClick="selectEntity('${entity.itsId}', '${docAppr}', '${namePlace}${param.nameEntity}')">${srvI18n.getMsg("Pick")}</button>
