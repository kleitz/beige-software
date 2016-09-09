<%@ page language="java" pageEncoding="UTF-8" %>
<button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editEntityFromOwnedListJson&nameAction=copyFromOwnedList&nameEntityFromOwnedList=${entitySimpleName}&idEntityFromOwnedList=${entity.itsId}${itsOwnerParams}');">${srvI18n.getMsg("Copy")}</button>
