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

import org.beigesoft.settings.MngSettings;
import org.beigesoft.persistable.UserJetty;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.persistable.UserRoleJetty;
import org.beigesoft.orm.service.SrvOrmSqlite;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.PersistableLine;
import org.beigesoft.orm.test.TestSimple;
import org.beigesoft.log.LoggerSimple;

/**
 * <p>Test of ORM service.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestSqlite {

  SrvOrmSqlite<ResultSet> srvOrm;

  SrvDatabase srvDatabase;
  
  LoggerSimple logger = new LoggerSimple();

  public TestSqlite() throws Exception {
    logger.setIsShowDebugMessages(true);
    srvOrm = new SrvOrmSqlite<ResultSet>();
    srvDatabase = new SrvDatabase();
    srvDatabase.setLogger(logger);
    SrvRecordRetriever srvRecordRetriever = new SrvRecordRetriever();
    srvOrm.setSrvRecordRetriever(srvRecordRetriever);
    srvDatabase.setSrvRecordRetriever(srvRecordRetriever);
    srvOrm.setSrvDatabase(srvDatabase);
    srvOrm.setLogger(logger);
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvOrm.setSrvSqlEscape(new SrvSqlEscape());
    srvDatabase.setHlpInsertUpdate(srvOrm.getHlpInsertUpdate());
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(logger);
    srvOrm.setMngSettings(mngSettings);
    srvOrm.loadConfiguration("beige-orm", "persistence-sqlite.xml");
    String currDir = System.getProperty("user.dir");
    System.out.println("Start test JDBC Sqlite");
    System.out.println("Current dir using System:" + currDir);
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(srvOrm.getPropertiesBase().getDatabaseUrl());
    ds.setDriverClassName(srvOrm.getPropertiesBase().getJdbcDriverClass());
    srvDatabase.setDataSource(ds);
  }

  @Test
  public void testAll() throws Exception {
    TestSimple<ResultSet> testSimple = new TestSimple<ResultSet>();
    testSimple.setSrvDatabase(srvDatabase);
    testSimple.setLogger(logger);
    testSimple.setSrvOrm(srvOrm);
    testSimple.doTest1();
    TestConcurrence testConcurrence = new TestConcurrence();
    testConcurrence.setLogger(logger);
    testConcurrence.setSrvDatabase(srvDatabase);
    testConcurrence.setSrvOrm(srvOrm);
    testConcurrence.tstThreads();
    TestSqliteLock testSqliteLock = new TestSqliteLock();
    testSqliteLock.setLogger(logger);
    testSqliteLock.setSrvDatabase(srvDatabase);
    testSqliteLock.setSrvOrm(srvOrm);
    testSqliteLock.tstLock();
    System.out.println("End test JDBC Sqlite");
  }
}
