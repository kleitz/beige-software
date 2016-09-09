<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach var="ownedListsMapEntry" items="${ownedListsMap}">
  <c:set var="entitySimpleName" value="${ownedListsMapEntry.key.simpleName}" scope="request"/>
  <c:set var="entityCanonicalName" value="${ownedListsMapEntry.key.canonicalName}" scope="request"/>
  <c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(entityCanonicalName, orderPrintList)}" scope="request"/>
  <c:set var="nameEnts" value="${entitySimpleName}s"/>
  <div class="title-list"> ${srvI18n.getMsg(nameEnts)}: </div>
  <table>
    <tr>
      <c:forEach var="entryFld" items="${fieldsForList}">
        <th>${srvI18n.getMsg(entryFld.key)}</th>
      </c:forEach>
    </tr>
    <c:forEach var="entity" items="${ownedListsMapEntry.value}">
      <c:set var="entity" value="${entity}" scope="request"/>
      <tr>
        <c:forEach var="entryFld" items="${fieldsForList}">
          <c:set var="model" value="${entity[entryFld.key]}" scope="request"/>
          <td>
            <jsp:include page="../toString/${entryFld.value['wdgToString']}.jsp"/>
          </td>
        </c:forEach>
      </tr>
    </c:forEach>
  </table>
  <br>
</c:forEach>
