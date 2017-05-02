<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="namePlaceForm" value="frmMainPlace" scope="request"/>
{"multiTargetResponse":
  [{"nameTarget": "${namePlaceForm}", "content": "",
    "nameTargetParent": null, "javascript": null},
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="nmRndList" value="listJson" scope="request"/>
<c:set var="prefixFilterOrderForm" value="${namePlace}" scope="request"/>
<c:import url="/WEB-INF/jsp/listAccEntries.jsp" varReader="rdEntities" charEncoding="UTF-8">
  {"nameTarget": "${namePlace}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(param.javascript)}"}]
}
</c:import>
