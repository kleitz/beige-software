<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${orderMap[ordByName] != fieldName}"> <c:set var="checkedOrNot" value=""/> </c:if>
<c:if test="${orderMap[ordByName] == fieldName}"> <c:set var="checkedOrNot" value="checked"/> </c:if>
<label>
  <input type="radio" name="${ordByName}" value="${fieldName}" ${checkedOrNot} onchange="inputHasBeenChanged(this);">
  ${srvI18n.getMsg(fieldName)}</label>
