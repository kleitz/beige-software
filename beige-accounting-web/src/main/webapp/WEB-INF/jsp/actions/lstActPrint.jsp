<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty entity}">
  <a href="entity/?nameRenderer=printEntity&nameAction=print&nameEntity=${param.nameEntity}&idEntity=${entity.itsId}" target="_blank" class="btn btn-sm" >${srvI18n.getMsg("Print")}</a>
</c:if>
