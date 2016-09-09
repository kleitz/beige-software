<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${prefixFilterOrderForm}FltOrdDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      <c:set var="nameEnts" value="${param.nameEntity}s"/>
      ${srvI18n.getMsg(nameEnts)} ${srvI18n.getMsg("filterOrder")}
      <button onclick="closeDlgCareful('${prefixFilterOrderForm}FltOrd')" class="btn-close">x</button>
    </div>
    <form class="filter" id="${prefixFilterOrderForm}FltOrdFrm" action="entityList/" method="POST">
      <input type="hidden" name="nameRenderer" value="${nameRendererList}">
      <input type="hidden" name="nameEntity" value="${param.nameEntity}">
      <input type="hidden" name="page" value="${param.page}">
      <input type="hidden" name="javascript" value="clearChangesAndCloseDialog('${prefixFilterOrderForm}FltOrd')">
      <input type="hidden" name="mobile" value="${param.mobile}">
      <c:forEach var="entry" items="${fieldsForList}" varStatus="vs">
        <c:if test="${not empty entry.value.get('wdgFilter')}">
          <c:set var="fieldName" value="${entry.key}" scope="request"/>
          <jsp:include page="../${param.mobile}filters/${entry.value.get('wdgFilter')}.jsp"/>
        </c:if>
      </c:forEach>
      <div class="input-line">
        ${srvI18n.getMsg("OrderBy")}
        <c:set var="ordByName" value="${fltOrdPrefix}orderBy" scope="request"/>
        <c:set var="ordByDescName" value="${fltOrdPrefix}orderByDesc"/>
        <c:if test="${orderMap[ordByName] != 'disabled'}"> <c:set var="checkedOrNot" value=""/> </c:if>
        <c:if test="${orderMap[ordByName] == 'disabled'}"> <c:set var="checkedOrNot" value="checked"/> </c:if>
        <label>
          <input type="radio" name="${ordByName}" value="disabled" ${checkedOrNot} onchange="inputHasBeenChanged(this);">
          ${srvI18n.getMsg("disabled")}
        </label>
        <c:forEach var="entry" items="${fieldsForList}" varStatus="vs">
          <c:if test="${not empty entry.value.get('wdgOrder')}">
            <c:set var="fieldName" value="${entry.key}" scope="request"/>
            <jsp:include page="../order/${entry.value.get('wdgOrder')}.jsp"/>
          </c:if>
        </c:forEach>
        <c:if test="${orderMap[ordByDescName] != 'on'}"> <c:set var="checkedOrNot" value=""/> </c:if>
        <c:if test="${orderMap[ordByDescName] == 'on'}"> <c:set var="checkedOrNot" value="checked"/> </c:if>
        <label>
          <input type="checkbox" name="${ordByDescName}" value="${orderMap[ordByDescName]}" ${checkedOrNot} onchange="inputHasBeenChanged(this);">
          ${srvI18n.getMsg("orderByDesc")}
        </label>
      </div>
      <div class="form-actions">
        <input style="display: none" id="${prefixFilterOrderForm}FltOrdFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormByAjax('${prefixFilterOrderForm}FltOrdFrm');">${srvI18n.getMsg("Apply")}</button>
        <a href="#" onclick="closeDlgCareful('${prefixFilterOrderForm}FltOrd');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
