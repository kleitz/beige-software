<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<button class="btn btn-sm" onClick="selectSubacc('${entity.subaccId}', '${entity.subaccType}', &quot;${entity.subaccName}&quot;, '${namePlace}${param.nmEnt}');">${srvI18n.getMsg("Pick")}</button>
