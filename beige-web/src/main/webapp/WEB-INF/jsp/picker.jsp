<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(classEntity, 'orderShowPicker')}" scope="request"/>
<div class="title-list">
  <c:if test="${not empty mngUvds.classesSettings.get(classEntity).get('wdgFilterOrder')}">
    <button onclick="openDlg('${prefixFilterOrderForm}FltOrdDlg');" class="btn btn-sm">${srvI18n.getMsg("filterOrder")}</button>
  </c:if>
</div>
<table>
  <tr>
    <jsp:include page="${param.mobile}forList/wdgToRowHeader.jsp"/>
    <th class="column-actions">${srvI18n.getMsg('Actions')}</th>
  </tr>
  <c:forEach var="entity" items="${entities}">
    <c:set var="entity" value="${entity}" scope="request"/>
    <tr class="tr-picker">
      <jsp:include page="${param.mobile}forList/wdgToRowDetail.jsp"/>
      <td class="column-actions">
        <c:if test="${empty param.wdgPick}">
          <jsp:include page="pick/${mngUvds.classesSettings[classEntity].get('wdgPick')}.jsp"/>
        </c:if>
        <c:if test="${not empty param.wdgPick}">
          <jsp:include page="pick/${param.wdgPick}.jsp"/>
        </c:if>
      </td>
    </tr>
  </c:forEach>
</table>
<div class="pages">
  <c:if test="${empty param.mobile}">
    <c:set var="flyParams" value="" scope="request"/>
  </c:if>
  <c:if test="${not empty param.mobile}">
    <c:set var="flyParams" value="&mobile=${param.mobile}" scope="request"/>
  </c:if>
  <c:forEach var="entry" items="${filterMap}">
    <c:set var="flyParams" value="${flyParams}&${entry.key}=${entry.value}"/>
  </c:forEach>
  <c:forEach var="entry" items="${orderMap}">
    <c:set var="flyParams" value="${flyParams}&${entry.key}=${entry.value}"/>
  </c:forEach>
  <c:if test="${not empty param.wdgPick}">
    <c:set var="flyParams" value="${flyParams}&wdgPick=${param.wdgPick}"/>
  </c:if>
  <c:forEach var="page" items="${pages}">
    <c:if test="${page.value eq '...'}">
      <span class="page-inactive">...</span>
    </c:if>
    <c:if test="${!(page.value eq '...') && page.isCurrent}">
      <a href="#" class="page-current" onclick="getHtmlByAjax('GET', 'service/?nmRnd=${nmRndList}&nmsAct=list&nmHnd=${param.nmHnd}&nmEnt=${param.nmEnt}&page=${page.value}${flyParams}');">${page.value}</a>
    </c:if>
    <c:if test="${!(page.value eq '...') && !page.isCurrent}">
      <a href="#" class="page" onclick="getHtmlByAjax('GET', 'service/?nmRnd=${nmRndList}&nmsAct=list&nmHnd=${param.nmHnd}&nmEnt=${param.nmEnt}&page=${page.value}${flyParams}');">${page.value}</a>
    </c:if>
  </c:forEach>
</div>
