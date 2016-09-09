<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.pricePrecision == 0}">
  <c:set var="step" value="1"/>
</c:if>
<c:if test="${accSettings.pricePrecision == 1}">
  <c:set var="step" value="0.1"/>
</c:if>
<c:if test="${accSettings.pricePrecision == 2}">
  <c:set var="step" value="0.01"/>
</c:if>
<c:if test="${accSettings.pricePrecision == 3}">
  <c:set var="step" value="0.001"/>
</c:if>
<c:if test="${accSettings.pricePrecision == 4}">
  <c:set var="step" value="0.0001"/>
</c:if>
<c:set var="inputType" value="number" scope="request"/>
<c:set var="inputAdd" value="step='${step}'" scope="request"/>
<jsp:include page="inputSimpleTmpl.jsp"/>
