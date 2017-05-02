<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.${fieldName}">${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <select name="${entity.getClass().simpleName}.${fieldName}" onchange="inputHasBeenChanged(this);">
        <c:set var="sel1" value=""/>
        <c:if test="${entity[fieldName] == 1}"> <c:set var="sel1" value="selected"/> </c:if>
        <option value="1" ${sel1}>1</option>
        <c:set var="sel1" value=""/>
        <c:if test="${entity[fieldName] == 2}"> <c:set var="sel2" value="selected"/> </c:if>
        <option value="2" ${sel2}>2</option>
      </select>
      <c:set var="autofocus" value="" scope="request"/>
    </div>
  </td>
</tr>
