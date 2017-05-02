<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach var="entry" items="${mngUvds.makeFldPropLst(entity.getClass(), orderPrintForm)}">
  ${srvI18n.getMsg(entry.key)}:
  <c:set var="model" value="${entity[entry.key]}" scope="request"/>
  <jsp:include page="../toString/${entry.value.get('wdgToString')}.jsp"/><br>
</c:forEach>
<br>
