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
<tr>
  <td>
    <label>${srvI18n.getMsg("grossWage")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input disabled value="${entity.itsOwner.itsTotal}"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label>${srvI18n.getMsg("allowance")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" required id="${entity.getClass().simpleName}allowance" name="${entity.getClass().simpleName}.allowance" value="${entity['allowance']}" onchange="inputHasBeenChanged(this); calculateTotalTax('${entity.getClass().simpleName}', ${entity.itsOwner.itsTotal});"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label>${srvI18n.getMsg("plusAmount")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" required id="${entity.getClass().simpleName}plusAmount" name="${entity.getClass().simpleName}.plusAmount" value="${entity['plusAmount']}" onchange="inputHasBeenChanged(this); calculateTotalTax('${entity.getClass().simpleName}', ${entity.itsOwner.itsTotal});"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsPercentage">${srvI18n.getMsg("itsPercentage")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" required id="${entity.getClass().simpleName}itsPercentage" name="${entity.getClass().simpleName}.itsPercentage" value="${entity['itsPercentage']}" onchange="inputHasBeenChanged(this); calculateTotalTax('${entity.getClass().simpleName}', ${entity.itsOwner.itsTotal});"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsTotal">${srvI18n.getMsg("itsTotal")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" id="${entity.getClass().simpleName}itsTotal" name="${entity.getClass().simpleName}.itsTotal" value="${entity['itsTotal']}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
