<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.stringValue1">${srvI18n.getMsg("itsValue")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <textarea rows="2" required name="${entity.getClass().simpleName}.stringValue1" onchange="inputHasBeenChanged(this);">${entity.stringValue1}</textarea> 
    </div>
  </td>
</tr>
