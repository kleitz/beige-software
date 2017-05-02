<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="frmReplicateEditDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg('Import_acc_entries')}
      <button onclick="closeDlgCareful('frmReplicateEdit')" class="btn-close">x</button>
    </div>
    <form id="frmReplicateEditFrm" action="replicator" method="POST" target="_blank" enctype="multipart/form-data">
      <table class="tbl-fieldset">
        <tr>
          <td>
            <label>${srvI18n.getMsg("user_name")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input name="userName" value="">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("user_pass")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input type="password" name="userPass" value="">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("url_source")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input name="urlSource" required value="http://localhost:8080/beige-accounting-web/secure/sendEntities">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("max_records_per_transaction")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input type="number" required name="maxRecords" value="100">
              <input type="hidden" name="replicatorName" value="replicatorTaxMarket">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${srvI18n.getMsg("requestedDatabaseId")}:</label>
          </td>
          <td>
            <div style="display: flex;">
              <input type="number" required name="requestedDatabaseId">
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label for="replicationMethodId">${srvI18n.getMsg('ReplicationAccMethod')}:</label>
          </td>
          <td>
            <div class="input-line">
              <input class="picked-appearence" id="replicationMethodAppearanceVisible" disabled="disabled" type="text" onchange="inputHasBeenChanged(this);">
              <input id="replicationMethodId" required type="hidden" name="replicationMethodId">
              <button type="button" class="btn" onclick="openEntityPicker('ReplicationAccMethod', 'replication', 'Method', '&mobile=${param.mobile}');">...</button>
              <button type="button" class="btn" onclick="clearSelectedEntity('replicationMethod');">X</button>
            </div>
          </td>
        </tr>
      </table>
      <div class="form-actions">
        <button type="button" class="btn" onclick="checkSubmitForm('frmReplicateEditFrm', false)">${srvI18n.getMsg("Replicate")}</button>
        <a href="#" onclick="closeDlgCareful('frmReplicateEdit');">${srvI18n.getMsg("Close")}</a>
        <input style="display: none" id="frmReplicateEditFrmFakeSubmit" type="submit"/>
      </div>
    </form>
  </div>
</dialog>
