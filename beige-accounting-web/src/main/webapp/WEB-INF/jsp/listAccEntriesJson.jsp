<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="nmRndList" value="listAccEntriesJson" scope="request"/>
<c:import url="/WEB-INF/jsp/listAccEntries.jsp" varReader="rdEntities" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlace}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": ${utlJsp.nullOrJsonStr(param.javascript)}}]
}
</c:import>
