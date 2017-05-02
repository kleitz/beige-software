<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="erecip" class="org.beigesoft.persistable.Erecipient" />
<div class="form-actions">
  <input style="display: none" id="${namePlaceForm}EditFrmFakeSubmit" type="submit"/>
  <button type="button" onclick="submitFormByAjax('${namePlaceForm}EditFrm', true, '&nmRnd=editEntitySavedJson');">${srvI18n.getMsg("Save")}</button>
  <c:if test="${ownedListsMap.get(erecip['class']) != null && ownedListsMap.get(erecip['class']).size() != 0}">
    <button type="button" onclick="submitFormByAjax('${namePlaceForm}EditFrm', false, '&nmRnd=listAfterFormActionJson&msgSuccess=send_ok&actionAdd=send');">${srvI18n.getMsg("Send")}</button>
  </c:if>
  <a href="#" onclick="closeDlgCareful('${namePlaceForm}Edit');">${srvI18n.getMsg("Close")}</a>
</div>
