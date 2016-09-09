<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="namePlaceForm" value="frmMainPlace" scope="request"/>
{"multiTargetResponse":
  [{"nameTarget": "${namePlaceForm}", "content": "",
    "nameTargetParent": null, "javascript": null},
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="showSuccess" value="showSuccess(MSGS['deleted_successful']);"/>
<c:set var="nameRendererList" value="listJson" scope="request"/>
<c:import url="/WEB-INF/jsp/list.jsp" varReader="rdEntities" charEncoding="UTF-8">
   {"nameTarget": "${namePlace}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(showSuccess)}"}]
}
</c:import>    
