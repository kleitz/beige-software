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
    String beanName = getDataSourceName();
    HikariDataSource dataSource =
      (HikariDataSource) getBeansMap().get(beanName);
    if (dataSource == null) {
      Properties props = new Properties();
      props.setProperty("dataSourceClassName",
        "org.postgresql.ds.PGSimpleDataSource");
      props.setProperty("dataSource.user", getDatabaseUser());
      props.setProperty("dataSource.password", getDatabasePassword());
      props.setProperty("dataSource.databaseName", getDatabaseName());
      HikariConfig config = new HikariConfig(props);
      dataSource = new HikariDataSource(config);
      getBeansMap().put(beanName, dataSource);
      lazyGetLogger().info(FactoryAppBeansPostgresql.class, beanName
        + " has been created.");
    }
    return dataSource;
  }


  /**
   * <p>Get Service that prepare Database after full import
   * in lazy mode. It resets auto-incremented ID sequences (APersistableBase)
   * after identical copy of another database.</p>
   * @return IDelegator - preparator Database after full import.
   * @throws Exception - an exception
   */
  @Override
  public final synchronized PrepareDbAfterGetCopyPostgresql<ResultSet>
    lazyGetPrepareDbAfterFullImport() throws Exception {
    String beanName = getPrepareDbAfterFullImportName();
    @SuppressWarnings("unchecked")
    PrepareDbAfterGetCopyPostgresql<ResultSet> prepareDbAfterGetCopyPostgresql =
      (PrepareDbAfterGetCopyPostgresql<ResultSet>) getBeansMap().get(beanName);
    if (prepareDbAfterGetCopyPostgresql == null) {
      prepareDbAfterGetCopyPostgresql =
        new PrepareDbAfterGetCopyPostgresql<ResultSet>();
      prepareDbAfterGetCopyPostgresql
        .setClasses(lazyGetMngSettingsGetDbCopy().getClasses());
      prepareDbAfterGetCopyPostgresql.setLogger(lazyGetLogger());
      prepareDbAfterGetCopyPostgresql.setSrvDatabase(lazyGetSrvDatabase());
      prepareDbAfterGetCopyPostgresql.setFactoryAppBeans(this);
      getBeansMap().put(beanName, prepareDbAfterGetCopyPostgresql);
      lazyGetLogger().info(FactoryAppBeansPostgresql.class, beanName
        + " has been created.");
    }
    return prepareDbAfterGetCopyPostgresql;
  }
}
