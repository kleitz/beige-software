<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="nmRndList" value="listJson" scope="request"/>
<c:set var="prefixFilterOrderForm" value="${namePlace}" scope="request"/>
<c:if test="${param.javascript == null}">
  <c:set var="jscrt" value="showSuccess('${srvI18n.getMsg(param.msgSuccess)}');"/>
</c:if>
<c:if test="${param.javascript != null}">
  <c:set var="jscrt" value="showSuccess('${srvI18n.getMsg(param.msgSuccess)}');${param.javascript}"/>
</c:if>
<c:import url="/WEB-INF/jsp/listWhole.jsp" varReader="rdEntities" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlace}", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": "${utlJsp.toJsonString(jscrt)}"}]
}
</c:import>
