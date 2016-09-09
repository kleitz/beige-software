<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsCost">${srvI18n.getMsg("itsCost")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" id="${entity.getClass().simpleName}itsCostVisible" disabled value="${entity['itsCost']}" onchange="inputHasBeenChanged(this);"/> 
      <input type="hidden" id="${entity.getClass().simpleName}itsCost" name="${entity.getClass().simpleName}.itsCost" value="${entity['itsCost']}"/> 
    </div>
  </td>
</tr>
