<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty entity['reversedId']}">
  <c:if test="${empty entity['invItem'].knownCost}">
    <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editEntityFolJson&nmsAct=entityCopy&nmEnt=${entitySimpleName}&PurchaseInvoiceLine.itsId=${entity.itsId}${ownerVersion}${flyParams}');">${srvI18n.getMsg("Copy")}</button>
  </c:if>
  <c:if test="${not empty entity['invItem'].knownCost}">
    <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=editLineKnownCostJson&nmsAct=entityCopy&nmEnt=${entitySimpleName}&PurchaseInvoiceLine.itsId=${entity.itsId}${ownerVersion}${flyParams}');">${srvI18n.getMsg("Copy")}</button>
  </c:if>
  <button class="btn btn-sm" onclick="getHtmlByAjaxCareful('GET', 'service/?nmHnd=${param.nmHnd}&nmRnd=reverseLineJson&nmsAct=entityReverse&nmEnt=${entitySimpleName}&PurchaseInvoiceLine.itsId=${entity.itsId}${ownerVersion}${flyParams}');">${srvI18n.getMsg("Reverse")}</button>
</c:if>
