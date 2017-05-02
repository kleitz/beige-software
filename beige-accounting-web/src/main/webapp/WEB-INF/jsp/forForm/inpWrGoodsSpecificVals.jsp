<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${entity.specifics.itsType eq 'TEXT'}">
  <jsp:include page="../${param.mobile}inputs/inpGoodsSpecStringVal.jsp"/>
</c:if>
<c:if test="${entity.specifics.itsType eq 'BIGDECIMAL'}">
  <jsp:include page="../${param.mobile}inputs/inpGoodsSpecNumericVal.jsp"/>
</c:if>
<c:if test="${entity.specifics.itsType eq 'INTEGER'}">
  <jsp:include page="../${param.mobile}inputs/inpGoodsSpecIntegerVal.jsp"/>
</c:if>
<c:if test="${entity.specifics.itsType eq 'CHOOSEABLE_SPECIFICS'}">
  <jsp:include page="../${param.mobile}inputs/inpGoodsSpecChooseable.jsp"/>
</c:if>
<c:set var="isFileAction" value="${entity.specifics.itsType eq 'FILE' || entity.specifics.itsType eq 'IMAGE' || entity.specifics.itsType eq 'IMAGE_IN_SET' ||  entity.specifics.itsType eq 'FILE_EMBEDDED'}"/>
<c:if test="${empty entity.stringValue1 && empty entity.stringValue2  && isFileAction}">
  <jsp:include page="../${param.mobile}inputs/inpGoodsSpecFileAdd.jsp"/>
</c:if>
<c:if test="${not empty entity.stringValue1 && empty entity.stringValue2 && isFileAction}">
  <jsp:include page="../${param.mobile}inputs/inpGoodsSpecFileEdit.jsp"/>
</c:if>
