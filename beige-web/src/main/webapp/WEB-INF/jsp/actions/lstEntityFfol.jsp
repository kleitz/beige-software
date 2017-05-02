<%@ page language="java" pageEncoding="UTF-8" %>
<button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=insertDeleteEntityFfolJson&nmsAct=entityEdit&nmEnt=${entitySimpleName}&${entitySimpleName}.itsId=${entity.itsId}${flyParams}');">${srvI18n.getMsg("Edit")}</button>
<button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=insertDeleteEntityFfolJson&nmsAct=entityConfirmDelete&nmEnt=${entitySimpleName}&${entitySimpleName}.itsId=${entity.itsId}${flyParams}');">${srvI18n.getMsg("Delete")}</button>
