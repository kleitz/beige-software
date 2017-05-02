<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="namePlaceForm" value="frmMainPlace" scope="request"/>
{"multiTargetResponse":
  [{"nameTarget": "${namePlaceForm}", "content": "",
    "nameTargetParent": null, "javascript": null},
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="showSuccess" value="showSuccess('${srvI18n.getMsg(param.msgSuccess)}');"/>
<jsp:include page="${param.nmRndList}Part.jsp"/>
]}
