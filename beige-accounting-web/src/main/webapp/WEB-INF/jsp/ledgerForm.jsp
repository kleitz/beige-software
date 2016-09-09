<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="now" class="java.util.Date" />
<dialog id="frmReportEditDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg('Ledger')}
      <button onclick="closeDlgCareful('frmReportEdit')" class="btn-close">x</button>
    </div>
    <form id="frmReportEditFrm" action="ledger/" method="GET">
      <input type="hidden" name="nameRenderer" value="ledger">
      <table class="tbl-fieldset">
        <tr>
          <td>
            <label for="date1">${srvI18n.getMsg('date1')}:</label>
          </td>
        </tr>
        <tr>
          <td>
            <input type="datetime-local" required name="date1" value="${utlJsp.dateTimeToIso8601(now)}" onchange="inputHasBeenChanged(this);"/> 
          </td>
        </tr>
        <tr>
          <td>
            <label for="date2">${srvI18n.getMsg('date2')}:</label>
          </td>
        </tr>
        <tr>
          <td>
            <input type="datetime-local" required name="date2" value="${utlJsp.dateTimeToIso8601(now)}" onchange="inputHasBeenChanged(this);"/> 
          </td>
        </tr>
        <tr>
          <td>
            <label for="accId">${srvI18n.getMsg('account')}:</label>
          </td>
        </tr>
        <tr>
          <td>
            <div class="input-line">
              <input class="picked-appearence" id="ledgeraccAppearanceVisible" disabled="disabled" type="text" onchange="inputHasBeenChanged(this); clearSubacc('ledger', 'acc', 'subacc');">
              <input id="ledgeraccId" required type="hidden" name="accId">
              <button type="button" class="btn" onclick="openEntityPicker('Account', 'ledger', 'acc', '&fltordPisUsedVal=true&fltordPforcedFor=isUsed&mobile=${param.mobile}');">...</button>
              <button type="button" class="btn" onclick="clearSelectedEntity('ledgeracc');">X</button>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label for="ledgersubaccAppearanceVisible">${srvI18n.getMsg('subaccount')}:</label>
          </td>
        </tr>
        <tr>
          <td>
            <div class="input-line">
              <input class="picked-appearence" id="ledgersubaccAppearanceVisible" disabled type="text" onchange="inputHasBeenChanged(this);">
              <input id="ledgersubaccAppearance" type="hidden" name="subaccName">
              <input id="ledgersubaccType" type="hidden" name="subaccType">
              <input id="ledgersubaccId" type="hidden" name="subaccId">
              <button id="ledgersubaccChoose" type="button" class="btn" onclick="openPickerSubacc('ledger', 'acc', 'subacc', '${param.mobile}');">...</button>
              <button id="ledgersubaccClear" type="button" class="btn" onclick="clearSubacc('ledger', 'acc', 'subacc');">X</button>
            </div>
          </td>
        </tr>
      </table>
      <div class="form-actions">
        <input style="display: none" id="frmReportEditFrmFakeSubmit" type="submit"/>
        <button type="button" onclick="submitFormForNewWindow('frmReportEditFrm', false);">${srvI18n.getMsg("Report")}</button>
        <a href="#" onclick="closeDlgCareful('frmReportEdit');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
