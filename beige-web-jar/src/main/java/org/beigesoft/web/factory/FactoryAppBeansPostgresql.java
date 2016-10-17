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

import org.beigesoft.orm.service.SrvOrmPostgresql;
import org.beigesoft.replicator.service.PrepareDbAfterGetCopyPostgresql;

/**
 * <p>Application beans factory for Postgresql RDBMS.
 * Cause don't use IOC.
 * Property dataSourceJndiName must be settled!
 * </p>
 *
 * @author Yury Demidenko
 */
public class FactoryAppBeansPostgresql extends AFactoryAppBeansJdbc {

  /**
   * <p>Service that reset auto-incremented ID sequences (APersistableBase)
   * after identical copy of another database
   * and release AppFactory beans.</p>
   */
  private PrepareDbAfterGetCopyPostgresql<ResultSet>
    prepareDbAfterGetCopyPostgresql;

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
    this.prepareDbAfterGetCopyPostgresql = null;
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
    if ("prepareDbAfterGetAnotherCopy".equals(pBeanName)) {
      return lazyGetPrepareDbAfterGetCopyPostgresql();
    }
    return null;
  }

  /**
   * <p>Instantiate ORM  service.</p>
   * @return SrvOrmPostgresql - ORM  service
   */
  @Override
  public final synchronized SrvOrmPostgresql<ResultSet> instantiateSrvOrm() {
    return new SrvOrmPostgresql<ResultSet>();
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
        "org.postgresql.ds.PGSimpleDataSource");
      props.setProperty("dataSource.user", getDatabaseUser());
      props.setProperty("dataSource.password", getDatabasePassword());
      props.setProperty("dataSource.databaseName", getDatabaseName());
      HikariConfig config = new HikariConfig(props);
      this.dataSource = new HikariDataSource(config);
      lazyGetLogger().info(FactoryAppBeansPostgresql.class,
        "DataSource has been created.");
    }
    return this.dataSource;
  }

  /**
   * <p>Get PrepareDbAfterGetCopyPostgresql in lazy mode.</p>
   * @return PrepareDbAfterGetCopyPostgresql - PrepareDbAfterGetCopyPostgresql
   * @throws Exception - an exception
   */
  public final synchronized PrepareDbAfterGetCopyPostgresql<ResultSet>
    lazyGetPrepareDbAfterGetCopyPostgresql() throws Exception {
    if (this.prepareDbAfterGetCopyPostgresql == null) {
      this.prepareDbAfterGetCopyPostgresql =
        new PrepareDbAfterGetCopyPostgresql<ResultSet>();
      this.prepareDbAfterGetCopyPostgresql
        .setClasses(lazyGetMngSettingsGetDbCopy().getClasses());
      this.prepareDbAfterGetCopyPostgresql.setLogger(lazyGetLogger());
      this.prepareDbAfterGetCopyPostgresql.setSrvDatabase(lazyGetSrvDatabase());
      this.prepareDbAfterGetCopyPostgresql.setFactoryAppBeans(this);
      lazyGetLogger().info(FactoryAppBeansPostgresql.class,
        "PrepareDbAfterGetCopyPostgresql has been created.");
    }
    return this.prepareDbAfterGetCopyPostgresql;
  }
}
