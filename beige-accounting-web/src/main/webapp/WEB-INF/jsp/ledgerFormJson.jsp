<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="javascript" value="openDlg('frmReportEditDlg');"/>
<c:import url="/WEB-INF/jsp/ledgerForm.jsp" varReader="rdForm" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "frmReport", "content": "${utlJsp.toJsonStringAndClose(rdForm)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(javascript)}"}]
}
</c:import>
