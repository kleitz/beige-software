<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="now" class="java.util.Date" />
<dialog id="frmReportEditDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg('balance_sheet')}
      <button onclick="closeDlgCareful('frmReportEdit')" class="btn-close">x</button>
    </div>
    <form id="frmReportEditFrm" action="reportPdf/" method="GET" target="_blank">
      <input id="hndNmInput" type="hidden" name="nmHnd" value="hndlBalancePdfReq">
      
      <input type="hidden" name="nmRnd" value="balanceSheet">

      <table class="tbl-fieldset">
        <tr>
          <td>
            <label for="date1">${srvI18n.getMsg('date')}:</label>
          </td>
        </tr>
        <tr>
          <td>
            <input type="datetime-local" required name="date2" value="${srvDate.toIso8601DateTimeNoTz(now, null)}" onchange="inputHasBeenChanged(this);"/> 
          </td>
        </tr>
      </table>
      <div class="form-actions">
        <button type="button" onclick="submitFormForNewWindow('frmReportEditFrm', false);">${srvI18n.getMsg("ReportPdf")}</button>
        <button type="button" onclick="this.form.action='service/';document.getElementById('hndNmInput').value='srvReqBalanceSheet';submitFormForNewWindow('frmReportEditFrm', false);">${srvI18n.getMsg("Report")}</button>
        <a href="#" onclick="closeDlgCareful('frmReportEdit');">${srvI18n.getMsg("Close")}</a>
      </div>
    </form>
  </div>
</dialog>
