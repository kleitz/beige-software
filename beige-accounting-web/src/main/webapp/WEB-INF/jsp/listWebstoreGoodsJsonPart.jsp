<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="fltOrdPrefix" value="fltordM" scope="request"/>
<c:set var="namePlace" value="lstMainPlace" scope="request"/>
<c:set var="nmRndList" value="listWebstoreGoodsJson" scope="request"/>
<c:if test="${not empty param.javascript}">
  <c:set var="javascriptAll" value="${param.javascript}"/>
</c:if>
<c:if test="${not empty showSuccess}">
  <c:set var="javascriptAll" value="${javascriptAll}${showSuccess}"/>
</c:if>
<c:set var="wdgListFooter" value="wdgListFooterWebstoreGoods" scope="request"/>
<c:import url="/WEB-INF/jsp/list.jsp" varReader="rdEntities" charEncoding="UTF-8">
  {"nameTarget": "${namePlace}list", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": ${utlJsp.nullOrJsonStr(javascriptAll)}},
</c:import>
<c:import url="/WEB-INF/jsp/assignGoodsToCatalogForm.jsp" varReader="rdEntities" charEncoding="UTF-8">
  {"nameTarget": "${namePlace}assignGoodCat", "content": "${utlJsp.toJsonStringAndClose(rdEntities)}",
    "nameTargetParent": null, "javascript": null}
</c:import>
