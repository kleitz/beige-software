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
      <c:set var="selectedDisabled" value=""/>
      <c:if test="${empty entity[fieldName]}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
      <select ${required} ${autofocus} name="${entity.getClass().simpleName}.${fieldName}" onchange="inputHasBeenChanged(this);">
        <option value="" ${selectedDisabled}>-</option>
        <c:forEach var="enm" items="${mngUvds.fieldsSettings[entity.getClass()].get(fieldName).get('enumValues').split(',')}">
          <c:if test="${!entity[fieldName].toString().equals(enm)}"> <c:set var="selectedDisabled" value=""/> </c:if>
          <c:if test="${entity[fieldName].toString().equals(enm)}"> <c:set var="selectedDisabled" value="selected"/> </c:if>
          <option value="${enm}" ${selectedDisabled}>${srvI18n.getMsg(enm)}</option>
        </c:forEach>
      </select>
      <c:set var="autofocus" value="" scope="request"/>
    </div>
  </td>
</tr>
