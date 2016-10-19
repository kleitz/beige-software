package org.beigesoft.replicator.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
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

import org.beigesoft.log.ILogger;
import org.beigesoft.service.IUtilXml;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Service that clear database
 * then get identical copy of another one
 * (exclude tables with authentication User/Role/Tomcat/Jetty)
 * with XML messages trough HTTP connection.
 * It's support HTTP-base and form authentication.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class ClearDbThenGetAnotherCopyXmlHttp<RS>
  implements IClearDbThenGetAnotherCopy {

  /**
   * <p>Replicators settings manager.</p>
   **/
  private IMngSettings mngSettings;

  /**
   * <p>Entities reader from XML service.</p>
   **/
  private ISrvEntityReader srvEntityReaderXml;

  /**
   * <p>Database entities reader service.</p>
   **/
  private IDatabaseReader databaseReader;

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>Cookies.</p>
   **/
  private String cookies;

  /**
   * <p>XML service.</p>
   **/
  private IUtilXml utilXml;

  /**
   * <p>It will clear current database then copy
   * data from another with XML messages trough HTTP connection.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void clearDbThenGetAnotherCopy(
    final Map<String, Object> pAddParam) throws Exception {
    try {
      //URL must be
      String urlSourceStr = (String) pAddParam.get("urlSource");
      if (urlSourceStr == null || urlSourceStr.length() < 10) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Where is no urlSource!!!");
      }
      URL url = new URL(urlSourceStr);
      String authMethod = (String) pAddParam.get("authMethod");
      if ("base".equals(authMethod)) {
        final String userName = (String) pAddParam.get("userName");
        final String userPass = (String) pAddParam.get("userPass");
        Authenticator.setDefault(new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, userPass.toCharArray());
          }
        });
      } else if ("form".equals(authMethod)) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        requestCookiesGet(pAddParam);
        authForm(pAddParam, cookieManager);
      }
      makeJob(url, pAddParam);
      String statusString =
        ", Database has been emptied and data has been imported.";
      pAddParam.put("statusString", new Date().toString() + ", "
        + ClearDbThenGetAnotherCopyXmlHttp.class.getSimpleName()
          + statusString);
      this.logger.info(ClearDbThenGetAnotherCopyXmlHttp.class, statusString);
    } catch (ExceptionWithCode ex) {
      pAddParam.put("statusString", new Date().toString() + ", "
        + ClearDbThenGetAnotherCopyXmlHttp.class.getSimpleName()
          + ", " + ex.getShortMessage());
      this.logger.info(ClearDbThenGetAnotherCopyXmlHttp.class,
        ex.getShortMessage());
    }
  }

  /**
   * <p>It will clear current database.</p>
   * @throws Exception - an exception
   **/
  public final void clearDB() throws Exception {
    ArrayList<Class<?>> classesArr =
      new ArrayList<Class<?>>(this.mngSettings.getClasses());
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      for (int i = classesArr.size() - 1; i >= 0; i--) {
        Class<?> entityClass = classesArr.get(i);
        this.srvDatabase.executeDelete(entityClass.getSimpleName(), null);
      }
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>It copy data from another with XML messages
   * through given HTTP connection.</p>
   * @param pUrl URL
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  public final void makeJob(final URL pUrl,
    final Map<String, Object> pAddParam) throws Exception {
    String maxRecordsStr = (String) pAddParam.get("maxRecords");
    if (maxRecordsStr == null || maxRecordsStr.length() == 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "Where is no maxRecords!!!");
    }
    int maxRecords = Integer.parseInt(maxRecordsStr);
    Map<String, Integer> classesCounts = new LinkedHashMap<String, Integer>();
    pAddParam.put("classesCounts", classesCounts);
    Integer classCount = 0;
    boolean isCleared = false;
    int databaseVersion = this.srvDatabase.getVersionDatabase();
    for (Class<?> entityClass : this.mngSettings.getClasses()) {
      int entitiesReceived = 0;
      int firstRecord = 0;
      do {
        // HttpURLConnection is single request connection
        HttpURLConnection urlConnection = (HttpURLConnection) pUrl
          .openConnection();
        if (!pUrl.getHost().equals(urlConnection.getURL().getHost())) {
          throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
            "You should sign-in in browser first!");
        }
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
          urlConnection.setDoOutput(true);
          urlConnection.setRequestMethod("POST");
          if (getCookies() != null) {
            urlConnection.addRequestProperty("Cookie", getCookies());
          }
          writer = new OutputStreamWriter(urlConnection
            .getOutputStream(), Charset.forName("UTF-8").newEncoder());
          writer.write("entityName=" + entityClass.getCanonicalName()
            + "&maxRecords=" + maxRecords + "&firstRecord="
              + firstRecord + "&requestingDatabaseVersion=" + databaseVersion);
          writer.flush();
          if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
            reader = new BufferedReader(new InputStreamReader(urlConnection
                .getInputStream(), Charset.forName("UTF-8").newDecoder()));
            if (!this.utilXml.readUntilStart(reader, "message")) {
              throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
                "Wrong XML response without message tag!!!");
            }
            Map<String, String> msgAttrsMap = this.srvEntityReaderXml.
              readAttributes(reader, null);
            String error = msgAttrsMap.get("error");
            if (error != null) {
              throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
                error);
            }
            String entitiesCountStr = msgAttrsMap.get("entitiesCount");
            if (entitiesCountStr == null) {
              throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
                "Wrong XML response without entitiesCount in message!!!");
            }
            entitiesReceived = Integer.parseInt(entitiesCountStr);
            if (entitiesReceived > 0) {
              classCount += entitiesReceived;
              this.logger.info(ClearDbThenGetAnotherCopyXmlHttp.class,
                "Try to parse entities total: " + entitiesReceived + " of "
                  + entityClass.getCanonicalName());
              if (!isCleared) {
                clearDB();
                isCleared = true;
              }
              this.databaseReader.readAndStoreEntities(reader, pAddParam);
              if (entitiesReceived == maxRecords) {
                firstRecord += maxRecords;
              } else {
                firstRecord = 0;
                entitiesReceived = 0;
              }
            } else {
              firstRecord = 0;
            }
          } else {
            throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
              "Can't receive data!!! Response code=" + urlConnection
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
      } while (entitiesReceived > 0);
      classesCounts.put(entityClass.getCanonicalName(), classCount);
      classCount = 0;
    }
  }

  /**
   * <p>Connect to secure address with method GET to receive
   * authenticate cookies.</p>
   * @param pAddParam additional params
   * @throws Exception - an exception
   **/
  public final void requestCookiesGet(
    final Map<String, Object> pAddParam) throws Exception {
    String urlGetAuthCookStr = (String) pAddParam.get("urlGetAuthCookies");
    URL urlGetAuthCookies = new URL(urlGetAuthCookStr);
    HttpURLConnection urlConnection = (HttpURLConnection) urlGetAuthCookies
      .openConnection();
    BufferedReader reader = null;
    try {
      urlConnection.setRequestMethod("GET");
      urlConnection.addRequestProperty("Connection", "keep-alive");
      if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
        reader = new BufferedReader(new InputStreamReader(urlConnection
          .getInputStream(), Charset.forName("UTF-8").newDecoder()));
        while (reader.read() != -1) { //NOPMD
          //just read out
        }
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "requestCookiesGet Can't receive data!!! Response code="
            + urlConnection.getResponseCode());
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
    if (!url.getHost().equals(urlConnection.getURL().getHost())) {
      throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
        "You should sign-in in browser first!");
    }
    OutputStreamWriter writer = null;
    BufferedReader reader = null;
    try {
      urlConnection.setDoOutput(true);
      urlConnection.setRequestMethod("POST");
      String paramStr = authUserName + "=" + userName + "&"
        + authUserPass + "=" + userPass;
      StringBuffer cookiesSb = new StringBuffer();
      for (HttpCookie cookie : pCookieManager.getCookieStore().getCookies()) {
        cookiesSb.append(cookie.getName() + "=" + cookie.getValue() + ";");
      }
      setCookies(cookiesSb.toString());
      urlConnection.addRequestProperty("Cookie", getCookies());
      urlConnection.addRequestProperty("Connection", "keep-alive");
      urlConnection.addRequestProperty("Content-Type",
        "application/x-www-form-urlencoded");
      urlConnection.addRequestProperty("Content-Length",
        String.valueOf(paramStr.length()));
      this.logger.debug(ClearDbThenGetAnotherCopyXmlHttp.class,
        "Request before flush auth:");
      for (Map.Entry<String, List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.debug(ClearDbThenGetAnotherCopyXmlHttp.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.debug(ClearDbThenGetAnotherCopyXmlHttp.class,
            "   Request entry value: " + val);
        }
      }
      writer = new OutputStreamWriter(urlConnection
        .getOutputStream(), Charset.forName("UTF-8").newEncoder());
      writer.write(paramStr);
      writer.flush();
      reader = new BufferedReader(new InputStreamReader(urlConnection
          .getInputStream(), Charset.forName("UTF-8").newDecoder()));
      while (reader.read() != -1) { //NOPMD
        //just read out
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

  //Simple getters and setters:
  /**
   * <p>Getter for mngSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngSettings() {
    return this.mngSettings;
  }

  /**
   * <p>Setter for mngSettings.</p>
   * @param pMngSettings reference
   **/
  public final void setMngSettings(final IMngSettings pMngSettings) {
    this.mngSettings = pMngSettings;
  }

  /**
   * <p>Getter for srvEntityReaderXml.</p>
   * @return ISrvEntityReader
   **/
  public final ISrvEntityReader getSrvEntityReaderXml() {
    return this.srvEntityReaderXml;
  }

  /**
   * <p>Setter for srvEntityReaderXml.</p>
   * @param pSrvEntityReaderXml reference
   **/
  public final void setSrvEntityReaderXml(
    final ISrvEntityReader pSrvEntityReaderXml) {
    this.srvEntityReaderXml = pSrvEntityReaderXml;
  }

  /**
   * <p>Getter for databaseReader.</p>
   * @return IDatabaseReader
   **/
  public final IDatabaseReader getDatabaseReader() {
    return this.databaseReader;
  }

  /**
   * <p>Setter for databaseReader.</p>
   * @param pDatabaseReader reference
   **/
  public final void setDatabaseReader(final IDatabaseReader pDatabaseReader) {
    this.databaseReader = pDatabaseReader;
  }

  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Getter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for cookies.</p>
   * @return String
   **/
  public final String getCookies() {
    return this.cookies;
  }

  /**
   * <p>Setter for cookies.</p>
   * @param pCookies reference
   **/
  public final void setCookies(final String pCookies) {
    this.cookies = pCookies;
  }

  /**
   * <p>Getter for utilXml.</p>
   * @return IUtilXml
   **/
  public final IUtilXml getUtilXml() {
    return this.utilXml;
  }

  /**
   * <p>Setter for utilXml.</p>
   * @param pUtilXml reference
   **/
  public final void setUtilXml(final IUtilXml pUtilXml) {
    this.utilXml = pUtilXml;
  }
}
