<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="actTitle" value="${srvI18n.getMsg('Reverse')}"/>
<c:set var="wdgName" value="wdgReverseForm" scope="request"/>
<dialog id="${namePlaceForm}EditDlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg(actTitle)} ${srvI18n.getMsg(entity.getClass().simpleName)}
      <button onclick="closeDlgCareful('${namePlaceForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceForm}EditFrm" action="service/" method="POST" enctype="multipart/form-data">
      <input type="hidden" name="nmsAct" value="entitySave,list">
      <input type="hidden" name="nmHnd" value="${param.nmHnd}">
      <input type="hidden" name="nmEnt" value="${entity.getClass().simpleName}">
      <input type="hidden" name="nmRndList" value="${param.nmRndList}">
      <input type="hidden" name="msgSuccess" value="reverse_ok">
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
      <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass(), 'orderShowForm')}">
        <c:if test="${not empty entry.value.get(wdgName)}">
          <c:set var="fieldName" value="${entry.key}" scope="request"/>
          <c:set var="fieldSettings" value="${entry.value}" scope="request"/>
          <jsp:include page="forForm/${fieldSettings.get('wdgInputWrapper')}.jsp"/>
        </c:if>
      </c:forEach>
      </table>
      <jsp:include page="forForm/${mngUvds.classesSettings.get(entity.getClass()).get('wdgFormActions')}reverse.jsp"/>
    </form>
  </div>
</dialog>
