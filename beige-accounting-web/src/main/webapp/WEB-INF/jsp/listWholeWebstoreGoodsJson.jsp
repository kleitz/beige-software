<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="nmRndList" value="listWebstoreGoodsJson" scope="request"/>
<c:set var="prefixFilterOrderForm" value="${namePlace}" scope="request"/>
<c:import url="/WEB-INF/jsp/listWholeWebstoreGoods.jsp" varReader="rdEntities" charEncoding="UTF-8">
{"multiTargetResponse":
  [{"nameTarget": "${namePlace}", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": ${utlJsp.nullOrJsonStr(param.javascript)}}]
}
</c:import>
