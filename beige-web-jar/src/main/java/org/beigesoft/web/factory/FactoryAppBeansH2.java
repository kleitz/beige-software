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

import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import org.beigesoft.replicator.service.PrepareDbAfterGetCopy;
import org.beigesoft.orm.service.SrvOrmH2;
import java.sql.ResultSet;

/**
 * <p>Application beans factory for H2 RDBMS.
 * Cause don't use IOC.
 * Property dataSourceJndiName must be settled!
 * </p>
 *
 * @author Yury Demidenko
 */
public class FactoryAppBeansH2 extends AFactoryAppBeansJdbc {

  /**
   * <p>Service that  release AppFactory beans.</p>
   */
  private PrepareDbAfterGetCopy prepareDbAfterGetCopy;

  /**
   * <p>Data Source.</p>
   */
  private HikariDataSource dataSource;

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
    setLogger(null);
    setSrvRecordRetriever(null);
    setSrvDatabase(null);
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
    return null;
  }


  /**
   * <p>Instantiate ORM  service.</p>
   * @return SrvOrmH2 - ORM  service
   */
  @Override
  public final synchronized SrvOrmH2<ResultSet> instantiateSrvOrm() {
    return new SrvOrmH2<ResultSet>();
  }

  /**
   * <p>Get DataSource in lazy mode.</p>
   * @return DataSource - DataSource
   * @throws Exception - an exception
   */
  @Override
  public final synchronized DataSource lazyGetDataSource() throws Exception {
    if (this.dataSource == null) {
      Properties props = new Properties();
      props.setProperty("dataSourceClassName", "org.h2.jdbcx.JdbcDataSource");
      props.setProperty("dataSource.user", getDatabaseUser());
      props.setProperty("dataSource.password", getDatabasePassword());
      props.setProperty("dataSource.Url", getDatabaseName());
      HikariConfig config = new HikariConfig(props);
      this.dataSource = new HikariDataSource(config);
      lazyGetLogger().info(FactoryAppBeansH2.class,
        "HikariDataSource has been created.");
    }
    return this.dataSource;
  }

  /**
   * <p>Get Service that prepare Database after full import
   * in lazy mode.</p>
   * @return IDelegator - preparator Database after full import.
   * @throws Exception - an exception
   */
  @Override
  public final synchronized PrepareDbAfterGetCopy
    lazyGetPrepareDbAfterFullImport() throws Exception {
    if (this.prepareDbAfterGetCopy == null) {
      this.prepareDbAfterGetCopy = new PrepareDbAfterGetCopy();
      this.prepareDbAfterGetCopy.setLogger(lazyGetLogger());
      this.prepareDbAfterGetCopy.setFactoryAppBeans(this);
      lazyGetLogger().info(FactoryAppBeansH2.class,
        "PrepareDbAfterGetCopy has been created.");
    }
    return this.prepareDbAfterGetCopy;
  }

  //Simple setters to replace services during runtime:
  /**
   * <p>Setter for dataSource.</p>
   * @param pDataSource reference
   **/
  public final synchronized void setDataSource(
    final HikariDataSource pDataSource) {
    this.dataSource = pDataSource;
  }
}
