<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.costPrecision == 0}">
  <c:set var="step" value="1"/>
</c:if>
<c:if test="${accSettings.costPrecision == 1}">
  <c:set var="step" value="0.1"/>
</c:if>
<c:if test="${accSettings.costPrecision == 2}">
  <c:set var="step" value="0.01"/>
</c:if>
<c:if test="${accSettings.costPrecision == 3}">
  <c:set var="step" value="0.001"/>
</c:if>
<c:if test="${accSettings.costPrecision == 4}">
  <c:set var="step" value="0.0001"/>
</c:if>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}debit">${srvI18n.getMsg("itsAmount")}</label>
  </td>
  <c:set var="itsAmount" value="${entity['debit']}"/>
  <c:if test="${entity['credit'] gt 0}">
    <c:set var="itsAmount" value="${entity['credit']}"/>
  </c:if>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" required name="${entity.getClass().simpleName}.debit" value="${itsAmount}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
