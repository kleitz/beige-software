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

import org.beigesoft.orm.service.SrvOrmPostgresql;
import java.sql.ResultSet;

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
    }
    return this.dataSource;
  }
}
