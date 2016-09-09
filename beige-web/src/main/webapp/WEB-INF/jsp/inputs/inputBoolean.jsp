<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.${fieldName}">${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <c:set var="required" value=""/>
      <c:if test="${srvOrm.tablesMap[entity.getClass().simpleName].fieldsMap[fieldName].definition.contains('not null')}">
        <c:set var="required" value="required"/>
      </c:if>
      <select ${required} ${autofocus} name="${entity.getClass().simpleName}.${fieldName}" onchange="inputHasBeenChanged(this);">
        <c:set var="selectedDisabled" value=""/>
        <c:if test="${entity[fieldName]}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
        <option value="true" ${selectedDisabled}>${srvI18n.getMsg("Yes")}</option>
        <c:set var="selectedDisabled" value=""/>
        <c:if test="${!entity[fieldName]}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
        <option value="false" ${selectedDisabled}>${srvI18n.getMsg("No")}</option>
      </select>
      <c:set var="autofocus" value="" scope="request"/>
    </div>
  </td>
</tr>
