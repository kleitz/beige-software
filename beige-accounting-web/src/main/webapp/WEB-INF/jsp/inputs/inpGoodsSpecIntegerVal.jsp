<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.longValue1">${srvI18n.getMsg("itsValue")}</label>
  </td>
  <td>
    <div class="input-line">
      <input type="number" required name="${entity.getClass().simpleName}.longValue1" value="${entity['longValue1']}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.stringValue1">${srvI18n.getMsg("UnitOfMeasure")} ${srvI18n.getMsg("if_present")}</label>
  </td>
  <td>
    <div class="input-line">
      <input type="text" name="${entity.getClass().simpleName}.stringValue1" value="${entity['stringValue1']}" onchange="inputHasBeenChanged(this);"/> 
    </div>
  </td>
</tr>
