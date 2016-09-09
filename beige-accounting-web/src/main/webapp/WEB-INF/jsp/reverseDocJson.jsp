<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="namePlaceForm" value="frmMainPlace" scope="request"/>
<c:set var="javascript" value="openDlg('${namePlaceForm}EditDlg');"/>
<c:import url="/WEB-INF/jsp/reverseDoc.jsp" varReader="rdEditEnty" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlaceForm}", "content": "${utlJsp.toJsonStringAndClose(rdEditEnty)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(javascript)}"}]
}
</c:import>
