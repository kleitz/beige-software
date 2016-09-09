<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}${fieldName}AppearanceVisible">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}${fieldName}AppearanceVisible" disabled="disabled" type="text" value="${entity[fieldName].itsName}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}${fieldName}Id" required type="hidden" name="${entity.getClass().simpleName}.${fieldName}.itsId" value="${entity[fieldName].itsId}">
      <button type="button" class="btn" onclick="openEntityPicker('${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].foreignEntity}','${entity.getClass().simpleName}', '${fieldName}', '&fltordPitsTypeOpr=eq&fltordPitsTypeValId=4&fltordPitsTypeValAppearance=Finished%20product&fltordPforcedFor=itsType&mobile=${param.mobile}');">...</button>
      <button type="button" class="btn" onclick="clearSelectedEntity('${entity.getClass().simpleName}${fieldName}');">X</button>
    </div>
  </td>
</tr>
