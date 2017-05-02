<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="form-actions">
  <button type="button" onclick="submitFormByAjax('${namePlaceForm}DeleteFrm', false);">${srvI18n.getMsg("Delete")}</button>
  <a href="#" onclick="closeDlgCareful('${namePlaceForm}Delete');">${srvI18n.getMsg("Close")}</a>
</div>
