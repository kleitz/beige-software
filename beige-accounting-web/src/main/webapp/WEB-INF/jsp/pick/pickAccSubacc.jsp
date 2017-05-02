<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<button class="btn btn-sm" onClick="selectAccSubacc('${entity.itsId}', &quot;${entity.itsName}&quot;, '${namePlace}${param.nmEnt}');">${srvI18n.getMsg("Pick")}</button>
