<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pages">
  <c:set var="ownerFieldName" value="${srvOrm.tablesMap[entitySimpleName].ownerFieldName}"/>
  <button onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=insertDeleteEntityFfolJson&nmsAct=entityCreate&nmEnt=${entitySimpleName}&${entitySimpleName}.${ownerFieldName}=${ownerIdStr}${flyParams}');" class="btn btn-sm">
    ${srvI18n.getMsg("New")}
  </button>
</div>
