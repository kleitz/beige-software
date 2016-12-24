<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty model.itsId}">
  <c:if test="${empty model.idBirth}">
    # ${model.idDatabaseBirth}-${model.itsId}, ${srvI18n.getMsg("item_id")}=${model.invItem.itsId}, ${srvI18n.getMsg("itsCost")}=${model.itsCost}, ${srvI18n.getMsg("theRest")}=${model.theRest}
  </c:if>
  <c:if test="${not empty model.idBirth}">
    # ${model.idDatabaseBirth}-${model.idBirth}, ${srvI18n.getMsg("item_id")}=${model.invItem.itsId}, ${srvI18n.getMsg("itsCost")}=${model.itsCost}, ${srvI18n.getMsg("theRest")}=${model.theRest}
  </c:if>
</c:if>
