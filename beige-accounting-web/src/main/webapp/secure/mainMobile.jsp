<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <script type="text/javascript" src="../static/js/beige.ajax.js"></script>
  <script type="text/javascript" src="../static/js/beige.form.js"></script>
  <script type="text/javascript" src="../static/js/beige.accounting.js"></script>
  <script type="text/javascript" src="../static/js/beige.i18n.en.js"></script>
  <link rel="icon" type="image/png" href="../static/img/favicon.png">
  <link rel="stylesheet" href="../static/css/beige.common.css" />
  <title>Beige Accounting</title>
</head>
<body>

  <div class="navbar">
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Work")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=DebtorCreditorCategory&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("DebtorCreditorCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=DebtorCreditor&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("DebtorCreditors")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=ServicePurchasedCategory&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("ServicePurchasedCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=ServicePurchased&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("ServicePurchaseds")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=PrepaymentTo&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PrepaymentTos")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=PurchaseInvoice&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PurchaseInvoices")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=PaymentTo&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PaymentTos")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=PurchaseReturn&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PurchaseReturns")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=PrepaymentFrom&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PrepaymentFroms")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=SalesInvoice&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SalesInvoices")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=PaymentFrom&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PaymentFroms")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=SalesReturn&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SalesReturns")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=ManufacturingProcess&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("ManufacturingProcesss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Manufacture&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Manufactures")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=EmployeeCategory&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("EmployeeCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Employee&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Employees")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=WageType&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("WageTypes")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=WageTaxTable&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("WageTaxTables")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Wage&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Wages")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Warehouse")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Warehouse&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Warehouses")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=WarehouseSite&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("WarehouseSites")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=InvItemType&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("InvItemTypes")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=InvItemCategory&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("InvItemCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=InvItemTaxCategory&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("InvItemTaxCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=InvItem&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("InvItems")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=BeginningInventory&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("BeginningInventorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=GoodsLoss&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsLosss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=MoveItems&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("MoveItemss")}</a>
         <a href="warehouseRests/?nameRenderer=warehouseRests" target="_blank">${pageContext.servletContext.getAttribute("srvI18n").getMsg("warehouse_rests")}</a>
         <a href="warehouseSiteRests/?nameRenderer=warehouseSiteRests" target="_blank">${pageContext.servletContext.getAttribute("srvI18n").getMsg("warehouse_site_rests")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Accounting")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=AccSettings&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("AccSettings")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Expense&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Expenses")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=BankAccount&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("BankAccounts")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Property&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Propertys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=UnitOfMeasure&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("UnitOfMeasures")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Tax&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Taxs")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Currency&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Currencys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=Account&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Accounts")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listWholeJson&nameEntity=AccountingEntries&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("AccountingEntriess")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'entityList/?nameRenderer=listAccEntriesWholeJson&nameEntity=AccountingEntry&nameSrvEntity=srvAccEntryEditDescr&page=1&mobile=mobile%2F');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("AccountingEntrys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'report/?nameRenderer=ledgerFormJson');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Ledger")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'report/?nameRenderer=balanceFormJson');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("TrialBalance")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">...</a>
      <div class="dropdown-content">
        <a href="http://www.beigesoft.org" target="_blank">Beigesoft ™</a>
        <a href="https://sites.google.com/site/beigesoftware" target="_blank">Other domain Beigesoft ™</a>
        <c:if test="${not empty pageContext['request'].userPrincipal}">
          <a href="../index.jsp?logoff=true">${pageContext['request'].userPrincipal.name} ${pageContext.servletContext.getAttribute("srvI18n").getMsg("logout")}</a>
        </c:if>
        <c:if test="${empty pageContext['request'].userPrincipal}">
          <a href="../">${pageContext.servletContext.getAttribute("srvI18n").getMsg("exit")}</a>
        </c:if>
      </div>
    </div>
  </div>  

  <div id="lstMainPlace">
  </div>
  
  <div id="frmMainPlace">
  </div>

  <div id="frmSubPlace">
  </div>

  <div id="frmReport">
  </div>

  <div id="pickersPlace">
  </div>

  <div id="pickersPlaceDub">
  </div>

  <dialog id="dlgError" class="dlg dlg-alert">
    <div class="error">
      <div class="dialog-title error-title">
        Error!
        <button onclick="document.getElementById('dlgError').close()" class="btn-close btn-error">x</button>
      </div>
      <div id="errorPlace" class="msg-place">
      </div>
   </div>
  </dialog>

  <dialog id="dlgWarning" class="dlg dlg-alert">
    <div class="warning">
      <div class="dialog-title warning-title">
        Warning!
        <button onclick="document.getElementById('dlgWarning').close()" class="btn-close btn-warning">x</button>
      </div>
      <div id="warningPlace" class="msg-place">
      </div>
   </div>
  </dialog>

  <div id="dlgSuccess" class="dlg-notifier">
    <div class="success">
      <div class="dialog-title success-title">
        Success!
        <button onclick="document.getElementById('dlgSuccess').close()" class="btn-close btn-success">x</button>
      </div>
      <div id="successPlace" class="msg-place">
      </div>
   </div>
  </div>

</body>
</html>
