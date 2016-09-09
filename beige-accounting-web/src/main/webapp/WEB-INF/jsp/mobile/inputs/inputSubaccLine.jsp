<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}subaccNameAppearanceVisible">${srvI18n.getMsg('subaccount')}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}subaccNameAppearanceVisible" disabled type="text" value="${entity.subaccName}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}subaccNameAppearance" type="hidden" required name="${entity.getClass().simpleName}.subaccName" value="${entity.subaccName}">
      <input id="${entity.getClass().simpleName}subaccType" type="hidden" name="${entity.getClass().simpleName}.subaccType" value="${entity.subaccType}">
      <input id="${entity.getClass().simpleName}subaccId" type="hidden" required name="${entity.getClass().simpleName}.subaccId" value="${entity.subaccId}">
      <button id="${entity.getClass().simpleName}subaccNameChoose" type="button" class="btn" onclick="openEntityPicker('${typeCodeSubaccMap.get(entity.subaccType).simpleName}', '${entity.getClass().simpleName}', 'subaccName', '&wdgPick=pickAccSubacc&mobile=${param.mobile}');">...</button>
      <button id="${entity.getClass().simpleName}subaccNameClear" type="button" class="btn" onclick="clearSubaccLine('${entity.getClass().simpleName}');">X</button>
    </div>
  </td>
</tr>
