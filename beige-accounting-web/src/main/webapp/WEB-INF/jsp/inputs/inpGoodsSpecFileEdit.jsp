<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="GoodsSpecific.stringValue1">${srvI18n.getMsg("alreadyLoadedUrl")}</label>
  </td>
  <td>
    <div class="input-line">
      <input id="GoodsSpecific.stringValue1" name="GoodsSpecific.stringValue1" value="${entity.stringValue1}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
