<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<dialog id="infoDlg" class="dlg" oncancel="return false;">
  <div class="form-std">
    <div class="dialog-title">
      ${srvI18n.getMsg('About')}
      <button onclick="closeDlg('infoDlg')" class="btn-close">x</button>
    </div>
    <table class="tbl-fieldset">
      <tr>
        <td>
          <label>${srvI18n.getMsg("databaseId")}:</label>
        </td>
        <td>
          ${databaseInfo.databaseId}
        </td>
      </tr>
      <tr>
        <td>
          <label>${srvI18n.getMsg("databaseVersion")}:</label>
        </td>
        <td>
          ${databaseInfo.databaseVersion}
        </td>
      </tr>
      <tr>
        <td>
          <label>${srvI18n.getMsg("description")}:</label>
        </td>
        <td>
          ${databaseInfo.description}
        </td>
      </tr>
      <tr>
        <td>
          <label>${srvI18n.getMsg("appVersion")}:</label>
        </td>
        <td>
          ${appVersion}
        </td>
      </tr>
      <tr>
        <td><a href="http://www.beigesoft.org" target="_blank">Beigesoft ™</a></td>
        <td><a href="https://sites.google.com/site/beigesoftware" target="_blank">Other domain Beigesoft ™</a></td>
      </tr>
    </table>
    <div class="form-actions">
      <button type="button" class="btn" onclick="closeDlg('infoDlg');">${srvI18n.getMsg("Close")}</button>
    </div>
  </div>
</dialog>
