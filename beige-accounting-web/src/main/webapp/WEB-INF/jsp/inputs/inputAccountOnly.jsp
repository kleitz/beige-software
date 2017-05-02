<%@ page language="java" pageEncoding="UTF-8" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${entity[fieldName].itsName}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${fieldName}Id" type="hidden" name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsId}">
      <button type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].foreignEntity}','${entity.getClass().simpleName}', '${fieldName}', '&nmHnd=${param.nmHnd}&fltordPisUsedVal=true&fltordPforcedFor=isUsed&mobile=${param.mobile}');">...</button>
      <button type="button" class="btn" onclick="clearSelectedEntity('${entity.getClass().simpleName}${fieldName}');">X</button>
    </div>
  </td>
</tr>
