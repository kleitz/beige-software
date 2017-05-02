<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" media="print" href="../../static/css/beige.print-a4.css" />
  <link rel="icon" type="image/png" href="../../static/img/favicon.png">
  <link rel="stylesheet" type="text/css" href="../../static/css/beige.reports.css" />
  <title>${srvI18n.getMsg(entity.getClass().simpleName)}</title>
</head>
<body>


<div class="organizationData">
  <b>${accSettings.organization}</b><br/>
  <c:if test="${not empty accSettings.taxIdentificationNumber}">
    ${srvI18n.getMsg('taxIdentificationNumber')}: ${accSettings.taxIdentificationNumber}<br/>
  </c:if>
  <c:if test="${not empty accSettings.regZip}">
    ${srvI18n.getMsg('regZip')}: ${accSettings.regZip}<br/>
  </c:if>
  <c:if test="${not empty accSettings.regAddress1}">
    ${srvI18n.getMsg('regAddress1')}: ${accSettings.regAddress1}<br/>
  </c:if>
  <c:if test="${not empty accSettings.regAddress2}">
    ${srvI18n.getMsg('regAddress2')}: ${accSettings.regAddress2}<br/>
  </c:if>
  <c:if test="${not empty accSettings.regCity}">
    ${srvI18n.getMsg('regCity')}: ${accSettings.regCity}<br/>
  </c:if>
  <c:if test="${not empty accSettings.regState}">
    ${srvI18n.getMsg('regState')}: ${accSettings.regState}<br/>
  </c:if>
  <c:if test="${not empty accSettings.regCountry}">
    ${srvI18n.getMsg('regCountry')}: ${accSettings.regCountry}<br/>
  </c:if>
</div>
<div class="doc-title">${srvI18n.getMsg('Invoice')} #
  <c:if test="${empty entity.idBirth}">
    ${entity.idDatabaseBirth}-${entity.itsId}
  </c:if>
  <c:if test="${not empty entity.idBirth}">
    ${entity.idDatabaseBirth}-${entity.idBirth}
  </c:if>
  <fmt:formatDate value="${entity.itsDate}" type="date" timeStyle="short"/>
</div>
<div class="entity">
  <b>${srvI18n.getMsg('customer')}:</b>
  <div class="customerData">
    ${entity.customer.itsName}
    <c:if test="${not empty entity.customer.taxIdentificationNumber}">
      <br/>${srvI18n.getMsg('taxIdentificationNumber')}: ${entity.customer.taxIdentificationNumber}
    </c:if>
    <c:if test="${not empty entity.customer.regZip}">
      <br/>${srvI18n.getMsg('regZip')}: ${entity.customer.regZip}
    </c:if>
    <c:if test="${not empty entity.customer.regAddress1}">
      <br/>${srvI18n.getMsg('regAddress1')}: ${entity.customer.regAddress1}
    </c:if>
    <c:if test="${not empty entity.customer.regAddress2}">
      <br/>${srvI18n.getMsg('regAddress2')}: ${entity.customer.regAddress2}
    </c:if>
    <c:if test="${not empty entity.customer.regCity}">
      <br/>${srvI18n.getMsg('regCity')}: ${entity.customer.regCity}
    </c:if>
    <c:if test="${not empty entity.customer.regState}">
      <br/>${srvI18n.getMsg('regState')}: ${entity.customer.regState}
    </c:if>
    <c:if test="${not empty entity.customer.regCountry}">
      <br/>${srvI18n.getMsg('regCountry')}: ${entity.customer.regCountry}
    </c:if>
  </div>
  <br/>
  <c:set var="doc" value="${entity}"/>
  <c:set var="orderPrintList" value="orderPrintList" scope="request"/>
  <jsp:include page="printOwnedList.jsp"/>
  <div>
    <div class="totals">
      <b>${srvI18n.getMsg('subtotal')}:</b>
      ${doc.subtotal}<br/>
      <b>${srvI18n.getMsg('totalTaxes')}:</b>
      ${doc.totalTaxes}<br/>
      <b>${srvI18n.getMsg('itsTotal')}:</b>
      ${doc.itsTotal}
    </div>
  </div>
</div>
</body>
</html>
