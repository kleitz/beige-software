<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <b>${srvI18n.getMsg("enterEitherAlreadyOrLoadNew")}</b>
  </td>
</tr>
<tr>
  <td>
    <label for="GoodsSpecific.stringValue1">${srvI18n.getMsg("alreadyLoadedUrl")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input id="GoodsSpecific.stringValue1" name="GoodsSpecific.stringValue1" value="${entity.stringValue1}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="GoodsSpecific.path">${srvI18n.getMsg("file_to_load")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input id="GoodsSpecific.path" type="file" name="GoodsSpecific.path" onchange="inputHasBeenChanged(this);"/> 
      <input type="hidden" name="paramNameFileToUpload" value="GoodsSpecific.path">
    </div>
  </td>
</tr>
