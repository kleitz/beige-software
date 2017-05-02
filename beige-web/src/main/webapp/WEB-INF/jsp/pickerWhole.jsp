<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlace}${param.nmEnt}Dlg" class="dlg">
  <div class="form-std">
    <div class="dialog-title">
      <c:set var="nameEnts" value="${param.nmEnt}s"/>
      ${srvI18n.getMsg(nameEnts)} ${srvI18n.getMsg("picker")}
      <button onclick="closeDlg('${namePlace}${param.nmEnt}Dlg')" class="btn-close">x</button>
    </div>
    <div id="${namePlace}${param.nmEnt}list">
      <jsp:include page="picker.jsp"/>
    </div>
  </div>
</dialog>
<c:if test="${not empty mngUvds.classesSettings.get(classEntity).get('wdgFilterOrder')}">
  <jsp:include page="filterOrder/${mngUvds.classesSettings.get(classEntity).get('wdgFilterOrder')}.jsp"/>
</c:if>
