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
    String beanName = getDataSourceName();
    HikariDataSource dataSource =
      (HikariDataSource) getBeansMap().get(beanName);
    if (dataSource == null) {
      Properties props = new Properties();
      props.setProperty("dataSourceClassName",
        "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
      props.setProperty("dataSource.user", getDatabaseUser());
      props.setProperty("dataSource.password", getDatabasePassword());
      props.setProperty("dataSource.url", getDatabaseName());
      HikariConfig config = new HikariConfig(props);
      dataSource = new HikariDataSource(config);
      getBeansMap().put(beanName, dataSource);
      lazyGetLogger().info(FactoryAppBeansMysql.class, beanName
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
    PrepareDbAfterGetCopy prepareDbAfterGetCopyMysql =
      (PrepareDbAfterGetCopy) getBeansMap().get(beanName);
    if (prepareDbAfterGetCopyMysql == null) {
      prepareDbAfterGetCopyMysql =
        new PrepareDbAfterGetCopy();
      prepareDbAfterGetCopyMysql.setLogger(lazyGetLogger());
      prepareDbAfterGetCopyMysql.setFactoryAppBeans(this);
      getBeansMap().put(beanName, prepareDbAfterGetCopyMysql);
      lazyGetLogger().info(FactoryAppBeansMysql.class, beanName
        + " has been created.");
    }
    return prepareDbAfterGetCopyMysql;
  }
}
