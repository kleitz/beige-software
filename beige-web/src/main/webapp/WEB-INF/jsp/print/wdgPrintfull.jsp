<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="title" value="ReportFull" scope="request"/>
<c:set var="orderPrintForm" value="orderPrintfullForm" scope="request"/>
<c:set var="orderPrintList" value="orderPrintfullList" scope="request"/>
<c:set var="printOwnedList" value="printOwnedListFull" scope="request"/>
<jsp:include page="printEntity.jsp"/>
