<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="javascript" value="openDlg('infoDlg');"/>
<c:import url="/WEB-INF/jsp/about.jsp" varReader="rdAbout" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "targetInfo", "content": "${utlJsp.toJsonStringAndClose(rdAbout)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(javascript)}"}]
}
</c:import>
