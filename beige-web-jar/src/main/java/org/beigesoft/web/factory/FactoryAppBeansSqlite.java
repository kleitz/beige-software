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
   * <p>Get other RDBMS specific bean in lazy mode
   * (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGetOtherRdbmsBean(
    final String pBeanName) throws Exception {
    if (getMngDatabaseName().equals(pBeanName)) {
      return lazyGetMngDatabaseSqlite();
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
    String beanName = getDataSourceName();
    HikariDataSource dataSource =
      (HikariDataSource) getBeansMap().get(beanName);
    if (dataSource == null) {
      dataSource = new HikariDataSource();
      dataSource.setJdbcUrl(getDatabaseName());
      dataSource.setDriverClassName("org.sqlite.JDBC");
      getBeansMap().put(beanName, dataSource);
      lazyGetLogger().info(FactoryAppBeansSqlite.class, beanName
        + " has been created.");
    }
    return dataSource;
  }

  /**
   * <p>Get MngDatabaseSqlite in lazy mode.</p>
   * @return MngDatabaseSqlite - MngDatabaseSqlite
   * @throws Exception - an exception
   */
  public final synchronized MngDatabaseSqlite
    lazyGetMngDatabaseSqlite() throws Exception {
    String beanName = getMngDatabaseName();
    MngDatabaseSqlite mngDatabaseSqlite =
      (MngDatabaseSqlite) getBeansMap().get(beanName);
    if (mngDatabaseSqlite == null) {
      mngDatabaseSqlite = new MngDatabaseSqlite();
      mngDatabaseSqlite.setFactoryAppBeansSqlite(this);
      getBeansMap().put(beanName, mngDatabaseSqlite);
      lazyGetLogger().info(FactoryAppBeansSqlite.class, beanName
        + " has been created.");
    }
    return mngDatabaseSqlite;
  }

  /**
   * <p>Getter of Manager Database service name.</p>
   * @return service name
   **/
  public final String getMngDatabaseName() {
    return "IMngDatabase";
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
      lazyGetLogger().info(FactoryAppBeansSqlite.class, beanName
        + " has been created.");
    }
    return prepareDbAfterGetCopy;
  }
}
