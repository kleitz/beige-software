<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity[fieldName]}">
  <c:set var="docLineAppr" value="# ${entity[fieldName].itsId}, ${entity[fieldName].invItem.itsName}, ${srvI18n.getMsg('itsCost')}=${entity[fieldName].itsCost}, ${srvI18n.getMsg('rest_was')}=${entity[fieldName].theRest}"/>
  <c:if test="${empty entity[fieldName].idBirth}">
    <c:set var="docLineAppr" value="# ${entity[fieldName].idDatabaseBirth}-${entity[fieldName].itsId}, Item #${entity[fieldName].invItem.itsId}, ${srvI18n.getMsg('itsCost')}=${entity[fieldName].itsCost}, ${srvI18n.getMsg('theRest')}=${entity[fieldName].theRest}"/>
  </c:if>
  <c:if test="${not empty entity[fieldName].idBirth}">
    <c:set var="docLineAppr" value="# ${entity[fieldName].idDatabaseBirth}-${entity[fieldName].idBirth}, Item #${entity[fieldName].invItem.itsId}, ${srvI18n.getMsg('itsCost')}=${entity[fieldName].itsCost}, ${srvI18n.getMsg('theRest')}=${entity[fieldName].theRest}"/>
  </c:if>
</c:if>
<tr>
  <td>
    <label>${srvI18n.getMsg(fieldName)}</label>
  </td>
  <td>
    <div class="input-line">
      <input class="picked-appearence" disabled type="text" value="${docLineAppr}">
      <input type="hidden" required name="${entity.getClass().simpleName}.${fieldName}" value="${entity[fieldName].itsId}">
    </div>
  </td>
</tr>
