<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="namePlaceSubForm" value="frmSubPlace" scope="request"/>
<c:set var="javascript" value="openDlg('${namePlaceSubForm}DeleteDlg');"/>
<c:import url="/WEB-INF/jsp/confirmDeleteEntityFromOwnedList.jsp" varReader="rdEditEnty" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlaceSubForm}", "content": "${utlJsp.toJsonStringAndClose(rdEditEnty)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(javascript)}"}]
}
</c:import>
