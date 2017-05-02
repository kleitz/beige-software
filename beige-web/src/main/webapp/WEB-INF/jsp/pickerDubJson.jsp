<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fltOrdPrefix" value="fltordPD" scope="request"/>
<c:set var="namePlace" value="pickersPlaceDub" scope="request"/>
<c:set var="nmRndList" value="pickerDubJson" scope="request"/>
<c:import url="/WEB-INF/jsp/picker.jsp" varReader="rdEntities" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlace}${param.nmEnt}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(javascript)}"}]
}
</c:import>
