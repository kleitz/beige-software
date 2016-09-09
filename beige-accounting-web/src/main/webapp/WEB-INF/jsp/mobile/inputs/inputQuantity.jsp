<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${accSettings.quantityPrecision == 0}">
  <c:set var="stepq" value="1"/>
</c:if>
<c:if test="${accSettings.quantityPrecision == 1}">
  <c:set var="stepq" value="0.1"/>
</c:if>
<c:if test="${accSettings.quantityPrecision == 2}">
  <c:set var="stepq" value="0.01"/>
</c:if>
<c:if test="${accSettings.quantityPrecision == 3}">
  <c:set var="stepq" value="0.001"/>
</c:if>
<c:if test="${accSettings.quantityPrecision == 4}">
  <c:set var="stepq" value="0.0001"/>
</c:if>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsQuantity">${srvI18n.getMsg("itsQuantity")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${stepq}" required id="${entity.getClass().simpleName}itsQuantity" name="${entity.getClass().simpleName}.itsQuantity" value="${entity['itsQuantity']}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
