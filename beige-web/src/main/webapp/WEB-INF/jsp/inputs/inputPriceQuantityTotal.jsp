<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsPrice">${srvI18n.getMsg("itsPrice")}</label>
  </td>
  <td>
    <div class="input-line">
      <input type="number" ${autofocus} step="0.01" required id="${entity.getClass().simpleName}itsPrice" name="${entity.getClass().simpleName}.itsPrice" value="${entity['itsPrice']}" onchange="inputHasBeenChanged(this); calculateTotalForPrice('${entity.getClass().simpleName}', 2, 2);"/> 
      <c:set var="autofocus" value="" scope="request"/>
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsQuantity">${srvI18n.getMsg("itsQuantity")}</label>
  </td>
  <td>
    <div class="input-line">
      <input type="number" required id="${entity.getClass().simpleName}itsQuantity" name="${entity.getClass().simpleName}.itsQuantity" value="${entity['itsQuantity']}" onchange="inputHasBeenChanged(this); calculateTotalForPrice('${entity.getClass().simpleName}', 2, 2);"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsTotal">${srvI18n.getMsg("itsTotal")}</label>
  </td>
  <td>
    <div class="input-line">
      <input type="number" step="0.01" id="${entity.getClass().simpleName}itsTotal" name="${entity.getClass().simpleName}.itsTotal" value="${entity['itsTotal']}" onchange="inputHasBeenChanged(this); calculatePrice('${entity.getClass().simpleName}', 2, 2);"/> 
    </div>
  </td>
</tr>
