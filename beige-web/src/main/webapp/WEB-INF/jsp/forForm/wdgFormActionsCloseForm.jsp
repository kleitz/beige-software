<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="form-actions">
  <input style="display: none" id="${namePlaceForm}EditFrmFakeSubmit" type="submit"/>
  <button type="button" onclick="submitFormByAjax('${namePlaceForm}EditFrm', true, '&nmRnd=listAfterFormActionJson');">${srvI18n.getMsg("Save")}</button>
  <a href="#" onclick="closeDlgCareful('${namePlaceForm}Edit');">${srvI18n.getMsg("Close")}</a>
</div>
