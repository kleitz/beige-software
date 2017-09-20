package org.beigesoft.replicator.service;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.net.URL;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.HttpURLConnection;
import java.net.CookiePolicy;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.CookieHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.beigesoft.service.UtilXml;
import org.beigesoft.log.LoggerSimple;
import org.beigesoft.exception.ExceptionWithCode;

/**
 * <p>HttpURLConnection Test.</p>
 *
 * @author Yury Demidenko
 */
public class HttpURLConnectionTest {

  /**
   * <p>Logger.</p>
   **/
  LoggerSimple logger = new LoggerSimple();

  UtilXml utilXml = new UtilXml();

  //@Test //tomcat must work
  public void tstGetPost() throws Exception {
    String urlSource = "http://localhost:8080/beige-accounting-web/secure/main.jsp";
    String urlBase = urlSource.substring(0, urlSource.indexOf("secure") - 1);
    assertEquals("http://localhost:8080/beige-accounting-web", urlBase);
    CookieManager cookieManager = new CookieManager();
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    CookieHandler.setDefault(cookieManager);
    URL url = new URL(urlSource);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userName", "admin");
    params.put("userPass", "admin");
    params.put("authMethod", "form");
    params.put("urlBase", urlBase);
    params.put("authUrl", urlBase + "/secure/j_security_check");
    params.put("authUserName", "j_username");
    params.put("authUserPass", "j_password");
    params.put("urlSource", urlSource);
    tstAuthGet(url, params, cookieManager); //either Get or Post for test allowed
    urlSource = "http://localhost:8080/beige-accounting-web/secure/sendEntities";
    params.put("urlSource", urlSource);
    //url = new URL(urlSource);
    //tstAuthPost(url, params, cookieManager);
  }

  /**
   * <p>It authenticate by post simulate form.</p>
   * @param pAddParam additional params
   * @param pCookieManager CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void authForm(final Map<String, Object> pAddParam,
      final CookieManager pCookieManager) throws Exception {
    String authUrl = (String) pAddParam.get("authUrl");
    String authUserName = (String) pAddParam.get("authUserName");
    String authUserPass = (String) pAddParam.get("authUserPass");
    String userName = (String) pAddParam.get("userName");
    String userPass = (String) pAddParam.get("userPass");
    URL url = new URL(authUrl);
    HttpURLConnection urlConnection = (HttpURLConnection) url
      .openConnection();
    OutputStreamWriter writer = null;
    BufferedReader reader = null;
    try {
      urlConnection.setDoOutput(true);
      urlConnection.setRequestMethod("POST");
      String paramStr = authUserName + "=" + userName + "&"
        + authUserPass + "=" + userPass;
      String cookiesStr = "";
      for (HttpCookie cookie : pCookieManager.getCookieStore().getCookies()) {
        cookiesStr += cookie.getName() + "=" + cookie.getValue() +";";
      }
      urlConnection.addRequestProperty("Cookie", cookiesStr);
      urlConnection.addRequestProperty("Connection" , "keep-alive");
      urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConnection.addRequestProperty("Content-Length", String.valueOf(paramStr.length()));
      //urlConnection.addRequestProperty("User-Agent", Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36);
      //urlConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
      //urlConnection.addRequestProperty("Referer", "http://localhost:8080/beige-accounting-web/secure/main.jsp");
      //urlConnection.addRequestProperty("Origin", "http://localhost:8080");
      //urlConnection.addRequestProperty("Host", "localhost:8080");
      //urlConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
      //urlConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6");
      //urlConnection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
      //urlConnection.addRequestProperty("Cache-Control", "max-age=0");
      this.logger.info(null, HttpURLConnectionTest.class,
        "Request before flush auth:");
      for (Map.Entry<String,List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.info(null, HttpURLConnectionTest.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "   Request entry value: " + val);
        }
      }
      writer = new OutputStreamWriter(urlConnection
        .getOutputStream(), Charset.forName("UTF-8").newEncoder());
      writer.write(paramStr);
      writer.flush();
      this.logger.info(null, HttpURLConnectionTest.class,
        "send aparms: " + paramStr);
      //j_username=admin&j_password=admin - length=33
      reader = new BufferedReader(new InputStreamReader(urlConnection
          .getInputStream(), Charset.forName("UTF-8").newDecoder()));
      while (reader.read() != -1) { //NOPMD
        //just read out
      }
//      this.utilXml.readUntilStart(reader, "kkkkkkkkkkkkklok"); //just read out
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      urlConnection.disconnect();
    }
  }

  /**
   * <p>It try to authenticate trough form meth GET.</p>
   * @param pUrl URL
   * @param pAddParam additional params
   * @param pCookieManager CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void tstAuthGet(final URL pUrl,
    final Map<String, Object> pAddParam,
      final CookieManager pCookieManager) throws Exception {
    HttpURLConnection urlConnection = (HttpURLConnection) pUrl
      .openConnection();
    BufferedReader reader = null;
    try {
      urlConnection.setRequestMethod("GET");
      urlConnection.addRequestProperty("Connection" , "keep-alive");
      //urlConnection.addRequestProperty("Referer", "http://localhost:8080/beige-accounting-web/secure/main.jsp");
      //urlConnection.addRequestProperty("Host", "localhost:8080");
      //urlConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
      //urlConnection.addRequestProperty("User-Agent", Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36);
      //urlConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
      //urlConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6");
      //urlConnection.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
      //urlConnection.addRequestProperty("Cache-Control", "max-age=0");
      this.logger.info(null, HttpURLConnectionTest.class,
        "Request tstAuthGet:");
      for (Map.Entry<String,List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.info(null, HttpURLConnectionTest.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "    Request entry value: " + val);
        }
      }
      if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
        reader = new BufferedReader(new InputStreamReader(urlConnection
            .getInputStream(), Charset.forName("UTF-8").newDecoder()));
        while (reader.read() != -1) { //NOPMD
          //just read out
        }
       /* boolean isAuthForm = this.utilXml.readUntilStart(reader, "form");
        assertTrue(isAuthForm);
        this.utilXml.readUntilStart(reader, "kkkkkkkkkkkkklok"); //just read out
        for (HttpCookie cookie : pCookieManager.getCookieStore().getCookies()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "tstAuthGet after read: " + cookie.getName() + " - " + cookie.getValue());
        }*/
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "tstAuthGet Can't receive data!!! Response code=" + urlConnection
            .getResponseCode());
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      urlConnection.disconnect();
    }
    authForm(pAddParam, pCookieManager);
    tryAgainGet(pUrl, pAddParam, pCookieManager);
  }

  /**
   * <p>It try again after authenticate trough form GET.</p>
   * @param pUrl URL
   * @param pAddParam additional params
   * @param pCookieManager CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void tryAgainGet(final URL pUrl,
    final Map<String, Object> pAddParam,
      final CookieManager pCookieManager) throws Exception {
    HttpURLConnection urlConnection = (HttpURLConnection) pUrl
      .openConnection();
    BufferedReader reader = null;
    try {
      urlConnection.setRequestMethod("GET");
      String cookiesStr = "";
      for (HttpCookie cookie : pCookieManager.getCookieStore().getCookies()) {
        cookiesStr += cookie.getName() + "=" + cookie.getValue() +";";
      }
      urlConnection.addRequestProperty("Cookie", cookiesStr);
      urlConnection.addRequestProperty("Connection" , "keep-alive");
      //urlConnection.addRequestProperty("Referer", "http://localhost:8080/beige-accounting-web/secure/main.jsp");
      //urlConnection.addRequestProperty("Host", "localhost:8080");
      //urlConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
      //urlConnection.addRequestProperty("User-Agent", Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36);
      //urlConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
      //urlConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6");
      //urlConnection.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
      //urlConnection.addRequestProperty("Cache-Control", "max-age=0");
      this.logger.info(null, HttpURLConnectionTest.class,
        "Request try again:");
      for (Map.Entry<String,List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.info(null, HttpURLConnectionTest.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "    Request entry value: " + val);
        }
      }
      if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
        reader = new BufferedReader(new InputStreamReader(urlConnection
            .getInputStream(), Charset.forName("UTF-8").newDecoder()));
        boolean isDialog = this.utilXml.readUntilStart(reader, "dialog");
        assertTrue(isDialog);
        this.utilXml.readUntilStart(reader, "jjjjjjjjkoyjjjjjjjjj"); //just read out
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "Try again Can't receive data!!! Response code=" + urlConnection
            .getResponseCode());
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      urlConnection.disconnect();
    }
  }

  /**
   * <p>It try to authenticate trough form meth Post.</p>
   * @param pUrl URL
   * @param pAddParam additional params
   * @param pCookieManager CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void tstAuthPost(final URL pUrl,
    final Map<String, Object> pAddParam,
      final CookieManager pCookieManager) throws Exception {
    HttpURLConnection urlConnection = (HttpURLConnection) pUrl
      .openConnection();
    OutputStreamWriter writer = null;
    BufferedReader reader = null;
    try {
      urlConnection.setRequestMethod("POST");
      urlConnection.addRequestProperty("Connection" , "keep-alive");
      urlConnection.setDoOutput(true);
      this.logger.info(null, HttpURLConnectionTest.class,
        "Request tstAuthPost:");
      for (Map.Entry<String,List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.info(null, HttpURLConnectionTest.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "    Request entry value: " + val);
        }
      }
      writer = new OutputStreamWriter(urlConnection
        .getOutputStream(), Charset.forName("UTF-8").newEncoder());
      writer.write("entityName=org.beigesoft.accounting.persistable.InvItem&maxRecords=100&firstRecord=0");
      writer.flush();
      if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
        reader = new BufferedReader(new InputStreamReader(urlConnection
            .getInputStream(), Charset.forName("UTF-8").newDecoder()));
        boolean isAuthForm = this.utilXml.readUntilStart(reader, "form");
        assertTrue(isAuthForm);
        this.utilXml.readUntilStart(reader, "kkkkkkkkkkkkklok"); //just read out
        for (HttpCookie cookie : pCookieManager.getCookieStore().getCookies()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "tstAuthPost after read: " + cookie.getName() + " - " + cookie.getValue());
        }
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "tstAuthPost Can't receive data!!! Response code=" + urlConnection
            .getResponseCode());
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      urlConnection.disconnect();
    }
    authForm(pAddParam, pCookieManager);
    tryAgainPost(pUrl, pAddParam, pCookieManager);
  }

  /**
   * <p>It try  again after authenticate make Post.</p>
   * @param pUrl URL
   * @param pAddParam additional params
   * @param pCookieManager CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void tryAgainPost(final URL pUrl,
    final Map<String, Object> pAddParam,
      final CookieManager pCookieManager) throws Exception {
    HttpURLConnection urlConnection = (HttpURLConnection) pUrl
      .openConnection();
    OutputStreamWriter writer = null;
    BufferedReader reader = null;
    try {
      urlConnection.setRequestMethod("POST");
      urlConnection.addRequestProperty("Connection" , "keep-alive");
      urlConnection.setDoOutput(true);
      this.logger.info(null, HttpURLConnectionTest.class,
        "Request tryAgainPost:");
      for (Map.Entry<String,List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.info(null, HttpURLConnectionTest.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "    Request entry value: " + val);
        }
      }
      writer = new OutputStreamWriter(urlConnection
        .getOutputStream(), Charset.forName("UTF-8").newEncoder());
      writer.write("entityName=org.beigesoft.accounting.persistable.InvItem&maxRecords=100&firstRecord=0");
      writer.flush();
      if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
        reader = new BufferedReader(new InputStreamReader(urlConnection
            .getInputStream(), Charset.forName("UTF-8").newDecoder()));
        boolean isMessage = this.utilXml.readUntilStart(reader, "message");
        assertTrue(isMessage);
        this.utilXml.readUntilStart(reader, "kkkkkkkkkkkkklok"); //just read out
        for (HttpCookie cookie : pCookieManager.getCookieStore().getCookies()) {
          this.logger.info(null, HttpURLConnectionTest.class,
            "tryAgainPost after read: " + cookie.getName() + " - " + cookie.getValue());
        }
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "tryAgainPost Can't receive data!!! Response code=" + urlConnection
            .getResponseCode());
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      urlConnection.disconnect();
    }
  }
}
