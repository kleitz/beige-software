<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlaceForm}DeleteDlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg("Delete")} ${srvI18n.getMsg(param.nmEnt)}
      <button onclick="closeDlgCareful('${namePlaceForm}Delete')" class="btn-close">x</button>
    </div>
    <form id="${namePlaceForm}DeleteFrm" action="service/" method="POST" enctype="multipart/form-data">
      <input type="hidden" name="nmsAct" value="entityDelete,list">
      <input type="hidden" name="nmHnd" value="${param.nmHnd}">
      <input type="hidden" name="nmEnt" value="${param.nmEnt}">
      <input type="hidden" name="msgSuccess" value="deleted_ok">
      <input type="hidden" name="nmRndList" value="${param.nmRndList}">
      <input type="hidden" name="nmRnd" value="listAfterFormActionJson">
      <input type="hidden" name="page" value="${param.page}">
      <c:if test="${not empty param.mobile}">
        <input type="hidden" name="mobile" value="${param.mobile}">
      </c:if>
      <c:forEach items="${param}" var="par">
        <c:if test="${par.key.startsWith('fltordM') || par.key.startsWith('fly')}">
          <input type="hidden" name="${par.key}" value="${par.value}">
          <c:set var="flyParams" value="${flyParams}&${par.key}=${par.value}"/>
        </c:if>
      </c:forEach>
      <table class="tbl-fieldset">
      <c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass(), 'orderShowForm')}">
        <c:if test="${not empty entry.value.get('wdgDeleteForm')}">
          <c:set var="fieldName" value="${entry.key}" scope="request"/>
          <jsp:include page="inputs/${entry.value.get('wdgDeleteForm')}.jsp"/>
        </c:if>
      </c:forEach>
      </table>
      <c:forEach var="ownedListsMapEntry" items="${ownedListsMap}">
        <c:set var="fieldsForList" value="${mngUvds.makeFldPropLst(ownedListsMapEntry.key, 'orderShowList')}" scope="request"/>
        <c:set var="nameEnts" value="${ownedListsMapEntry.key.simpleName}s"/>
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
      <jsp:include page="forForm/${mngUvds.classesSettings.get(entity.getClass()).get('wdgFormActionsDelete')}.jsp"/>
    </form>
  </div>
</dialog>
