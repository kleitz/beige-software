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
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;
import org.junit.BeforeClass;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.persistable.UserJetty;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.persistable.UserRoleJetty;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.orm.service.SrvOrmSqlite;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.test.persistable.GoodVersionTime;
import org.beigesoft.log.ILogger;

/**
 * <p>Test in multithreading, include optimistic locking.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestSqliteLock {

  //services:
  private ISrvOrm<ResultSet> srvOrm;
  
  private SrvDatabase srvDatabase;

  private ILogger logger;

  private Long cakeId;

  public void tstLock() throws Exception {
    logger.info(this.getClass(), "start...");
    doLock();
    Thread thread1 = new Thread() {

      public void run() {
        try {
          TestSqliteLock.this.doLock();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    };
    thread1.start();
    Thread thread2 = new Thread() {

      public void run() {
        try {
          TestSqliteLock.this.doLock();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    };
    thread2.start();
    if (thread1.isAlive()) {
      thread1.join();
    }
    if (thread2.isAlive()) {
      thread2.join();
    }
    logger.info(this.getClass(), "end");
  }

  private void doLock() throws Exception {
    try {
      logger.info(this.getClass(), " Thread ID=" +Thread.currentThread()
        .getId() +" start...");
      GoodVersionTime cake = new GoodVersionTime();
      cake.setItsName("cake");
      cake.setIdDatabaseBirth(999);
      assertNull(cake.getItsVersion());
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(SrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      this.srvOrm.insertEntity(cake);
      this.cakeId = cake.getItsId();
      logger.info(this.getClass(), "cake inserted");
      assertNotNull(cake.getItsVersion());
      cake.setItsName("cake1");
      this.srvOrm.updateEntity(cake);
      Integer rowCountPl = srvOrm.evalRowCount(GoodVersionTime.class);
      GoodVersionTime itsCake1 = srvOrm.retrieveEntityWithConditions(GoodVersionTime.class, "where ITSNAME='cake1'");
      this.srvOrm.deleteEntity(GoodVersionTime.class, cakeId);
      logger.info(this.getClass(), "cake deleted");
      rowCountPl = srvOrm.evalRowCount(GoodVersionTime.class);
      List<GoodVersionTime> goods = srvOrm.retrieveList(GoodVersionTime.class);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        if (!this.srvDatabase.getIsAutocommit()) {
          this.srvDatabase.rollBackTransaction();
        }
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
      throw new Exception(ex);
    } finally {
      try {
        this.srvDatabase.releaseResources();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    logger.info(this.getClass(), " Thread ID=" +Thread.currentThread()
      .getId() +" exit");
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<ResultSet> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<ResultSet> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return SrvDatabase
   **/
  public final SrvDatabase getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final SrvDatabase pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }
}
