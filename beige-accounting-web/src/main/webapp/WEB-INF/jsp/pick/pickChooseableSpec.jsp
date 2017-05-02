<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<button class="btn btn-sm" onClick="selectChooseableSpecType('${entity.itsType.itsId}', &quot;${entity.itsType.itsName}&quot;, '${namePlace}${param.nmEnt}');selectEntity('${entity.itsId}', &quot;${entity.itsName}&quot;, '${namePlace}${param.nmEnt}');">${srvI18n.getMsg("Pick")}</button>
