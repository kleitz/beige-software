<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="loginErrorJsp" value="Invalid user name or password!" scope="request"/>
  
<jsp:include page="fragments/loginTmpl.jsp"/>
