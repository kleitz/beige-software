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
<c:set var="wdgListHeader" value="${mngUvds.classesSettings[classEntity].get('wdgListHeader')}"/>
<c:if test="${not empty wdgListHeader}">
  <jsp:include page="forList/${wdgListHeader}.jsp"/>
</c:if>
<c:set var="wdgListActions" value="${mngUvds.classesSettings.get(classEntity).get('wdgListActions')}"/>
<c:set var="objectIdName" value="${srvOrm.tablesMap[classEntity.simpleName].idFieldName}" scope="request"/>
<c:set var="cnvFtfsName" value="${hldCnvFtfsNames.getFor(classEntity, objectIdName)}"/>
<c:set var="cnvFtfsId" value="${fctCnvFtfs.lazyGet(null, cnvFtfsName)}" scope="request"/>
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
<c:if test="${empty wdgListFooter}">
  <c:set var="wdgListFooter" value="${mngUvds.classesSettings[classEntity].get('wdgListFooter')}"/>
</c:if>
<c:if test="${not empty wdgListFooter}">
  <jsp:include page="${param.mobile}forList/${wdgListFooter}.jsp"/>
</c:if>
