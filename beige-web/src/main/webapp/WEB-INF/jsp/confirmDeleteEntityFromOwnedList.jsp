<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlaceSubForm}DeleteDlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg("Delete")} ${srvI18n.getMsg(param.nameEntityFromOwnedList)}
      <button onclick="closeDlgCareful('${namePlaceSubForm}Delete')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceSubForm}DeleteFrm" action="entity/" method="POST">
      <input type="hidden" name="nameAction" value="deleteFromOwnedList">
      <input type="hidden" name="nameEntity" value="${param.nameEntity}">
      <input type="hidden" name="idEntity" value="${param.idEntity}">
      <input type="hidden" name="nameEntityFromOwnedList" value="${param.nameEntityFromOwnedList}">
      <input type="hidden" name="page" value="${param.page}">
      <input type="hidden" name="nameRenderer" value="editEntityFromOwnedListSavedJson">
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
        <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass().canonicalName, 'orderShowForm')}" varStatus="vs">
          <c:if test="${not empty entry.value.get('wdgDeleteForm')}">
            <c:set var="fieldName" value="${entry.key}" scope="request"/>
            <jsp:include page="inputs/${entry.value.get('wdgDeleteForm')}.jsp"/>
          </c:if>
        </c:forEach>
      </table>
      <div class="form-actions">
        <input style="display: none" id="${namePlaceSubForm}DeleteFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${namePlaceSubForm}DeleteFrm', false);">${srvI18n.getMsg("Delete")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlaceSubForm}Delete');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
