<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isFileAction" value="${entity.specifics.itsType eq 'FILE' || entity.specifics.itsType eq 'IMAGE' || entity.specifics.itsType eq 'IMAGE_IN_SET' ||  entity.specifics.itsType eq 'FILE_EMBEDDED'}"/>
<div class="form-actions">
  <input style="display: none" id="${namePlaceForm}EditFrmFakeSubmit" type="submit"/>
  <c:if test="${empty entity.stringValue1 && empty entity.stringValue2  && isFileAction}">
    <button type="button" onclick="submitGoodsSpecificByAjax('${namePlaceForm}EditFrm');">${srvI18n.getMsg("Save")}</button>
  </c:if>
  <c:if test="${!(empty entity.stringValue1 && empty entity.stringValue2  && isFileAction)}">
    <button type="button" onclick="submitFormByAjax('${namePlaceForm}EditFrm', true, '&nmRnd=editEntitySavedJson');">${srvI18n.getMsg("Save")}</button>
  </c:if>
  <a href="#" onclick="closeDlgCareful('${namePlaceForm}Edit');">${srvI18n.getMsg("Close")}</a>
</div>
