<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="actTitle" value="Edit"/>
<c:set var="wdgName" value="wdgEditForm" scope="request"/>
<c:set var="msgSuccess" value="edit_ok" scope="request"/>
<c:if test="${entity.isNew}">
  <c:set var="actTitle" value="New"/>
  <c:set var="wdgName" value="wdgNewForm" scope="request"/>
  <c:set var="msgSuccess" value="insert_ok" scope="request"/>
</c:if>
<dialog id="${namePlaceForm}EditDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg(actTitle)} ${srvI18n.getMsg(entity.getClass().simpleName)}
      <button onclick="closeDlgCareful('${namePlaceForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceForm}EditFrm" action="service/" method="POST" enctype="multipart/form-data">
      <input type="hidden" id="${namePlaceForm}EditFrm.nmsAct" name="nmsAct" value="entitySave,entityEdit,list">
      <input type="hidden" name="nmHnd" value="${param.nmHnd}">
      <input type="hidden" name="nmEnt" value="${entity.getClass().simpleName}">
      <input type="hidden" name="msgSuccess" value="${msgSuccess}">
      <input type="hidden" name="nmRndList" value="${param.nmRndList}">
      <input type="hidden" name="page" value="${param.page}">
      <c:if test="${not empty param.mobile}">
        <input type="hidden" name="mobile" value="${param.mobile}">
        <c:set var="flyParams" value="&page=${param.page}&mobile=${param.mobile}" scope="request"/>
      </c:if>
      <c:if test="${empty param.mobile}">
        <c:set var="flyParams" value="&page=${param.page}" scope="request"/>
      </c:if>
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM') || par.key.startsWith('fly')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
          <c:set var="flyParams" value="${flyParams}&${par.key}=${par.value}"/>
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
      <c:set var="ownerHasVersion" value="false"/>
      <c:set var="autofocus" value="autofocus" scope="request"/>
      <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass(), 'orderShowForm')}">
        <c:if test="${not empty entry.value.get(wdgName)}">
          <c:set var="fieldName" value="${entry.key}" scope="request"/>
          <c:set var="fieldSettings" value="${entry.value}" scope="request"/>
          <jsp:include page="forForm/${fieldSettings.get('wdgInputWrapper')}.jsp"/>
        </c:if>
        <c:if test="${entry.key eq 'itsVersion'}">
          <c:set var="ownerHasVersion" value="true"/>
        </c:if>
      </c:forEach>
      </table>
      <jsp:include page="forForm/${mngUvds.classesSettings.get(entity.getClass()).get('wdgFormActions')}.jsp"/>
    </form>
    <c:if test="${!entity.isNew && ownedListsMap != null}">
      <c:set var="objectIdName" value="${srvOrm.tablesMap[entity.getClass().simpleName].idFieldName}"/>
      <c:set var="cnvFtfsName" value="${hldCnvFtfsNames.getFor(entity.getClass(), objectIdName)}"/>
      <c:set var="cnvFtfs" value="${fctCnvFtfs.lazyGet(null, cnvFtfsName)}"/>
      <c:set var="ownerIdStr" value="${cnvFtfs.toString(null, entity[objectIdName])}" scope="request"/>
      <c:if test="${ownerHasVersion}">
        <c:set var="ownerVersion" value="&${entity.getClass().simpleName}.ownerVersion=${entity.itsVersion}" scope="request"/>
      </c:if>
      <c:forEach var="ownedListsMapEntry" items="${ownedListsMap}">
        <c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(ownedListsMapEntry.key, 'orderShowList')}" scope="request"/>
        <c:set var="entitySimpleName" value="${ownedListsMapEntry.key.simpleName}" scope="request"/>
        <c:set var="ownedListsMapEntry" value="${ownedListsMapEntry}" scope="request"/>
        <c:set var="objectIdName" value="${srvOrm.tablesMap[entitySimpleName].idFieldName}" scope="request"/>
        <c:set var="cnvFtfsName" value="${hldCnvFtfsNames.getFor(ownedListsMapEntry.key, objectIdName)}"/>
        <c:set var="cnvFtfsId" value="${fctCnvFtfs.lazyGet(null, cnvFtfsName)}" scope="request"/>
        <jsp:include page="ownedList/${mngUvds.classesSettings.get(ownedListsMapEntry.key).get('wdgOwnedList')}.jsp"/>
      </c:forEach>
    </c:if>
  </div>
</dialog>
