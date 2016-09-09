<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="actTitle" value="${srvI18n.getMsg('Reverse')}"/>
<c:set var="wdgName" value="wdgReverseForm" scope="request"/>
<dialog id="${namePlaceForm}EditDlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg(actTitle)} ${srvI18n.getMsg(param.nameEntity)}
      <button onclick="closeDlgCareful('${namePlaceForm}Edit')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceForm}EditFrm" action="entity/" method="POST">
      <input type="hidden" name="nameAction" value="save">
      <input type="hidden" name="nameEntity" value="${param.nameEntity}">
      <input type="hidden" name="page" value="${param.page}">
      <input type="hidden" name="mobile" value="${param.mobile}">
      <c:set var="flyParams" value=""/>
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
          <c:set var="flyParams" value="${flyParams}&${par.key}=${par.value}"/>
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
      <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass().canonicalName, 'orderShowForm')}">
        <c:if test="${not empty entry.value.get(wdgName)}">
          <c:set var="fieldName" value="${entry.key}" scope="request"/>
          <c:set var="fieldSettings" value="${entry.value}" scope="request"/>
          <jsp:include page="forForm/${fieldSettings.get('wdgInputWrapper')}.jsp"/>
        </c:if>
      </c:forEach>
      </table>
      <jsp:include page="forForm/${mngUvds.classesSettings.get(entity.getClass().canonicalName).get('wdgFormActions')}reverse.jsp"/>
    </form>
  </div>
</dialog>
