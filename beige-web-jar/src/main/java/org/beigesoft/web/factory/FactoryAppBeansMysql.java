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
import java.sql.ResultSet;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import org.beigesoft.orm.service.SrvOrmMysql;
import org.beigesoft.replicator.service.PrepareDbAfterGetCopy;

/**
 * <p>Application beans factory for Mysql RDBMS.
 * Cause don't use IOC.
 * Property dataSourceJndiName must be settled!
 * </p>
 *
 * @author Yury Demidenko
 */
public class FactoryAppBeansMysql extends AFactoryAppBeansJdbc {

  /**
   * <p>Service that reset auto-incremented ID sequences (APersistableBase)
   * after identical copy of another database
   * and release AppFactory beans.</p>
   */
  private PrepareDbAfterGetCopy prepareDbAfterGetCopyMysql;

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
    this.prepareDbAfterGetCopyMysql = null;
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
   * @return SrvOrmMysql - ORM  service
   */
  @Override
  public final synchronized SrvOrmMysql<ResultSet> instantiateSrvOrm() {
    return new SrvOrmMysql<ResultSet>();
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
      props.setProperty("dataSourceClassName",
        "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
      props.setProperty("dataSource.user", getDatabaseUser());
      props.setProperty("dataSource.password", getDatabasePassword());
      props.setProperty("dataSource.url", getDatabaseName());
      HikariConfig config = new HikariConfig(props);
      this.dataSource = new HikariDataSource(config);
      lazyGetLogger().info(FactoryAppBeansMysql.class,
        "DataSource has been created.");
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
    if (this.prepareDbAfterGetCopyMysql == null) {
      this.prepareDbAfterGetCopyMysql =
        new PrepareDbAfterGetCopy();
      this.prepareDbAfterGetCopyMysql.setLogger(lazyGetLogger());
      this.prepareDbAfterGetCopyMysql.setFactoryAppBeans(this);
      lazyGetLogger().info(FactoryAppBeansMysql.class,
        "PrepareDbAfterGetCopy has been created.");
    }
    return this.prepareDbAfterGetCopyMysql;
  }
}
