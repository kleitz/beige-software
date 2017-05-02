<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="inputType" value="file" scope="request"/>
<c:set var="inputAdd" value="" scope="request"/>
<jsp:include page="inputSimpleTmpl.jsp"/>
<input type="hidden" name="paramNameFileToUpload" value="${entity.getClass().simpleName}.${fieldName}">
<input type="hidden" name="fieldNameFileName" value="itsName">
