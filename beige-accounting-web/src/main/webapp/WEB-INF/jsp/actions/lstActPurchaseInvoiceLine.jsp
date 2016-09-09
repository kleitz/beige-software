<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty entity['reversedId']}">
  <c:if test="${empty entity['invItem'].knownCost}">
    <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editEntityFromOwnedListJson&nameAction=copyFromOwnedList&nameEntityFromOwnedList=${entitySimpleName}&idEntityFromOwnedList=${entity.itsId}${itsOwnerParams}');">${srvI18n.getMsg("Copy")}</button>
  </c:if>
  <c:if test="${not empty entity['invItem'].knownCost}">
    <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editLineKnownCostJson&nameAction=copyFromOwnedList&nameEntityFromOwnedList=${entitySimpleName}&idEntityFromOwnedList=${entity.itsId}${itsOwnerParams}');">${srvI18n.getMsg("Copy")}</button>
  </c:if>
  <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=reverseLineJson&nameAction=copyFromOwnedList&actionAdd=reverse&nameEntityFromOwnedList=${entitySimpleName}&idEntityFromOwnedList=${entity.itsId}${itsOwnerParams}');">${srvI18n.getMsg("Reverse")}</button>
</c:if>
