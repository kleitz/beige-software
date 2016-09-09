<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
${model},
<c:if test="${model.doubleValue() == 0}">
  ${srvI18n.getMsg("unpaid")}
</c:if>
<c:if test="${model.doubleValue() == entity.itsTotal.doubleValue()}">
  ${srvI18n.getMsg("paid")}
</c:if>
<c:if test="${model.doubleValue() gt entity.itsTotal.doubleValue()}">
  ${srvI18n.getMsg("overpaid")}
</c:if>
<c:if test="${model.doubleValue() lt entity.itsTotal.doubleValue() && model.doubleValue() gt 0}">
  ${srvI18n.getMsg("partially_paid")}
</c:if>
