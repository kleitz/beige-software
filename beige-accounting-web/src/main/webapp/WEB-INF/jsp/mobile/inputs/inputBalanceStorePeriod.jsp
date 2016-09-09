<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.${fieldName}">${srvI18n.getMsg(fieldName)}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <select required name="${entity.getClass().simpleName}.${fieldName}" onchange="inputHasBeenChanged(this);">
        <c:if test="${entity[fieldName].toString().equals('DAILY')}"> <c:set var="selected" value="selected"/> </c:if>
        <option value="DAILY" ${selected}>${srvI18n.getMsg('DAILY')}</option>
        <c:if test="${entity[fieldName].toString().equals('WEEKLY')}"> <c:set var="selectedWeekly" value="selected"/> </c:if>
        <option value="WEEKLY" ${selectedWeekly}>${srvI18n.getMsg('WEEKLY')}</option>
        <c:if test="${entity[fieldName].toString().equals('MONTHLY')}"> <c:set var="selectedMonthly" value="selected"/> </c:if>
        <option value="MONTHLY" ${selectedMonthly}>${srvI18n.getMsg('MONTHLY')}</option>
      </select>
    </div>
  </td>
</tr>
