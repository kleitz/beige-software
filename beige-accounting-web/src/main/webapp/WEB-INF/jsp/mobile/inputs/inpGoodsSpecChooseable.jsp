<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.longValue2">${srvI18n.getMsg('itsType')}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}chooseableSpecTypeAppearanceVisible" disabled="disabled" type="text" value="${entity.stringValue2}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}chooseableSpecTypeId" type="hidden" name="${entity.getClass().simpleName}.longValue2" value="${entity.longValue2}">
      <input id="${entity.getClass().simpleName}chooseableSpecTypeAppearance" type="hidden" name="${entity.getClass().simpleName}.stringValue2" value="${entity.stringValue2}">
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.longValue2">${srvI18n.getMsg('itsValue')}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input class="picked-appearence" id="${entity.getClass().simpleName}chooseableSpecAppearanceVisible" disabled="disabled" type="text" value="${entity.stringValue1}" onchange="inputHasBeenChanged(this);">
      <input id="${entity.getClass().simpleName}chooseableSpecId" required type="hidden" name="${entity.getClass().simpleName}.longValue1" value="${entity.longValue1}">
      <input id="${entity.getClass().simpleName}chooseableSpecAppearance" type="hidden" name="${entity.getClass().simpleName}.stringValue1" value="${entity.stringValue1}">
      <c:set var="filterType" value=""/>
      <c:if test="${not empty entity[longValue2]}">
        <c:set var="filterType" value="&fltordPitsTypeOpr=eq&fltordPitsTypeValId=${longValue2}&fltordPitsTypeValAppearance=${stringValue2}"/>
      </c:if>
      <button type="button" class="btn" onclick="openEntityPicker('ChooseableSpecifics','${entity.getClass().simpleName}', 'chooseableSpec','&nmHnd=${param.nmHnd}${filterType}');">...</button>
      <button type="button" class="btn" onclick="clearSelectedEntity('${entity.getClass().simpleName}chooseableSpec');">X</button>
    </div>
  </td>
</tr>
