<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pages">
  <c:forEach var="page" items="${pages}">
    <c:if test="${page.value eq '...'}">
      <span class="page-inactive">...</span>
    </c:if>
    <c:if test="${!(page.value eq '...') && page.isCurrent}">
      <a href="#" class="page-current" onclick="getHtmlByAjax('GET', 'service/?nmRnd=${nmRndList}&nmsAct=list&nmHnd=${param.nmHnd}&nmEnt=${classEntity.simpleName}&page=${page.value}${flyParams}');">${page.value}</a>
    </c:if>
    <c:if test="${!(page.value eq '...') && !page.isCurrent}">
      <a href="#" class="page" onclick="getHtmlByAjax('GET', 'service/?nmRnd=${nmRndList}&nmsAct=list&nmHnd=${param.nmHnd}&nmEnt=${classEntity.simpleName}&page=${page.value}${flyParams}');">${page.value}</a>
    </c:if>
  </c:forEach>
  <button id="listMainNew" onclick="getHtmlByAjax('GET', 'service/?nmRnd=editEntityJson&nmsAct=entityCreate&nmHnd=${param.nmHnd}&nmEnt=${classEntity.simpleName}&page=${param.page}${flyParams}');" class="btn btn-sm">
    ${srvI18n.getMsg("New")}
  </button>
</div>
