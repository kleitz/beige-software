<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="SubaccountLinesubaccNameAppearanceVisible">${srvI18n.getMsg('subaccount')}</label>
  </td>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="SubaccountLinesubaccNameAppearanceVisible" disabled type="text" value="${entity.subaccName}" onchange="inputHasBeenChanged(this);">
      <input id="SubaccountLinesubaccNameAppearance" type="hidden" required name="SubaccountLine.subaccName" value="${entity.subaccName}">
      <input id="SubaccountLinesubaccType" type="hidden" name="SubaccountLine.subaccType" value="${entity.subaccType}">
      <input id="SubaccountLinesubaccId" type="hidden" required name="SubaccountLine.subaccId" value="${entity.subaccId}">
      <button id="SubaccountLinesubaccNameChoose" type="button" class="btn" onclick="openEntityPicker('${typeCodeSubaccMap.get(entity.subaccType).simpleName}', 'SubaccountLine', 'subaccName', '&nmHnd=${param.nmHnd}&wdgPick=pickAccSubacc&mobile=${param.mobile}');">...</button>
      <button id="SubaccountLinesubaccNameClear" type="button" class="btn" onclick="clearSubaccLine('SubaccountLine');">X</button>
    </div>
  </td>
</tr>
