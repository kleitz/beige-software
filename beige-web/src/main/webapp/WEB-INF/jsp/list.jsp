<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="flyParams" value="&mobile=${param.mobile}" scope="request"/>
<c:forEach var="entry" items="${filterMap}">
  <c:set var="flyParams" value="${flyParams}&${entry.key}=${utlJsp.escapeHtml(entry.value)}" scope="request"/>
</c:forEach>
<c:forEach var="entry" items="${orderMap}">
  <c:set var="flyParams" value="${flyParams}&${entry.key}=${utlJsp.escapeHtml(entry.value)}" scope="request"/>
</c:forEach>
<c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(classEntity.canonicalName, 'orderShowList')}" scope="request"/>
<c:set var="wdgListHeader" value="${mngUvds.classesSettings[classEntity.canonicalName].get('wdgListHeader')}"/>
<c:if test="${not empty wdgListHeader}">
  <jsp:include page="forList/${wdgListHeader}.jsp"/>
</c:if>
<c:set var="wdgListActions" value="${mngUvds.classesSettings.get(classEntity.canonicalName).get('wdgListActions')}"/>
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
        <c:if test="${not empty wdgListActions}">
          <jsp:include page="actions/${wdgListActions}.jsp"/>
        </c:if>
      </td>
    </tr>
  </c:forEach>
</table>
<c:set var="wdgListFooter" value="${mngUvds.classesSettings[classEntity.canonicalName].get('wdgListFooter')}"/>
<c:if test="${not empty wdgListFooter}">
  <jsp:include page="${param.mobile}forList/${wdgListFooter}.jsp"/>
</c:if>
