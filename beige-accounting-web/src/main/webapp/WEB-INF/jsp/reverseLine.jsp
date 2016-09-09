<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="actTitle" value="${srvI18n.getMsg('Reverse')}"/>
<c:set var="wdgName" value="wdgReverseForm" scope="request"/>
<dialog id="${namePlaceSubForm}EditDlg" class="dlg dlg-sub">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg(actTitle)} ${srvI18n.getMsg(param.nameEntityFromOwnedList)}
      <button onclick="closeDlgCareful('${namePlaceSubForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceSubForm}EditFrm" action="entity/" method="POST">
      <input type="hidden" name="nameAction" value="saveFromOwnedList">
      <input type="hidden" name="nameEntity" value="${param.nameEntity}">
      <input type="hidden" name="idEntity" value="${param.idEntity}">
      <input type="hidden" name="nameEntityFromOwnedList" value="${param.nameEntityFromOwnedList}">
      <input type="hidden" name="page" value="${param.page}">
      <input type="hidden" name="nameRenderer" value="editEntityFromOwnedListSavedJson">
      <input type="hidden" name="mobile" value="${param.mobile}">
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
        <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass().canonicalName, 'orderShowForm')}">
          <c:if test="${not empty entry.value.get(wdgName)}">
            <c:set var="fieldName" value="${entry.key}" scope="request"/>
            <c:set var="fieldSettings" value="${entry.value}" scope="request"/>
            <jsp:include page="forForm/${entry.value.get('wdgInputWrapper')}.jsp"/>
          </c:if>
        </c:forEach>
      </table>
      <div class="form-actions">
        <input style="display: none" id="${namePlaceSubForm}EditFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${namePlaceSubForm}EditFrm', true);">${srvI18n.getMsg("Reverse")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlaceSubForm}Edit');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
