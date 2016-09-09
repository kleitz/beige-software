<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsCost">${srvI18n.getMsg("itsCost")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" ${autofocus} step="0.001" required id="${entity.getClass().simpleName}itsCost" name="${entity.getClass().simpleName}.itsCost" value="${entity['itsCost']}" onchange="inputHasBeenChanged(this); calculateTotalForCost('${entity.getClass().simpleName}', 2, 2);"/> 
      <c:set var="autofocus" value="" scope="request"/>
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
      <input type="number" required id="${entity.getClass().simpleName}itsQuantity" name="${entity.getClass().simpleName}.itsQuantity" value="${entity['itsQuantity']}" onchange="inputHasBeenChanged(this); calculateTotalForCost('${entity.getClass().simpleName}', 2, 2);"/> 
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
      <input type="number" step="0.001" id="${entity.getClass().simpleName}itsTotal" name="${entity.getClass().simpleName}.itsTotal" value="${entity['itsTotal']}" onchange="inputHasBeenChanged(this); calculateCost('${entity.getClass().simpleName}', 2, 2);"/> 
    </div>
  </td>
</tr>
