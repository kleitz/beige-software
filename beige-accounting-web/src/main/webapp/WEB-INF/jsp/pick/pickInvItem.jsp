<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="knc" value="${entity.knownCost}"/>
<c:if test="${empty entity.knownCost}">
  <c:set var="knc" value="null"/>
</c:if>
<button class="btn btn-sm" onClick="selectEntity('${entity.itsId}', &quot;${entity.itsName}&quot;, '${namePlace}${param.nmEnt}'); setCostUom(${knc}, ${entity.defUnitOfMeasure.itsId}, &quot;${entity.defUnitOfMeasure.itsName}&quot;, '${namePlace}${param.nmEnt}');">${srvI18n.getMsg("Pick")}</button>
