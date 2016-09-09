<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlaceForm}EditDlg" class="dlg dlg-sub" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg("Edit")} ${srvI18n.getMsg(param.nameEntity)}
      <button onclick="closeDlgCareful('${namePlaceForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceForm}EditFrm" action="entity/" method="POST">
      <input type="hidden" name="nameAction" value="save">
      <input type="hidden" name="nameEntity" value="${param.nameEntity}">
      <input type="hidden" name="page" value="${param.page}">
      <input type="hidden" name="mobile" value="${param.mobile}">
      <input type="hidden" name="nameRenderer" value="listAccEntriesAfterEditJson">
      <input type="hidden" name="nameSrvEntity" value="srvAccEntryEditDescr">
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
        <c:set var="fieldName" value="isNew" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputHidden.jsp"/>
        <c:set var="fieldName" value="itsId" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputReadOnly.jsp"/>
        <c:set var="fieldName" value="debit" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputReadOnly.jsp"/>
        <c:set var="fieldName" value="credit" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputReadOnly.jsp"/>
        <c:set var="fieldName" value="accDebit" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputEntityHasNameReadOnly.jsp"/>
        <c:set var="fieldName" value="accCredit" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputEntityHasNameReadOnly.jsp"/>
        <c:set var="fieldName" value="description" scope="request"/>
        <jsp:include page="${param.mobile}inputs/inputText.jsp"/>
      </table>
      <div class="form-actions">
        <input style="display: none" id="${namePlaceForm}EditFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${namePlaceForm}EditFrm');">${srvI18n.getMsg("Save")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlaceForm}Edit');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
