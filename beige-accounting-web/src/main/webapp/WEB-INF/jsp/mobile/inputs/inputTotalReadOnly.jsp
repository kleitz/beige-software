<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
  <td>
    <label for="${entity.getClass().simpleName}.itsTotal">${srvI18n.getMsg("itsTotal")}</label>
  </td>
</tr>
<tr>
  <td>
    <div class="input-line">
      <input type="number" disabled id="${entity.getClass().simpleName}itsTotalVisible" value="${entity['itsTotal']}"/> 
      <input type="hidden" id="${entity.getClass().simpleName}itsTotal" name="${entity.getClass().simpleName}.itsTotal" value="${entity['itsTotal']}"/> 
    </div>
  </td>
</tr>
