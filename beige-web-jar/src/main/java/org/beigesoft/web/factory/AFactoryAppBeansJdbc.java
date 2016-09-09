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
import org.beigesoft.jdbc.service.SrvRecordRetriever;

/**
 * <p>
 * JDBC based Application beans factory.
 * </p>
 *
 * @author Yury Demidenko
 */
public abstract class AFactoryAppBeansJdbc
  extends AFactoryAppBeans<ResultSet> {

  /**
   * <p>Logger.</p>
   */
  private ILogger logger;

  /**
   * <p>Record retriever service.</p>
   **/
  private SrvRecordRetriever srvRecordRetriever;

  /**
   * <p>Database service.</p>
   **/
  private SrvDatabase srvDatabase;

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
   * <p>Get other bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGetOtherBean(
    final String pBeanName) throws Exception {
    if ("DataSource".equals(pBeanName)) {
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
    if (this.logger == null) {
      this.logger = new LoggerStandard();
      this.logger.setIsShowDebugMessages(getIsShowDebugMessages());
      lazyGetLogger().info(AFactoryAppBeans.class,
        "LoggerStandard has been created.");
    }
    return this.logger;
  }

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return SrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  @Override
  public final synchronized SrvDatabase lazyGetSrvDatabase() throws Exception {
    if (this.srvDatabase == null) {
      this.srvDatabase = new SrvDatabase();
      this.srvDatabase.setLogger(lazyGetLogger());
      this.srvDatabase.setHlpInsertUpdate(lazyGetHlpInsertUpdate());
      this.srvDatabase.setDataSource(lazyGetDataSource());
      this.srvDatabase.setSrvRecordRetriever(lazyGetSrvRecordRetriever());
      lazyGetLogger().info(AFactoryAppBeans.class,
        "SrvDatabase has been created.");
    }
    return this.srvDatabase;
  }

  /**
   * <p>Get SrvRecordRetriever in lazy mode.</p>
   * @return SrvRecordRetriever - SrvRecordRetriever
   * @throws Exception - an exception
   */
  @Override
  public final synchronized SrvRecordRetriever
    lazyGetSrvRecordRetriever() throws Exception {
    if (this.srvRecordRetriever == null) {
      this.srvRecordRetriever = new SrvRecordRetriever();
      lazyGetLogger().info(AFactoryAppBeans.class,
        "SrvRecordRetriever has been created.");
    }
    return this.srvRecordRetriever;
  }

  //Simple setters to replace services during runtime:
  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final synchronized void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }
  /**
   * <p>Setter for srvRecordRetriever.</p>
   * @param pSrvRecordRetriever reference
   **/
  public final synchronized void setSrvRecordRetriever(
    final SrvRecordRetriever pSrvRecordRetriever) {
    this.srvRecordRetriever = pSrvRecordRetriever;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final synchronized void setSrvDatabase(
    final SrvDatabase pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }
}
