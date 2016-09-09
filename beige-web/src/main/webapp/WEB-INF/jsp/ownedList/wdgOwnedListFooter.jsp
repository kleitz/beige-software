<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pages">
  <button onclick="getHtmlByAjaxCareful('GET', 'entity/?nameRenderer=editEntityFromOwnedListJson&nameAction=createFromOwnedListTransactional&nameEntityFromOwnedList=${entitySimpleName}${itsOwnerParams}');" class="btn btn-sm">
    ${srvI18n.getMsg("New")}
  </button>
</div>
