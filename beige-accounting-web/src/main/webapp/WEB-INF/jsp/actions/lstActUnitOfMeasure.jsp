<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <c:if test="${entity.itsId gt 12}">
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'entity/?nameRenderer=editEntityJson&nameAction=edit&nameEntity=${param.nameEntity}&page=${param.page}&idEntity=${entity.itsId}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
    <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'entity/?nameRenderer=confirmDeleteEntityJson&nameAction=confirmDelete&nameEntity=${param.nameEntity}&page=${param.page}&idEntity=${entity.itsId}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
  </c:if>
</c:if>
