<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="form-actions">
  <input style="display: none" id="${namePlaceForm}EditFrmFakeSubmit" type="submit"/>
  <button type="button" onclick="submitFormByAjax('${namePlaceForm}EditFrm', true, '&nmRnd=editEntitySavedJson');">${srvI18n.getMsg("Save")}</button>
  <c:if test="${entity.itsTotal.doubleValue() gt 0}">
    <button type="button" onclick="submitFormByAjaxConfirm('${namePlaceForm}EditFrm', false, '&nmRnd=listAfterAccountJson&actionAdd=makeAccEntries');">${srvI18n.getMsg("MakeAccEntries")}</button>
  </c:if>
  <a href="#" onclick="closeDlgCareful('${namePlaceForm}Edit');">${srvI18n.getMsg("Close")}</a>
</div>
