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
<dialog id="${namePlaceSubForm}EditDlg" class="dlg dlg-sub" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg(actTitle)} ${srvI18n.getMsg(param.nmEnt)}
      <button onclick="closeDlgCareful('${namePlaceSubForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceSubForm}EditFrm" action="service/" method="POST" enctype="multipart/form-data">
      <input type="hidden" name="nmsAct" value="entityFolSave,entityEdit,list">
      <input type="hidden" name="nmHnd" value="${param.nmHnd}">
      <input type="hidden" name="nmEnt" value="${entity.getClass().simpleName}">
      <input type="hidden" name="msgSuccess" value="${msgSuccess}">
      <input type="hidden" name="nmRnd" value="editEntityFolSavedJson">
      <input type="hidden" name="nmRndList" value="${param.nmRndList}">
      <input type="hidden" name="page" value="${param.page}">
      <c:if test="${not empty param.mobile}">
        <input type="hidden" name="mobile" value="${param.mobile}">
      </c:if>
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM') || par.key.startsWith('fly')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
        <c:set var="autofocus" value="autofocus" scope="request"/>
        <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass(), 'orderShowForm')}">
          <c:if test="${not empty entry.value.get(wdgName)}">
            <c:set var="fieldName" value="${entry.key}" scope="request"/>
            <c:set var="fieldSettings" value="${entry.value}" scope="request"/>
            <jsp:include page="forForm/${fieldSettings.get('wdgInputWrapper')}.jsp"/>
        </c:if>
        </c:forEach>
      </table>
      <div class="form-actions">
        <input style="display: none" id="${namePlaceSubForm}EditFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${namePlaceSubForm}EditFrm');">${srvI18n.getMsg("Save")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlaceSubForm}Edit');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
