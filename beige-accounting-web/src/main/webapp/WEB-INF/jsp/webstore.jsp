<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    
    <link rel="icon" type="image/png" href="static/img/favicon.png">

    <title>Beige-WEB-Store</title>

    <!-- Bootstrap core CSS -->
    <link href="static/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="static/css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="static/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="static/css/theme.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="static/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>


    <!-- Fixed navbar -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <c:if test="${tradingSettings.isShowLogo}">
            <a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogId" class="navbar-brand navbar-brand-img"><img src="static/img/logo-web-store.png"></a>
          </c:if>
          <a class="navbar-brand" href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage">${tradingSettings.webStoreName}</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <c:forEach var="cat1l" items="${cat1and2l}">
              <c:if test="${not empty cat1l.subcatalogs}">
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${cat1l.itsName} <span class="caret"></span></a>
                  <ul class="dropdown-menu">
                    <c:forEach var="cat2l" items="${cat1l.subcatalogs}">
                      <c:if test="${not empty cat2l.subcatalogs}">
                        <li><a href="#" data-toggle="modal" data-target="#subcatalogsMdl${cat2l.itsId}">${cat2l.itsName}</a></li>
                      </c:if>
                      <c:if test="${empty cat2l.subcatalogs}">
                        <li><a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogName=${cat2l.itsName}&catalogId=${cat2l.itsId}">${cat2l.itsName}</a></li>
                      </c:if>
                    </c:forEach>
                  </ul>
                </li>
              </c:if>
              <c:if test="${empty cat1l.subcatalogs}">
                <li><a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogName=${cat1l.itsName}&catalogId=${cat1l.itsId}">${cat1l.itsName}</a></li>
              </c:if>
              <c:if test="${not empty listFilter}">
                <li><a href="#" data-toggle="modal" data-target="#filterMdl"><span class="glyphicon glyphicon-filter"></span><span class="glyphicon glyphicon-sort"></span></a></li>
              </c:if>
            </c:forEach>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <c:if test="${empty logginIfo}">
              <li><a href="#" class="glyphicon glyphicon-log-in" data-toggle="tooltip" title="${srvI18n.getMsg('log_in')}"></a></li>
            </c:if>
            <c:if test="${not empty logginIfo}">
              <li><a href="#" class="glyphicon glyphicon-log-out" data-toggle="tooltip" title="${srvI18n.getMsg('log_out')}"></a></li>
            </c:if>
            <c:if test="${not empty shoppingCart}">
              <li><a href="#" class="glyphicon glyphicon-shopping-cart" data-toggle="tooltip" title="${srvI18n.getMsg('shopping_cart')}" onclick="$('#cartMdl').modal('toggle');"><span class="badge">${shoppingCart.totalItems}</span></a></li>
            </c:if>
            <li><a href="#about" class="glyphicon glyphicon-info-sign" data-toggle="tooltip" title="${srvI18n.getMsg('About')}"></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
    

    <div class="container" role="main">

<!--Main list-->
<c:if test="${not empty catalogName}">
  <div class="well well-sm">${catalogName}</div>
</c:if>
<c:set var="divImg">
   <div class="col-md-4 col-xs-4">  
</c:set>
<c:set var="divDscr">
  <div class="col-md-8 col-xs-8">
</c:set>
<c:if test="${tradingSettings.columnsCount != 1}">
  <c:set var="divImg">
     <div class="col-md-2 col-xs-4">  
  </c:set>
  <c:set var="divDscr">
    <div class="col-md-4 col-xs-8">
  </c:set>
</c:if>
<c:forEach var="item" items="${itemsList}" varStatus="status">
  <c:set var="itsQuantity" value="1"/>
  <c:set var="cartItemItsId" value="null"/>
  <c:set var="orderedQuantity" value=""/>
  <c:if test="${not empty cartMap && not empty cartMap[item.itsType][item.itemId]}">
    <c:set var="itsQuantity" value="${cartMap[item.itsType][item.itemId].itsQuantity}"/>
    <c:set var="cartItemItsId" value="${cartMap[item.itsType][item.itemId].itsId}"/>
    <c:set target="${cartMap[item.itsType][item.itemId]}" property="availableQuantity" value="${item.availableQuantity}"/>
    <c:set var="orderedQuantity">
      <span class="badge">${cartMap[item.itsType][item.itemId].itsQuantity}</span>
    </c:set>
  </c:if>
  <c:if test="${tradingSettings.columnsCount != 1 && (status.index mod 2) == 0}">
    <div class="row">
  </c:if>
  ${divImg}
    <c:set var="imgUrlPref" value="../"/>
    <c:if test="${item.imageUrl.startsWith('http')}">
      <c:set var="imgUrlPref" value=""/>
    </c:if>
    <img class="img-responsive" src="${imgUrlPref}${item.imageUrl}">
  </div>
  ${divDscr}
    <h4><b>${item.itsPrice}</b> ${item.itsName}
      <a href="#" class="glyphicon glyphicon-shopping-cart" onclick="setCartItem(${item.itsType.ordinal()}, ${item.itemId}, '${item.itsName}', ${item.itsPrice}, ${itsQuantity}, ${item.availableQuantity}, ${cartItemItsId})">${orderedQuantity}</a>
    </h4>
    <p>${item.specificInList}</p>
  </div>
  <c:if test="${tradingSettings.columnsCount != 1 && (status.index mod 2) != 0}">
    </div>
  </c:if>
</c:forEach>
<c:if test="${tradingSettings.columnsCount != 1 && itemsList.size() == 1}">
  </div>
</c:if>

<c:if test="${not empty pages}">
  <div class="pages">
    <nav aria-label="...">
      <ul class="pagination">
        <c:forEach var="pg" items="${pages}">
          <c:if test="${pg.isCurrent}">
            <li class="active"><a href="#">${pg.value} <span class="sr-only">(current)</span></a></li>
          </c:if>
          <c:if test="${!pg.isCurrent}">
            <li><a href="#">${pg.value}</a></li>
          </c:if>
        </c:forEach>
      </ul>
    </nav>
  </div>
</c:if>

<!-- Modal cart item adder -->
<div class="modal fade" id="cartAddMdl" tabindex="-1" role="dialog" aria-labelledby="cartAddMdlLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="cartAddMdlLabel"><span class="glyphicon glyphicon-shopping-cart"></span><span class="glyphicon glyphicon-cart"></span>${srvI18n.getMsg("add_item_to_cart")}</h4>
      </div>
      <div class="modal-body">
        <form action="servicePublic" method="POST">
          <input type="hidden" name="nmRnd" value="webstore">
          <input type="hidden" name="nmHnd" value="hndTrdTrnsReq">
          <input type="hidden" name="nmPrc" value="PrcItemInCart">
          <c:if test="${not empty param.catalogId}">
            <input type="hidden" name="catalogId" value="${param.catalogId}">
            <input type="hidden" name="catalogName" value="${param.catalogName}">
          </c:if>
          <div class="form-group">
            <label>${srvI18n.getMsg("item")}:</label>
            <textarea id="cartItemName" readonly class="form-control">
            </textarea>
            <label>${srvI18n.getMsg("itsPrice")}:</label>
            <input id="cartItemPrice" readonly class="form-control">
            <label>${srvI18n.getMsg("itsQuantity")}:</label>
            <input type="number" step="1" min="1" id="cartItemQuantity" name="cartItemQuantity" class="form-control" onchange="refreshCartItemTotal('');">
            <label>${srvI18n.getMsg("itsTotal")}:</label>
            <input id="cartItemTotal" readonly class="form-control">
            <input type="hidden" id="cartItemAvailableQuantity" name="cartItemAvailableQuantity">
            <input type="hidden" id="cartItemType" name="cartItemType">
            <input type="hidden" id="cartItemId" name="cartItemId">
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary">Save</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Modal cart item edit -->
<div class="modal fade" id="cartEditMdl" tabindex="-1" role="dialog" aria-labelledby="cartEditMdlLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" onclick="$('#cartMdl').modal('show');" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="cartEditMdlLabel"><span class="glyphicon glyphicon-shopping-cart"></span><span class="glyphicon glyphicon-cart"></span>${srvI18n.getMsg("item_change_quantity")}</h4>
      </div>
      <div class="modal-body">
        <form action="servicePublic" method="POST">
          <input type="hidden" name="nmRnd" value="webstore">
          <input type="hidden" name="nmHnd" value="hndTrdTrnsReq">
          <input type="hidden" name="nmPrc" value="PrcItemInCart">
          <c:if test="${not empty param.catalogId}">
            <input type="hidden" name="catalogName" value="${param.catalogName}">
            <input type="hidden" name="catalogId" value="${param.catalogId}">
          </c:if>
          <div class="form-group">
            <label>${srvI18n.getMsg("item")}:</label>
            <textarea id="cartItemNameEdit" readonly class="form-control">
            </textarea>
            <label>Price:</label>
            <input id="cartItemPriceEdit" readonly class="form-control">
            <label>${srvI18n.getMsg("itsQuantity")}:</label>
            <input type="number" step="1" min="1" id="cartItemQuantityEdit" name="cartItemQuantity" class="form-control" onchange="refreshCartItemTotal('Edit');">
            <label>${srvI18n.getMsg("itsTotal")}:</label>
            <input id="cartItemTotalEdit" readonly class="form-control">
            <input type="hidden" id="cartItemAvailableQuantityEdit" name="cartItemAvailableQuantity">
            <input type="hidden" id="cartItemTypeEdit" name="cartItemType">
            <input type="hidden" id="cartItemIdEdit" name="cartItemId">
            <input type="hidden" id="cartItemItsIdEdit" name="cartItemItsId">
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary">${srvI18n.getMsg("Save")}</button>
            <button type="button" class="btn btn-default" data-dismiss="modal" onclick="$('#cartMdl').modal('show');">${srvI18n.getMsg("Close")}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Modal cart item remove -->
<div class="modal fade" id="cartDelMdl" tabindex="-1" role="dialog" aria-labelledby="cartDelMdlLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" onclick="$('#cartMdl').modal('show');" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="cartDelMdlLabel"><span class="glyphicon glyphicon-shopping-cart"></span><span class="glyphicon glyphicon-cart"></span>${srvI18n.getMsg("item_remove")}</h4>
      </div>
      <div class="modal-body">
        <form action="servicePublic" method="POST">
          <input type="hidden" name="nmRnd" value="webstore">
          <input type="hidden" name="nmHnd" value="hndTrdTrnsReq">
          <input type="hidden" name="nmPrc" value="PrcDelItemFromCart">
          <c:if test="${not empty param.catalogId}">
            <input type="hidden" name="catalogName" value="${param.catalogName}">
            <input type="hidden" name="catalogId" value="${param.catalogId}">
          </c:if>
          <div class="form-group">
            <label>${srvI18n.getMsg("item")}:</label>
            <textarea id="cartItemNameDel" readonly class="form-control">
            </textarea>
            <label>Price:</label>
            <input id="cartItemPriceDel" readonly class="form-control">
            <label>${srvI18n.getMsg("itsQuantity")}:</label>
            <input readonly id="cartItemQuantityDel" class="form-control">
            <label>${srvI18n.getMsg("itsTotal")}:</label>
            <input id="cartItemTotalDel" readonly class="form-control">
            <input type="hidden" id="cartItemItsIdDel" name="cartItemItsId">
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary">${srvI18n.getMsg("Delete")}</button>
            <button type="button" class="btn btn-default" data-dismiss="modal" onclick="$('#cartMdl').modal('show');">${srvI18n.getMsg("Close")}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Modal cart -->
<div class="modal fade" id="cartMdl" tabindex="-1" role="dialog" aria-labelledby="cartMdlLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="cartMdlLabel"><span class="glyphicon glyphicon-shopping-cart"></span></span>${srvI18n.getMsg("shopping_cart")}</h4>
      </div>
      <div class="modal-body">
        <c:forEach var="cartItem" items="${shoppingCart.itsItems}">
          <c:if test="${!cartItem.isDisabled}">
            <div class="row">
              <div class="col-md-4 col-xs-4">
                <b>${srvI18n.getMsg("item")}:</b>
              </div>
              <div class="col-md-8 col-xs-8">
                <b>${cartItem.itsName}</b>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4 col-xs-4">
                Price:
              </div>
              <div class="col-md-8 col-xs-8">
                ${cartItem.itsPrice}
              </div>
            </div>
            <div class="row">
              <div class="col-md-4 col-xs-4">
                ${srvI18n.getMsg("itsQuantity")}:
              </div>
              <div class="col-md-4 col-xs-4">
                ${cartItem.itsQuantity}
              </div>
              <div class="col-md-2 col-xs-2">
                <a href="#" class="glyphicon glyphicon glyphicon-pencil" data-dismiss="modal" onclick="setCartItem(${cartItem.itemType.ordinal()}, ${cartItem.itemId}, '${cartItem.itsName}', ${cartItem.itsPrice}, ${cartItem.itsQuantity}, ${cartMap[cartItem.itemType][cartItem.itemId].availableQuantity}, ${cartItem.itsId});"></a>
              </div>
              <div class="col-md-2 col-xs-2">
                <a href="#" class="glyphicon glyphicon glyphicon-remove" data-dismiss="modal" onclick="$('#cartDelMdl').modal({keyboard: false, backdrop: false}); delCartItem(${cartItem.itemType.ordinal()}, ${cartItem.itemId}, '${cartItem.itsName}', ${cartItem.itsPrice}, ${cartItem.itsQuantity}, ${cartItem.itsId});"></a>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4 col-xs-4">
                ${srvI18n.getMsg("itsTotal")}:
              </div>
              <div class="col-md-8 col-xs-8">
                ${cartItem.itsTotal}
              </div>
            </div>
          </c:if>
        </c:forEach>
        <div class="row">
          <div class="col-md-6 col-xs-6">
            <b>${srvI18n.getMsg("totalItems")}:</b>
          </div>
          <div class="col-md-6 col-xs-6">
            <b>${shoppingCart.totalItems}</b>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6 col-xs-6">
            <b>${srvI18n.getMsg("itsTotal")}:</b>
          </div>
          <div class="col-md-6 col-xs-6">
            <b>${shoppingCart.itsTotal}</b>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">${srvI18n.getMsg("Close")}</button>
        <a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcCartCheckOut" type="button" class="btn btn-primary">${srvI18n.getMsg("check_out")}</a>
      </div>
    </div>
  </div>
</div>

<!-- Modal filter -->
<div class="modal fade" id="filterMdl" tabindex="-1" role="dialog" aria-labelledby="filterMdlLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="filterMdlLabel"><span class="glyphicon glyphicon-filter"></span><span class="glyphicon glyphicon-sort"></span> Computers and notebooks</h4>
      </div>
      <div class="modal-body">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Apply</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal subcatalogs -->
<c:forEach var="entry" items="${cat3aml}">
  <div class="modal fade" id="subcatalogsMdl${entry.key.itsId}" tabindex="-1" role="dialog" aria-labelledby="subcatalogsMdl${entry.key.itsId}Label">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="subcatalogsMdl${entry.key.itsId}Label">${entry.key.itsName}</h4>
        </div>
        <div class="modal-body">
          <ul>
            <c:forEach var="cat3l" items="${entry.value}">
              <li>
                <c:if test="${empty cat3l.subcatalogs}">
                  <a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogId=${cat3l.itsId}&catalogName=${cat3l.itsName}" class="cat3ml">${cat3l.itsName}</a>
                </c:if>
                <c:if test="${not empty cat3l.subcatalogs}">
                  <a href="#" class="cat3ml">${cat3l.itsName}</a>
                  <c:forEach var="cat4l" items="${cat3l.subcatalogs}">
                    <li>
                      <c:if test="${empty cat4l.subcatalogs}">
                        <a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogId=${cat4l.itsId}&catalogName=${cat4l.itsName}" class="cat3ml">${cat4l.itsName}</a>
                      </c:if>
                      <c:if test="${not empty cat4l.subcatalogs}">
                        <a href="#" class="cat3ml">${cat4l.itsName}</a>
                        <c:forEach var="cat5l" items="${cat4l.subcatalogs}">
                          <li>
                            <c:if test="${empty cat5l.subcatalogs}">
                              <a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogId=${cat5l.itsId}&catalogName=${cat5l.itsName}" class="cat3ml">${cat5l.itsName}</a>
                            </c:if>
                            <c:if test="${not empty cat5l.subcatalogs}">
                              <a href="#" class="cat3ml">${cat5l.itsName}</a>
                              <c:forEach var="cat6l" items="${cat5l.subcatalogs}">
                                <li>
                                  <a href="servicePublic?nmRnd=webstore&nmHnd=hndTrdTrnsReq&nmPrc=PrcWebstorePage&catalogId=${cat6l.itsId}&catalogName=${cat6l.itsName}" class="cat3ml">${cat6l.itsName}</a>
                                </li>
                              </c:forEach>
                            </c:if>
                          </li>
                        </c:forEach>
                      </c:if>
                    </li>
                  </c:forEach>
                </c:if>
              </li>
            </c:forEach>            
          </ul>
        </div>
      </div>
    </div>
  </div>
</c:forEach>

    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/bootstrap.min.js"></script>
    <script src="static/js/beige.webstore.js"></script>
    <c:if test="${not empty param.cartItemItsId}">
      <script>$('#cartMdl').modal('show');</script>
    </c:if>
     <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="static/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>
