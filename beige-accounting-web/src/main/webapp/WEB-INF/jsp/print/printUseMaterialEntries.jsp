<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty useMaterialEntries}">
  <c:set var="entitySimpleName" value="UseMaterialEntry" scope="request"/>
  <c:set var="entityCanonicalName" value="org.beigesoft.accounting.persistable.UseMaterialEntry" scope="request"/>
  <c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(entityCanonicalName, 'orderPrintfullList')}" scope="request"/>
  <div class="title-list"> ${srvI18n.getMsg('useMaterialEntries')}: </div>
  <table>
    <tr>
      <c:forEach var="entryFld" items="${fieldsForList}">
        <th>${srvI18n.getMsg(entryFld.key)}</th>
      </c:forEach>
    </tr>
    <c:forEach var="entity" items="${useMaterialEntries}">
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
