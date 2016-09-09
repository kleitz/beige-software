<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlaceForm}DeleteDlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg("Delete")} ${srvI18n.getMsg(param.nameEntity)}
      <button onclick="closeDlgCareful('${namePlaceForm}Delete')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceForm}DeleteFrm" action="entity/" method="POST">
      <input type="hidden" name="nameAction" value="delete">
      <input type="hidden" name="nameEntity" value="${param.nameEntity}">
      <input type="hidden" name="page" value="${param.page}">
      <input type="hidden" name="nameRenderer" value="listAfterDeleteJson">
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
      <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass().canonicalName, 'orderShowForm')}">
        <c:if test="${not empty entry.value.get('wdgDeleteForm')}">
          <c:set var="fieldName" value="${entry.key}" scope="request"/>
          <jsp:include page="inputs/${entry.value.get('wdgDeleteForm')}.jsp"/>
        </c:if>
      </c:forEach>
      </table>
      <c:forEach var="ownedListsMapEntry" items="${ownedListsMap}">
        <c:set var="entitySimpleName" value="${ownedListsMapEntry.key.simpleName}" scope="request"/>
        <c:set var="entityCanonicalName" value="${ownedListsMapEntry.key.canonicalName}" scope="request"/>
        <c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(entityCanonicalName, 'orderShowList')}" scope="request"/>
        <c:set var="nameEnts" value="${entitySimpleName}s"/>
        <div class="title-list"> ${srvI18n.getMsg(nameEnts)} </div>
        <table>
          <tr>
            <c:forEach var="entryFld" items="${fieldsForList}">
              <th>${srvI18n.getMsg(entryFld.key)}</th>
            </c:forEach>
          </tr>
          <c:forEach var="entity" items="${ownedListsMapEntry.value}">
            <c:set var="entity" value="${entity}" scope="request"/>
            <tr>
              <c:forEach var="entryFld" items="${fieldsForList}">
                <c:set var="model" value="${entity[entryFld.key]}" scope="request"/>
                <td><jsp:include page="toString/${entryFld.value['wdgToString']}.jsp"/></td>
              </c:forEach>
            </tr>
          </c:forEach>
        </table>
      </c:forEach>
      <div class="form-actions">
        <input style="display: none" id="${namePlaceForm}DeleteFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${namePlaceForm}DeleteFrm', false);">${srvI18n.getMsg("Delete")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlaceForm}Delete');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
