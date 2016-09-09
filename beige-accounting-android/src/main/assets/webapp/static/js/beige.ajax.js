/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

function getHtmlByAjax(method,url) {
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.onreadystatechange = function(){
    takeRequest(xmlhttp);
  };
  xmlhttp.open(method, url, true);
  xmlhttp.send();
};

function postFormByAjax(url, params) {
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.onreadystatechange = function(){
    takeRequest(xmlhttp);
  };
  xmlhttp.open("POST", url, true);
  xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  xmlhttp.send(params);
};

function sendFormAjax(frm, params) {
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.onreadystatechange = function(){
    takeRequest(xmlhttp);
  };
  xmlhttp.open(frm.method, frm.action, true);
  if (frm.method == 'post') {
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  }
  xmlhttp.send(params);
};

function takeRequest(xmlhttp) {
  if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
    if (xmlhttp.responseText.indexOf('j_security_check') > 0) {
      location.reload();
    }
    var complRes = JSON.parse(xmlhttp.responseText);
    var arr=complRes.multiTargetResponse;
    for (var i = 0; i < arr.length; i++){
      var target = document.getElementById(arr[i].nameTarget);
      if (target == null) {
        target = document.createElement('div');
        target.setAttribute("id", arr[i].nameTarget);
        document.getElementById(arr[i].nameTargetParent).appendChild(target);
      }
      target.innerHTML = arr[i].content;
      if (arr[i].javascript!=null) {
        eval(arr[i].javascript);
      }
    }
  }
  else if (xmlhttp.readyState == 4) {
    if (xmlhttp.responseText == "") {
      document.getElementById("errorPlace").innerHTML = "<h4>http status = " + xmlhttp.status
        +"!!! </h4>";
    } else {
      document.getElementById("errorPlace").innerHTML = xmlhttp.responseText;
    }
    document.getElementById("dlgError").showModal();
  }
};
