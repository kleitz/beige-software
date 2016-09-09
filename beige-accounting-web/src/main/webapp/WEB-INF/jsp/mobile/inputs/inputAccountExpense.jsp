<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="subaccName" value="sub${fieldName}"/>
<c:set var="subaccTypeName" value="sub${fieldName}Type"/>
<c:set var="subaccIdName" value="sub${fieldName}Id"/>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${entity[fieldName].itsName}" onchange="inputHasBeenChanged(this); clearSubacc('${entity.getClass().simpleName}', '${fieldName}', '${subaccName}');">
      <input id="${entity.getClass().simpleName}${fieldName}Id" type="hidden" name="${entity.getClass().simpleName}.${fieldName}.itsId" value="${entity[fieldName].itsId}">
      <button type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].foreignEntity}','${entity.getClass().simpleName}', '${fieldName}', '&fltordPsubaccTypeOpr1=eq&fltordPsubaccTypeVal1=2009&fltordPisUsedVal=true&fltordPforcedFor=isUsed%2CsubaccType&mobile=${param.mobile}');">...</button>
      <button type="button" class="btn" onclick="clearSelectedEntity('${entity.getClass().simpleName}${fieldName}');">X</button>
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${subaccName}AppearanceVisible">${srvI18n.getMsg('subaccount')}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}${subaccName}AppearanceVisible" disabled type="text" value="${entity[subaccName]}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${subaccName}Appearance" type="hidden"  required name="${entity.getClass().simpleName}.${subaccName}" value="${entity[subaccName]}">
      <input id="${entity.getClass().simpleName}${subaccIdName}" type="hidden" required name="${entity.getClass().simpleName}.${subaccIdName}" value="${entity[subaccIdName]}">
      <input id="${entity.getClass().simpleName}${subaccTypeName}" type="hidden" name="${entity.getClass().simpleName}.${subaccTypeName}" value="${entity[subaccTypeName]}">
      <button id="${entity.getClass().simpleName}${subaccName}Choose" type="button" ${disabled} class="btn" onclick="openPickerSubacc('${entity.getClass().simpleName}', '${fieldName}', '${subaccName}', '${param.mobile}');">...</button>
      <button id="${entity.getClass().simpleName}${subaccName}Clear" type="button" ${disabled} class="btn" onclick="clearSubacc('${entity.getClass().simpleName}', '${fieldName}', '${subaccName}');">X</button>
    </div>
  </td>
</tr>
