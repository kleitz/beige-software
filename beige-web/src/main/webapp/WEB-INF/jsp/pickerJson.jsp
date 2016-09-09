<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fltOrdPrefix" value="fltordP" scope="request"/>
<c:set var="namePlace" value="pickersPlace" scope="request"/>
<c:set var="nameRendererList" value="pickerJson" scope="request"/>
<c:set var="prefixFilterOrderForm" value="${namePlace}${param.nameEntity}" scope="request"/>
<c:import url="/WEB-INF/jsp/picker.jsp" varReader="rdEntities" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlace}${param.nameEntity}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(param.javascript)}"}]
}
</c:import>
