<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="actTitle" value="Edit"/>
<c:set var="wdgName" value="wdgEditForm"/>
<c:set var="msgSuccess" value="edit_ok" scope="request"/>
<c:if test="${entity.isNew}">
  <c:set var="actTitle" value="New"/>
  <c:set var="wdgName" value="wdgNewForm"/>
  <c:set var="msgSuccess" value="insert_ok" scope="request"/>
</c:if>
<dialog id="${namePlaceSubForm}EditDlg" class="dlg dlg-sub" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg(actTitle)} ${srvI18n.getMsg(entity.getClass().simpleName)}
      <button onclick="closeDlgCareful('${namePlaceSubForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceSubForm}EditFrm" action="service/" method="POST" enctype="multipart/form-data">
      <input type="hidden" name="nmsAct" value="entityFolSave,entityEdit,list">
      <input type="hidden" name="nmHnd" value="${param.nmHnd}">
      <input type="hidden" name="nmEnt" value="${entity.getClass().simpleName}">
      <input type="hidden" name="nmRnd" value="editEntityFolSavedJson">
      <input type="hidden" name="nmRndList" value="${param.nmRndList}">
      <input type="hidden" name="msgSuccess" value="${msgSuccess}">
      <input type="hidden" name="page" value="${param.page}">
      <c:if test="${not empty param.mobile}">
        <input type="hidden" name="mobile" value="${param.mobile}">
      </c:if>
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
        <c:set var="fieldName" value="isNew" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputHidden.jsp"/>
        <c:set var="fieldName" value="idDatabaseBirth" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputHidden.jsp"/>
        <c:set var="fieldName" value="idBirth" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputHidden.jsp"/>
        <c:set var="fieldName" value="itsOwner" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputEntityHasIdHidden.jsp"/>
        <c:set var="fieldName" value="warehouseSite" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputEntityHasName.jsp"/>
        <c:set var="fieldName" value="invItem" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputPurchaseItemKnownCost.jsp"/>
        <c:set var="fieldName" value="unitOfMeasure" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputEntityHasNameReadOnly.jsp"/>
        <jsp:include page="${param.mobile}inputs/inputKnownCostQuantityTotal.jsp"/>
        <c:set var="fieldName" value="description" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputText.jsp"/>
      </table>
      <div class="form-actions">
        <input style="display: none" id="${namePlaceSubForm}EditFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${namePlaceSubForm}EditFrm');">${srvI18n.getMsg("Save")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlaceSubForm}Edit');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
