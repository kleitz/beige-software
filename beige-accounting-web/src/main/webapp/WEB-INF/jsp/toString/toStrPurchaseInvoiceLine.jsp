<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty model.itsId}">
  # ${model.itsId}, ${srvI18n.getMsg("item_id")}=${model.invItem.itsId}, ${srvI18n.getMsg("itsCost")}=${model.itsCost}, ${srvI18n.getMsg("theRest")}=${model.theRest}
</c:if>
