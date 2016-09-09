package org.beigesoft.jdbc.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.LinkedHashMap;
import java.io.File;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.persistable.UserJetty;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.persistable.UserRoleJetty;
import org.beigesoft.orm.service.SrvOrmPostgresql;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.PersistableLine;
import org.beigesoft.orm.test.TestSimple;
import org.beigesoft.log.LoggerSimple;

/**
 * <p>Test of ORM, Entity and Database services.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestPostgres {

  SrvOrmPostgresql<ResultSet> srvOrm;

  SrvDatabase srvDatabase;
  
  LoggerSimple logger = new LoggerSimple();

  public TestPostgres() throws Exception {
    logger.setIsShowDebugMessages(true);
    srvOrm = new SrvOrmPostgresql<ResultSet>();
    srvDatabase = new SrvDatabase();
    srvDatabase.setLogger(logger);
    SrvRecordRetriever srvRecordRetriever = new SrvRecordRetriever();
    srvOrm.setSrvRecordRetriever(srvRecordRetriever);
    srvDatabase.setSrvRecordRetriever(srvRecordRetriever);
    srvOrm.setSrvDatabase(srvDatabase);
    srvOrm.setSrvSqlEscape(new SrvSqlEscape());
    srvOrm.setLogger(logger);
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvDatabase.setHlpInsertUpdate(srvOrm.getHlpInsertUpdate());
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(logger);
    srvOrm.setMngSettings(mngSettings);
    srvOrm.loadConfiguration("beige-orm", "persistence-postgresql.xml");
    Properties props = new Properties();
    props.setProperty("dataSourceClassName", srvOrm.getPropertiesBase().getDataSourceClassName());
    props.setProperty("dataSource.user", srvOrm.getPropertiesBase().getUserName());
    props.setProperty("dataSource.password", srvOrm.getPropertiesBase().getUserPassword());
    props.setProperty("dataSource.databaseName", srvOrm.getPropertiesBase().getDatabaseName());
    HikariConfig config = new HikariConfig(props);
    HikariDataSource ds = new HikariDataSource(config);
    srvDatabase.setDataSource(ds);
  }

  @Test
  public void testAll() throws Exception {
    String currDir = System.getProperty("user.dir");
    System.out.println("Start test JDBC  Postgresql");
    System.out.println("Current dir using System:" + currDir);
    TestSimple<ResultSet> testSimple = new TestSimple<ResultSet>();
    testSimple.setLogger(logger);
    testSimple.setSrvDatabase(srvDatabase);
    testSimple.setSrvOrm(srvOrm);
    testSimple.doTest1();
    TestConcurrence testConcurrence = new TestConcurrence();
    testConcurrence.setLogger(logger);
    testConcurrence.setSrvDatabase(srvDatabase);
    testConcurrence.setSrvOrm(srvOrm);
    testConcurrence.tstThreads();
    System.out.println("End test JDBC Postgresql");
  }
}
