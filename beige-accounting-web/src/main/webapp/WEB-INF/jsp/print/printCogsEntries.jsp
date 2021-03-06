<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty cogsEntries}">
  <c:set var="entitySimpleName" value="CogsEntry" scope="request"/>
  <c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(classCogsEntry, 'orderPrintfullList')}" scope="request"/>
  <div class="title-list"> ${srvI18n.getMsg('cogsEntries')}: </div>
  <table>
    <tr>
      <c:forEach var="entryFld" items="${fieldsForList}">
        <th>${srvI18n.getMsg(entryFld.key)}</th>
      </c:forEach>
    </tr>
    <c:forEach var="entity" items="${cogsEntries}">
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
</c:if>
