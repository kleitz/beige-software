<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="nameEnts" value="${entitySimpleName}s"/>
<c:set var="wdgListActions" value="${mngUvds.classesSettings.get(entityCanonicalName).get('wdgListActions')}"/>
<div class="title-list"> ${srvI18n.getMsg(nameEnts)} </div>
<table>
  <tr>
    <jsp:include page="../${param.mobile}forList/wdgToRowHeader.jsp"/>
    <th>${srvI18n.getMsg('Actions')}</th>
  </tr>
  <c:forEach var="entity" items="${ownedListsMapEntry.value}">
    <c:set var="entity" value="${entity}" scope="request"/>
    <tr>
      <jsp:include page="../${param.mobile}forList/wdgToRowDetail.jsp"/>
      <td>
        <c:if test="${not empty wdgListActions}">
          <jsp:include page="../actions/${wdgListActions}.jsp"/>
        </c:if>
      </td>
    </tr>
  </c:forEach>
</table>
<div class="pages">
  <button onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editEntityFromOwnedListJson&nameAction=createFromOwnedListTransactional&nameEntityFromOwnedList=${entitySimpleName}${itsOwnerParams}');" class="btn btn-sm">
    ${srvI18n.getMsg("New")}
  </button>
  <button onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editLineKnownCostJson&nameAction=createFromOwnedListTransactional&nameEntityFromOwnedList=${entitySimpleName}${itsOwnerParams}');" class="btn btn-sm">
    ${srvI18n.getMsg("New_known_cost")}
  </button>
</div>
