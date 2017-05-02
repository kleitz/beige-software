<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="namePlaceForm" value="frmMainPlace" scope="request"/>
<c:set var="javascript" value="openDlg('${namePlaceForm}EditDlg');"/>
<c:import url="/WEB-INF/jsp/editEntity.jsp" varReader="rdEditEnty" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlaceForm}", "content": "${utlJsp.toJsonStringAndClose(rdEditEnty)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(javascript)}"},
</c:import>
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="showSuccess" value="showSuccess('${srvI18n.getMsg(param.msgSuccess)}');"/>
<c:set var="nmRndList" value="listJson" scope="request"/>
<c:set var="namePlaceSubForm" value="frmSubPlace" scope="request"/>
<c:import url="/WEB-INF/jsp/list.jsp" varReader="rdEntities" charEncoding="UTF-8">
  {"nameTarget": "${namePlace}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(showSuccess)}"},
  {"nameTarget": "${namePlaceSubForm}", "content": "",
    "nameTargetParent": null, "javascript": ${utlJsp.nullOrJsonStr(param.javascript)}}]
}
</c:import>    
