package org.beigesoft.web.factory;

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

import java.util.Properties;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import java.sql.ResultSet;

import javax.sql.DataSource;

import org.beigesoft.log.LoggerFile;
import org.beigesoft.log.ILogger;
import org.beigesoft.jdbc.service.SrvDatabase;

/**
 * <p>
 * JDBC based Application beans factory.
 * </p>
 *
 * @author Yury Demidenko
 */
public abstract class AFactoryAppBeansJdbc
  extends AFactoryAppBeans<ResultSet> {

  //To override:
  /**
   * <p>Get other RDBMS specific bean in lazy mode
   * (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  public abstract Object lazyGetOtherRdbmsBean(
    final String pBeanName) throws Exception;

  /**
   * <p>Get DataSource in lazy mode.</p>
   * @return DataSource - DataSource
   * @throws Exception - an exception
   */
  public abstract DataSource lazyGetDataSource() throws Exception;

  /**
   * <p>Getter of DataSource bean name.</p>
   * @return bean name
   **/
  public final String getDataSourceName() {
    return "DataSource";
  }

  /**
   * <p>Is need to SQL escape (character ').</p>
   * @return for Android false, JDBC - true.
   */
  @Override
  public final boolean getIsNeedsToSqlEscape() {
    return true;
  }

  /**
   * <p>Get other bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGetOtherBean(
    final String pBeanName) throws Exception {
    if (getDataSourceName().equals(pBeanName)) {
      return lazyGetDataSource();
    } else {
      return lazyGetOtherRdbmsBean(pBeanName);
    }
  }

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return SrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  @Override
  public final synchronized ILogger lazyGetLogger() throws Exception {
    String beanName = getLoggerName();
    ILogger log = (ILogger) getBeansMap().get(beanName);
    if (log == null) {
      LoggerFile logger = new LoggerFile();
      log = logger;
      String fileBaseName = "beige-accounting";
      logger.setFileMaxSize(10000000);
      logger.setFilePath(getWebAppPath() + File.separator + fileBaseName);
      System.out.println(getClass().getSimpleName() + "> Log file path: "
        + getWebAppPath() + File.separator + fileBaseName);
      String logPropFn = "/" + fileBaseName + ".properties";
      URL urlSetting = AFactoryAppBeansJdbc.class.getResource(logPropFn);
      if (urlSetting != null) {
        System.out.println(getClass().getSimpleName()
          + "> Found properties: " + logPropFn);
        InputStream inputStream = null;
        try {
          Properties props = new Properties();
          inputStream = AFactoryAppBeansJdbc.class
            .getResourceAsStream(logPropFn);
          props.load(inputStream);
          String fileMaxSizeStr = props.getProperty("fileMaxSize");
          if (fileMaxSizeStr != null) {
            Integer fileMaxSize = Integer.parseInt(fileMaxSizeStr);
            logger.setFileMaxSize(fileMaxSize);
          }
          String maxIdleTimeStr = props.getProperty("maxIdleTime");
          if (maxIdleTimeStr != null) {
            Integer maxIdleTime = Integer.parseInt(maxIdleTimeStr);
            logger.setMaxIdleTime(maxIdleTime);
          }
          String isCloseFileAfterRecordStr = props
            .getProperty("isCloseFileAfterRecord");
          if (isCloseFileAfterRecordStr != null) {
            Boolean isCloseFileAfterRecord = Boolean
              .valueOf(isCloseFileAfterRecordStr);
            logger.setIsCloseFileAfterRecord(isCloseFileAfterRecord);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        } finally {
          if (inputStream != null) {
            try {
              inputStream.close();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      } else {
        System.out.println(getClass().getSimpleName()
          + "> There is no properties: " + logPropFn);
      }
      logger.setIsShowDebugMessages(getIsShowDebugMessages());
      getBeansMap().put(beanName, logger);
      logger.info(null, AFactoryAppBeansJdbc.class, beanName
        + " has been created.");
    }
    return log;
  }

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return SrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  @Override
  public final synchronized SrvDatabase lazyGetSrvDatabase() throws Exception {
    String beanName = getSrvDatabaseName();
    SrvDatabase srvDatabase =
      (SrvDatabase) getBeansMap().get(beanName);
    if (srvDatabase == null) {
      srvDatabase = new SrvDatabase();
      srvDatabase.setLogger(lazyGetLogger());
      srvDatabase.setHlpInsertUpdate(lazyGetHlpInsertUpdate());
      srvDatabase.setDataSource(lazyGetDataSource());
      getBeansMap().put(beanName, srvDatabase);
      lazyGetLogger().info(null, AFactoryAppBeansJdbc.class, beanName
        + " has been created.");
    }
    return srvDatabase;
  }
}
