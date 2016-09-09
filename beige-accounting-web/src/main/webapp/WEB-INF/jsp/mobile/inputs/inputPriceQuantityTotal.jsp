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
</c:if><tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsPrice">${srvI18n.getMsg("itsPrice")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" required id="${entity.getClass().simpleName}itsPrice" name="${entity.getClass().simpleName}.itsPrice" value="${entity['itsPrice']}" onchange="inputHasBeenChanged(this); calculateTotalForPrice('${entity.getClass().simpleName}', ${accSettings.pricePrecision}, ${accSettings.pricePrecision});"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsQuantity">${srvI18n.getMsg("itsQuantity")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${stepq}" required id="${entity.getClass().simpleName}itsQuantity" name="${entity.getClass().simpleName}.itsQuantity" value="${entity['itsQuantity']}" onchange="inputHasBeenChanged(this); calculateTotalForPrice('${entity.getClass().simpleName}', ${accSettings.pricePrecision}, ${accSettings.pricePrecision});"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsTotal">
      ${srvI18n.getMsg("itsTotal")}
      <c:if test="${accSettings.isExtractSalesTaxFromSales}">
        ${srvI18n.getMsg('without_taxes')}
      </c:if>
    </label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" step="${step}" id="${entity.getClass().simpleName}itsTotal" name="${entity.getClass().simpleName}.itsTotal" value="${entity['itsTotal']}" onchange="inputHasBeenChanged(this); calculatePrice('${entity.getClass().simpleName}', ${accSettings.pricePrecision}, ${accSettings.pricePrecision});"/> 
    </div>
  </td>
</tr>
