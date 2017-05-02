<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="${namePlace}AssignGoodsToCatalogDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg('assign_catalog_bulk')}
      <button onclick="closeDlgCareful('${namePlace}AssignGoodsToCatalog')" class="btn-close">x</button>
    </div>
    <div style="padding: 5px; color: red;">${srvI18n.getMsg('warning_add_already_added')}</div>
    <div style="padding: 5px;">
      <b>${srvI18n.getMsg('filterAppearance')}:</b>
      <ul>
        <c:forEach var="fltrAppStr" items="${filterAppearance}">
          <li>
            ${fltrAppStr}
          </li>
        </c:forEach>
      </ul>
    </div>
    <form id="${namePlace}AssignGoodsToCatalogFrm" action="service/" method="POST" target="_blank">
      <input type="hidden" name="nmRnd" value="assignCatalogResult">
      <input type="hidden" name="nmHnd" value="hndTrdTrnsReq">
      <input type="hidden" name="nmPrc" value="PrcAssignGoodsToCatalog">
      <c:forEach var="entry" items="${filterMap}">
        <input type="hidden" name="${entry.key}" value="${entry.value}">
      </c:forEach>
      <table class="tbl-fieldset">
        <tr>
          <td>
            <label for="totalItems">${srvI18n.getMsg('totalFilteredItems')}:</label>
          </td>
          <td>
            <div class="input-line">
              <input disabled="disabled" type="text" value="${totalItems}">
              <input name="totalItems" required type="hidden" value="${totalItems}">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label for="accId">${srvI18n.getMsg('action')}:</label>
          </td>
          <td>
            <div class="input-line">
              <select name="goodsCatalogAction" onchange="inputHasBeenChanged(this);">
                <option value="add">${srvI18n.getMsg("add")}</option>
                <option value="remove">${srvI18n.getMsg("remove")}</option>
              </select>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label for="accId">${srvI18n.getMsg('catalog')}:</label>
          </td>
          <td>
            <div class="input-line">
              <input class="picked-appearence" id="assigncatAppearanceVisible" disabled="disabled" type="text" onchange="inputHasBeenChanged(this);">
              <input id="assigncatId" required type="hidden" name="CatalogGs.itsId">
              <button type="button" class="btn" onclick="openEntityPicker('CatalogGs', 'assign', 'cat', '&nmHnd=handlerEntityRequest&fltordPhasSubcatalogsVal=false&fltordPforcedFor=hasSubcatalogs');">...</button>
              <button type="button" class="btn" onclick="clearSelectedEntity('assigncat');">X</button>
            </div>
          </td>
        </tr>
      </table>
      <div class="form-actions">
        <button type="button" onclick="submitFormForNewWindow('${namePlace}AssignGoodsToCatalogFrm', true);">${srvI18n.getMsg("add_remove")}</button>
        <a href="#" onclick="closeDlgCareful('${namePlace}AssignGoodsToCatalog');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
