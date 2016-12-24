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
import java.util.Date;
import java.io.Writer;
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
import org.beigesoft.replicator.filter.IFilterEntities;
import org.beigesoft.delegate.IDelegator;
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
public class ReplicatorXmlHttp<RS>
  implements IReplicator {

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
   * <p>It prepare database before import (may be null),
   * e.g. for full database copy it should clear database.</p>
   **/
  private IDelegator databasePrepearerBefore;

  /**
   * <p>It prepare database after import (may be null),
   * e.g. for full database copy it should release AppBeansFactory
   * and for Postgresql it should remake sequences.</p>
   **/
  private IDelegator databasePrepearerAfter;

  /**
   * <p>XML service.</p>
   **/
  private IUtilXml utilXml;

  /**
   * <p>Filters Entity Sync map.</p>
   **/
  private Map<String, IFilterEntities> filtersEntities;

  /**
   * <p>It will clear current database then copy
   * data from another with XML messages trough HTTP connection.</p>
   * @param pAddParams additional params
   * @throws Exception - an exception
   **/
  @Override
  public final void replicate(
    final Map<String, Object> pAddParams) throws Exception {
    Writer htmlWriter = (Writer) pAddParams.get("htmlWriter");
    try {
      //URL must be
      String urlSourceStr = (String) pAddParams.get("urlSource");
      if (urlSourceStr == null || urlSourceStr.length() < 10) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "Where is no urlSource!!!");
      }
      URL url = new URL(urlSourceStr);
      String authMethod = (String) pAddParams.get("authMethod");
      if ("base".equals(authMethod)) {
        final String userName = (String) pAddParams.get("userName");
        final String userPass = (String) pAddParams.get("userPass");
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
        requestCookiesGet(pAddParams);
        authForm(pAddParams, cookieManager);
      }
      Map<String, Integer> classesCounts = makeJob(url, pAddParams);
      if (htmlWriter != null) {
        String statusString = ", replication has been done.";
        htmlWriter.write("<h4>" + new Date().toString()
          + statusString + "</h4>");
        pAddParams.put("statusString", new Date().toString() + ", "
          + ReplicatorXmlHttp.class.getSimpleName()
          + statusString);
        this.logger.info(ReplicatorXmlHttp.class, statusString);
        htmlWriter.write("<table>");
        htmlWriter.write("<tr><th style=\"padding: 5px;\">Class</th><th style=\"padding: 5px;\">Total records</th></tr>");
        for (Map.Entry<String, Integer> entry : classesCounts.entrySet()) {
          htmlWriter.write("<tr>");
          htmlWriter.write("<td>" + entry.getKey() + "</td>");
          htmlWriter.write("<td>" + entry.getValue() + "</td>");
          htmlWriter.write("</tr>");
        }
        htmlWriter.write("</table>");
      }
    } catch (ExceptionWithCode ex) {
      if (htmlWriter != null) {
        htmlWriter.write(new Date().toString() + ", "
        + ReplicatorXmlHttp.class.getSimpleName()
          + ", " + ex.getShortMessage());
      }
      this.logger.error(ReplicatorXmlHttp.class,
        ex.getShortMessage());
      throw ex;
    }
  }

  /**
   * <p>It copy data from another with XML messages
   * through given HTTP connection.</p>
   * @param pUrl URL
   * @param pAddParams additional params
   * @return Map<String, Integer> affected Class - records count
   * @throws Exception - an exception
   **/
  public final Map<String, Integer> makeJob(final URL pUrl,
    final Map<String, Object> pAddParams) throws Exception {
    String requestedDatabaseId = (String) pAddParams.get("requestedDatabaseId");
    String maxRecordsStr = (String) pAddParams.get("maxRecords");
    if (maxRecordsStr == null || maxRecordsStr.length() == 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "Where is no maxRecords!!!");
    }
    int maxRecords = Integer.parseInt(maxRecordsStr);
    Map<String, Integer> classesCounts = new LinkedHashMap<String, Integer>();
    Integer classCount = 0;
    boolean isDbPreparedBefore = false;
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
          String nameFilterEntities = this.mngSettings.getClassesSettings()
            .get(entityClass.getCanonicalName()).get("filter");
          String conditions = "";
          if (nameFilterEntities != null) {
            IFilterEntities filterEntities = this.filtersEntities
              .get(nameFilterEntities);
            if (filterEntities != null) {
              String cond = filterEntities.makeFilter(entityClass, pAddParams);
              if (cond != null) {
                conditions = " where " + cond;
              }
            }
          }
          conditions += " limit " + maxRecords + " offset " + firstRecord;
          String requestedDatabaseIdStr = "";
          if (requestedDatabaseId != null) {
            if (Integer.parseInt(requestedDatabaseId)
              == getSrvDatabase().getIdDatabase()) {
              throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
                "requested_database_must_be_different");
            }
            requestedDatabaseIdStr = "&requestedDatabaseId="
                + requestedDatabaseId;
          }
          writer.write("entityName=" + entityClass.getCanonicalName()
            + "&conditions=" + conditions + "&requestingDatabaseVersion="
              + databaseVersion + requestedDatabaseIdStr);
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
              this.logger.info(ReplicatorXmlHttp.class,
                "Try to parse entities total: " + entitiesReceived + " of "
                  + entityClass.getCanonicalName());
              if (!isDbPreparedBefore) {
                if (this.databasePrepearerBefore != null) {
                  this.databasePrepearerBefore.make(pAddParams);
                }
                isDbPreparedBefore = true;
              }
              this.databaseReader.readAndStoreEntities(reader, pAddParams);
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
    if (this.databasePrepearerAfter != null) {
      this.databasePrepearerAfter.make(pAddParams);
    }
    return classesCounts;
  }

  /**
   * <p>Connect to secure address with method GET to receive
   * authenticate cookies.</p>
   * @param pAddParams additional params
   * @throws Exception - an exception
   **/
  public final void requestCookiesGet(
    final Map<String, Object> pAddParams) throws Exception {
    String urlGetAuthCookStr = (String) pAddParams.get("urlGetAuthCookies");
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
   * @param pAddParams additional params
   * @param pCookieManager CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void authForm(final Map<String, Object> pAddParams,
      final CookieManager pCookieManager) throws Exception {
    String authUrl = (String) pAddParams.get("authUrl");
    String authUserName = (String) pAddParams.get("authUserName");
    String authUserPass = (String) pAddParams.get("authUserPass");
    String userName = (String) pAddParams.get("userName");
    String userPass = (String) pAddParams.get("userPass");
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
      this.logger.debug(ReplicatorXmlHttp.class,
        "Request before flush auth:");
      for (Map.Entry<String, List<String>> entry
        : urlConnection.getRequestProperties().entrySet()) {
        this.logger.debug(ReplicatorXmlHttp.class,
          "  Request entry key: " + entry.getKey());
        for (String val : entry.getValue()) {
          this.logger.debug(ReplicatorXmlHttp.class,
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
   * <p>Getter for databasePrepearerBefore.</p>
   * @return IDelegator
   **/
  public final IDelegator getDatabasePrepearerBefore() {
    return this.databasePrepearerBefore;
  }

  /**
   * <p>Setter for databasePrepearerBefore.</p>
   * @param pDatabasePrepearerBefore reference
   **/
  public final void setDatabasePrepearerBefore(
    final IDelegator pDatabasePrepearerBefore) {
    this.databasePrepearerBefore = pDatabasePrepearerBefore;
  }

  /**
   * <p>Getter for databasePrepearerAfter.</p>
   * @return IDelegator
   **/
  public final IDelegator getDatabasePrepearerAfter() {
    return this.databasePrepearerAfter;
  }

  /**
   * <p>Setter for databasePrepearerAfter.</p>
   * @param pDatabasePrepearerAfter reference
   **/
  public final void setDatabasePrepearerAfter(
    final IDelegator pDatabasePrepearerAfter) {
    this.databasePrepearerAfter = pDatabasePrepearerAfter;
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

  /**
   * <p>Getter for filtersEntities.</p>
   * @return Map<String, IFilterEntities>
   **/
  public final Map<String, IFilterEntities> getFiltersEntities() {
    return this.filtersEntities;
  }

  /**
   * <p>Setter for filtersEntities.</p>
   * @param pFiltersEntities reference
   **/
  public final void setFiltersEntities(
    final Map<String, IFilterEntities> pFiltersEntities) {
    this.filtersEntities = pFiltersEntities;
  }
}
