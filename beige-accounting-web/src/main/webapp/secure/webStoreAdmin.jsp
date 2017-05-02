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
  <link rel="icon" type="image/png" href="../static/img/favicon.png">
  <script type="text/javascript" src="../static/js/beige.i18n.en.js"></script>
  <link rel="stylesheet" href="../static/css/beige.common.css" />
  <title>Beige Accounting</title>
</head>
<body>

  <div class="navbar">
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("CatalogGs")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeWebstoreGoodsJson&nmEnt=InvItem&page=1&flyNeedFltAppear=true&fltordMitsTypeOpr=in&fltordMitsTypeValId=1,4&fltordMitsTypeValAppearance=Merchandise or stock in trade,Finished product&fltordMforcedFor=itsType');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("goods")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=CatalogGs&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("CatalogGss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=SubcatalogsCatalogsGs&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SubcatalogsCatalogsGss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsCatalogs&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsCatalogss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsRating&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsRatings")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=PickUpPlace&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PickUpPlaces")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsAvailable&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsAvailables")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SpecificsOfItem")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=SpecificsOfItemGroup&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SpecificsOfItemGroups")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=SpecificsOfItem&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SpecificsOfItems")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=ChooseableSpecificsType&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("ChooseableSpecificsTypes")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=ChooseableSpecifics&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("ChooseableSpecificss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsSpecific&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsSpecifics")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Pricing")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=DebtorCreditor&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("DebtorCreditors")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=PriceCategoryOfBuyers&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PriceCategoryOfBuyerss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=PriceCategoryOfItems&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PriceCategoryOfItemss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=PriceCategory&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("PriceCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=BuyerPriceCategory&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("BuyerPriceCategorys")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsPrice&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsPrices")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Advising")}</a>
      <div class="dropdown-content">
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=AdviseCategoryOfGs&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("AdviseCategoryOfGss")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=GoodsAdviseCategories&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("GoodsAdviseCategoriess")}</a>
         <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=AdvisedGoodsForGoods&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("AdvisedGoodsForGoodss")}</a>
      </div>
    </div>
    <div class="dropdown">
      <a href="#" class="dropdown-btn">...</a>
      <div class="dropdown-content">
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=TradingSettings&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("TradingSettings")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=SettingsAdd&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("SettingsAdd")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=EmailConnect&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("EmailConnects")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmHnd=handlerEntityRequest&nmsAct=list&nmRnd=listWholeJson&nmEnt=EmailMsg&page=1');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("EmailMsgs")}</a>
        <a href="service?nmHnd=hndTrdSmpReq&nmRnd=refreshedGoodsInList&nmPrc=PrcRefreshGoodsInList" target="_blank">${pageContext.servletContext.getAttribute("srvI18n").getMsg("RefreshGoodsInList")}</a>
        <a href="#" onclick="getHtmlByAjax('GET', 'service/?nmRnd=aboutJson&nmHnd=hndAbout');">${pageContext.servletContext.getAttribute("srvI18n").getMsg("about")}</a>
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

  <div id="frmReplicate">
  </div>

  <div id="pickersPlace">
  </div>

  <div id="pickersPlaceDub">
  </div>

  <div id="targetInfo">
  </div>

  <dialog id="dlgConfirm" class="dlg dlg-alert">
      <div class="confirm">
        <div class="dialog-title confirm-title">
          Conformation.
          <button onclick="document.getElementById('dlgConfirm').close();" class="btn-close btn-confirm">x</button>
        </div>
        <div id="confirmPlace" class="msg-place">
        </div>
        <div class="dlg-actions">
          <button id="confirmYes" class="btn btn-act btn-confirm">${pageContext.servletContext.getAttribute("srvI18n").getMsg("Yes")}</button>
          <button onclick="document.getElementById('dlgConfirm').close();" class="btn btn-act btn-confirm">${pageContext.servletContext.getAttribute("srvI18n").getMsg("No")}</button>
        </div>
     </div>
  </dialog>

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
