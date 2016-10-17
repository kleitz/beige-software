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

import com.zaxxer.hikari.HikariDataSource;

import org.beigesoft.replicator.service.PrepareDbAfterGetCopy;
import org.beigesoft.orm.service.SrvOrmSqlite;
import org.beigesoft.web.service.MngDatabaseSqlite;

/**
 * <p>Application beans factory for Sqlite RDBMS.
 * Cause don't use IOC.
 * Property dataSourceJndiName must be settled!
 * </p>
 *
 * @author Yury Demidenko
 */
public class FactoryAppBeansSqlite extends AFactoryAppBeansJdbc {

  /**
   * <p>Service that  release AppFactory beans.</p>
   */
  private PrepareDbAfterGetCopy prepareDbAfterGetCopy;

  /**
   * <p>Data Source.</p>
   */
  private HikariDataSource dataSource;

  /**
   * <p>Database manager for SQLite.</p>
   */
  private MngDatabaseSqlite mngDatabaseSqlite;

  /**
   * <p>Release beans (memory). This is "memory friendly" factory</p>
   * @throws Exception - an exception
   */
  public final synchronized void releaseBeans() throws Exception {
    if (getFactoryOverBeans() != null) {
      try {
        getFactoryOverBeans().releaseBeans();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (this.dataSource != null) {
      try {
        this.dataSource.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      this.dataSource = null;
    }
    this.prepareDbAfterGetCopy = null;
    this.mngDatabaseSqlite = null;
    setLogger(null);
    setSrvDatabase(null);
    setSrvRecordRetriever(null);
    setUtlReflection(null);
    setUtlProperties(null);
    setUtlJsp(null);
    setSrvI18n(null);
    setMngUvdSettings(null);
    setSrvWebEntity(null);
    setSrvPage(null);
    setMngSoftware(null);
    setSrvOrm(null);
    setSrvWebMvc(null);
    setHlpInsertUpdate(null);
    setClearDbThenGetAnotherCopyXmlHttp(null);
    setDatabaseWriter(null);
    setMngSettingsGetDbCopy(null);
    setUtilXml(null);
    getEntitiesMap().clear();
    getBeansMap().clear();
  }

  /**
   * <p>Get other RDBMS specific bean in lazy mode
   * (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGetOtherRdbmsBean(
    final String pBeanName) throws Exception {
    if ("IMngDatabase".equals(pBeanName)) {
      return lazyGetMngDatabaseSqlite();
    } else if ("prepareDbAfterGetAnotherCopy".equals(pBeanName)) {
      return lazyGetPrepareDbAfterGetCopy();
    }
    return null;
  }

  /**
   * <p>Instantiate ORM  service.</p>
   * @return SrvOrmSqlite - ORM  service
   */
  public final synchronized SrvOrmSqlite<ResultSet> instantiateSrvOrm() {
    return new SrvOrmSqlite<ResultSet>();
  }

  /**
   * <p>Get DataSource in lazy mode.</p>
   * @return DataSource - DataSource
   * @throws Exception - an exception
   */
  @Override
  public final synchronized DataSource lazyGetDataSource() throws Exception {
    if (this.dataSource == null) {
      this.dataSource = new HikariDataSource();
      this.dataSource.setJdbcUrl(getDatabaseName());
      this.dataSource.setDriverClassName("org.sqlite.JDBC");
      lazyGetLogger().info(FactoryAppBeansSqlite.class,
        "HikariDataSource has been created.");
    }
    return this.dataSource;
  }

  /**
   * <p>Get MngDatabaseSqlite in lazy mode.</p>
   * @return MngDatabaseSqlite - MngDatabaseSqlite
   * @throws Exception - an exception
   */
  public final synchronized MngDatabaseSqlite
    lazyGetMngDatabaseSqlite() throws Exception {
    if (this.mngDatabaseSqlite == null) {
      this.mngDatabaseSqlite = new MngDatabaseSqlite();
      this.mngDatabaseSqlite.setFactoryAppBeansSqlite(this);
      lazyGetLogger().info(FactoryAppBeansSqlite.class,
        "MngDatabaseSqlite has been created.");
    }
    return this.mngDatabaseSqlite;
  }

  /**
   * <p>Get PrepareDbAfterGetCopy in lazy mode.</p>
   * @return PrepareDbAfterGetCopy - PrepareDbAfterGetCopy
   * @throws Exception - an exception
   */
  public final synchronized PrepareDbAfterGetCopy
    lazyGetPrepareDbAfterGetCopy() throws Exception {
    if (this.prepareDbAfterGetCopy == null) {
      this.prepareDbAfterGetCopy = new PrepareDbAfterGetCopy();
      this.prepareDbAfterGetCopy.setLogger(lazyGetLogger());
      this.prepareDbAfterGetCopy.setFactoryAppBeans(this);
      lazyGetLogger().info(FactoryAppBeansSqlite.class,
        "PrepareDbAfterGetCopy has been created.");
    }
    return this.prepareDbAfterGetCopy;
  }

  /**
   * <p>Getter for dataSource.</p>
   * @return HikariDataSource
   **/
  public final synchronized HikariDataSource getDataSource() {
    return this.dataSource;
  }

  /**
   * <p>Setter for dataSource.</p>
   * @param pDataSource reference
   **/
  public final synchronized void setDataSource(
    final HikariDataSource pDataSource) {
    this.dataSource = pDataSource;
  }
}
