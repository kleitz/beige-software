<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty entity['reversedId']}">
  <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editEntityFromOwnedListJson&nameAction=copyFromOwnedList&nameEntityFromOwnedList=${entitySimpleName}&idEntityFromOwnedList=${entity.itsId}${itsOwnerParams}');">${srvI18n.getMsg("Copy")}</button>
  <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=reverseLineJson&nameAction=copyFromOwnedList&actionAdd=reverse&nameEntityFromOwnedList=${entitySimpleName}&idEntityFromOwnedList=${entity.itsId}${itsOwnerParams}');">${srvI18n.getMsg("Reverse")}</button>
</c:if>
