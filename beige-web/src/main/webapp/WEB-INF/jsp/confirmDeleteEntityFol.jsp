<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlaceSubForm}DeleteDlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg("Delete")} ${srvI18n.getMsg(param.nmEnt)}
      <button onclick="closeDlgCareful('${namePlaceSubForm}Delete')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceSubForm}DeleteFrm" action="service/" method="POST">
      <input type="hidden" name="nmsAct" value="entityFolDelete,entityEdit,list">
      <input type="hidden" name="nmHnd" value="${param.nmHnd}">
      <input type="hidden" name="nmEnt" value="${entity.getClass().simpleName}">
      <input type="hidden" name="nmRndList" value="${param.nmRndList}">
      <input type="hidden" name="page" value="${param.page}">
      <c:if test="${not empty param.mobile}">
        <input type="hidden" name="mobile" value="${param.mobile}">
      </c:if>
      <input type="hidden" name="msgSuccess" value="deleted_ok">
      <input type="hidden" name="nmRnd" value="editEntityFolSavedJson">
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM') || par.key.startsWith('fly')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
        <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass(), 'orderShowForm')}" varStatus="vs">
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
