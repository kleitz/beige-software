<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty param.mobile}">
  <c:set var="flyParams" value="&nmRndList=${nmRndList}" scope="request"/>
</c:if>
<c:if test="${not empty param.mobile}">
  <c:set var="flyParams" value="&nmRndList=${nmRndList}&mobile=${param.mobile}" scope="request"/>
</c:if>
<c:forEach var="entry" items="${filterMap}">
  <c:set var="flyParams" value="${flyParams}&${entry.key}=${entry.value}" scope="request"/>
</c:forEach>
<c:forEach var="entry" items="${orderMap}">
  <c:set var="flyParams" value="${flyParams}&${entry.key}=${entry.value}" scope="request"/>
</c:forEach>
<c:forEach items="${param}" var="par">
  <c:if test="${par.key.startsWith('fly')}">
    <c:set var="flyParams" value="${flyParams}&${par.key}=${par.value}"/>
  </c:if>
</c:forEach>
<c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(classEntity, 'orderShowList')}" scope="request"/>
<jsp:include page="forList/wdgListHeader.jsp"/>
<table>
  <tr>
    <jsp:include page="${param.mobile}forList/wdgToRowHeader.jsp"/>
    <th class="column-actions">${srvI18n.getMsg('Actions')}</th>
  </tr>
  <c:forEach var="entity" items="${entities}">
    <c:set var="entity" value="${entity}" scope="request"/>
    <tr>
      <jsp:include page="${param.mobile}forList/wdgToRowDetail.jsp"/>
      <td class="column-actions">
        <button class="btn btn-sm" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editAccEntryJson&nmsAct=entityEdit&nmEnt=AccountingEntry&AccountingEntry.itsId=${entity.itsId}&page=${param.page}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
      </td>
    </tr>
  </c:forEach>
</table>
<div class="pages">
  <c:forEach var="page" items="${pages}">
    <c:if test="${page.value eq '...'}">
      <span class="page-inactive">...</span>
    </c:if>
    <c:if test="${!(page.value eq '...') && page.isCurrent}">
      <a href="#" class="page-current" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmsAct=list&nmRnd=${nmRndList}&nmEnt=AccountingEntry&page=${page.value}${flyParams}');">${page.value}</a>
    </c:if>
    <c:if test="${!(page.value eq '...') && !page.isCurrent}">
      <a href="#" class="page" onclick="getHtmlByAjax('GET', 'service/?nmHnd=${param.nmHnd}&nmsAct=list&nmRnd=${nmRndList}&nmEnt=AccountingEntry&page=${page.value}${flyParams}');">${page.value}</a>
    </c:if>
  </c:forEach>
</div>

