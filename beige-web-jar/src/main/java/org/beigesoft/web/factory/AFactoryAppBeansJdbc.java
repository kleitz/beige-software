package org.beigesoft.web.factory;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.sql.ResultSet;

import javax.sql.DataSource;

import org.beigesoft.log.LoggerStandard;
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
    LoggerStandard logger = (LoggerStandard) getBeansMap().get(beanName);
    if (logger == null) {
      logger = new LoggerStandard();
      logger.setIsShowDebugMessages(getIsShowDebugMessages());
      getBeansMap().put(beanName, logger);
      lazyGetLogger().info(AFactoryAppBeansJdbc.class, beanName
        + " has been created.");
    }
    return logger;
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
      lazyGetLogger().info(AFactoryAppBeansJdbc.class, beanName
        + " has been created.");
    }
    return srvDatabase;
  }
}
