<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<jsp:include page="print/${mngUvds.classesSettings.get(entity.getClass().canonicalName).get('wdgPrint')}${param.actionAdd}.jsp"/>
