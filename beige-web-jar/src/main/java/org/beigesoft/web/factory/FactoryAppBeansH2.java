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
    String beanName = getDataSourceName();
    HikariDataSource dataSource =
      (HikariDataSource) getBeansMap().get(beanName);
    if (dataSource == null) {
      Properties props = new Properties();
      props.setProperty("dataSourceClassName", "org.h2.jdbcx.JdbcDataSource");
      props.setProperty("dataSource.user", getDatabaseUser());
      props.setProperty("dataSource.password", getDatabasePassword());
      props.setProperty("dataSource.Url", getDatabaseName());
      HikariConfig config = new HikariConfig(props);
      dataSource = new HikariDataSource(config);
      getBeansMap().put(beanName, dataSource);
      lazyGetLogger().info(FactoryAppBeansH2.class, beanName
        + " has been created.");
    }
    return dataSource;
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
    String beanName = getPrepareDbAfterFullImportName();
    PrepareDbAfterGetCopy prepareDbAfterGetCopy =
      (PrepareDbAfterGetCopy) getBeansMap().get(beanName);
    if (prepareDbAfterGetCopy == null) {
      prepareDbAfterGetCopy = new PrepareDbAfterGetCopy();
      prepareDbAfterGetCopy.setLogger(lazyGetLogger());
      prepareDbAfterGetCopy.setFactoryAppBeans(this);
      getBeansMap().put(beanName, prepareDbAfterGetCopy);
      lazyGetLogger().info(FactoryAppBeansH2.class, beanName
        + " has been created.");
    }
    return prepareDbAfterGetCopy;
  }
}
