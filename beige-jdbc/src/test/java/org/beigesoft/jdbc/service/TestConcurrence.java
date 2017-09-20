package org.beigesoft.jdbc.service;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class TestConcurrence {

  //services:
  private ISrvOrm<ResultSet> srvOrm;
  
  private SrvDatabase srvDatabase;

  private ILogger logger;

  //Shared flags:
  Boolean isCakeInserted = false;

  Boolean isCakeUpdatedByTh2 = false;
  
  Boolean isThread1End = false;

  Boolean isThread2End = false;

  Boolean isThread3End = false;
  
  Boolean isThread1Ok = false;

  Boolean isThread2Ok = false;

  Boolean isThread3Ok = false;
  
  //Data:
  Connection connThread1;

  Connection connThread2;

  Long cakeId;
  
  Long sugarId;

  public void tstThreads() throws Exception {
    doThread1();
    doThread2();
    doThread3();
    boolean lIsThread3End = false;
    int attempt = 0;
    while (!lIsThread3End && attempt < 20) {
      attempt++;
      logger.info(null, this.getClass(), "Th0: waiting for Th3");
      Thread.sleep(700);
      synchronized (isThread3End) {
        lIsThread3End = isThread3End;
      }
    }
    assertTrue(isThread1Ok);
    assertTrue(isThread2Ok);
    assertTrue(isThread3Ok);
    assertTrue(TestConcurrence.this.connThread1 != TestConcurrence.this.connThread2);
    logger.info(null, this.getClass(), "Connection in thread1 - " + TestConcurrence.this.connThread1);
    logger.info(null, this.getClass(), "Connection in thread2 - " + TestConcurrence.this.connThread2);
  }

  public void doThread1() {
    Thread thread1 = new Thread() {

      public void run() {
        try {
          GoodVersionTime cake = new GoodVersionTime();
          Map<String, Object> addParam = new HashMap<String, Object>();
          cake.setItsName("cake");
          cake.setIdDatabaseBirth(999);
          assertNull(cake.getItsVersion());
          TestConcurrence.this.srvDatabase.setIsAutocommit(false);
          TestConcurrence.this.srvDatabase.setTransactionIsolation(SrvDatabase.TRANSACTION_READ_UNCOMMITTED);
          TestConcurrence.this.srvDatabase.beginTransaction();
          TestConcurrence.this.srvOrm.insertEntity(addParam, cake);
          cake = TestConcurrence.this.srvOrm.retrieveEntity(addParam, cake);
          TestConcurrence.this.srvDatabase.commitTransaction();
          TestConcurrence.this.cakeId = cake.getItsId();
          logger.info(null, this.getClass(), "Th1: cake inserted");
          assertNotNull(cake.getItsVersion());
          synchronized (TestConcurrence.this.isCakeInserted) {
            TestConcurrence.this.isCakeInserted = true;
          }
          boolean lIsCakeUpdatedByTh2 = false;
          int attempt = 0;
          while (!lIsCakeUpdatedByTh2 && attempt < 20) {
            attempt++;
            logger.info(null, this.getClass(), "Th1: waiting for updating cake by Th2");
            Thread.sleep(200);
            synchronized (TestConcurrence.this.isCakeUpdatedByTh2) {
              lIsCakeUpdatedByTh2 = TestConcurrence.this.isCakeUpdatedByTh2;
            }
          }
          boolean isCakeChangedByTh2 = false;
          try {
            TestConcurrence.this.srvDatabase.beginTransaction();
            TestConcurrence.this.srvOrm.updateEntity(addParam, cake);
            TestConcurrence.this.srvDatabase.commitTransaction();
          } catch (ExceptionWithCode ex) {
            if (ex.getCode() == SrvDatabase.DIRTY_READ) {
              isCakeChangedByTh2 = true;
              logger.info(null, this.getClass(), "Th1 exeption: " + ex.getMessage());
              TestConcurrence.this.srvDatabase.rollBackTransaction();
            }
          }
          assertTrue(isCakeChangedByTh2);
          TestConcurrence.this.connThread1 = TestConcurrence.this.srvDatabase.getCurrentConnection();
          synchronized (TestConcurrence.this.isThread1End) {
            TestConcurrence.this.isThread1End = true;
          }
          logger.info(null, this.getClass(), "Th1: exit");
          TestConcurrence.this.isThread1Ok = true;
        } catch (Exception ex) {
          ex.printStackTrace();
          synchronized (TestConcurrence.this.isThread1End) {
            TestConcurrence.this.isThread1End = true;
          }
          try {
            if (!TestConcurrence.this.srvDatabase.getIsAutocommit()) {
              TestConcurrence.this.srvDatabase.rollBackTransaction();
            }
          } catch (Exception ex2) {
            ex2.printStackTrace();
          }
          assertTrue(false);
        } finally {
          try {
            TestConcurrence.this.srvDatabase.releaseResources();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    };
    thread1.start();
  }

  public void doThread2() {
    Thread thread2 = new Thread() {
      
      public void run() {
        try {
          GoodVersionTime sugar = new GoodVersionTime();
          Map<String, Object> addParam = new HashMap<String, Object>();
          sugar.setItsName("sugar");
          sugar.setIdDatabaseBirth(999);
          TestConcurrence.this.srvDatabase.setIsAutocommit(false);
          TestConcurrence.this.srvDatabase.setTransactionIsolation(SrvDatabase.TRANSACTION_READ_UNCOMMITTED);
          //insert sugar:
          TestConcurrence.this.srvDatabase.beginTransaction();
          TestConcurrence.this.srvOrm.insertEntity(addParam, sugar);
          TestConcurrence.this.srvDatabase.commitTransaction();
          logger.info(null, this.getClass(), "Th2: sugar inserted");
          TestConcurrence.this.sugarId = sugar.getItsId();
          boolean lIsCakeInserted = false;
          synchronized (TestConcurrence.this.isCakeInserted) {
            lIsCakeInserted = TestConcurrence.this.isCakeInserted;
          }
          int attempt = 0;
          while (!lIsCakeInserted && attempt < 20) {
            attempt++;
            logger.info(null, this.getClass(), "Th2: waiting for cake");
            Thread.sleep(200);
            synchronized (TestConcurrence.this.isCakeInserted) {
              lIsCakeInserted = TestConcurrence.this.isCakeInserted;
            }
          }
          //change cake:
          TestConcurrence.this.srvDatabase.beginTransaction();
          GoodVersionTime cake = new GoodVersionTime();
          cake.setItsId(cakeId);
          cake = TestConcurrence.this.srvOrm.retrieveEntity(addParam, cake);
          Long oldVersion = cake.getItsVersion();
          TestConcurrence.this.srvOrm.updateEntity(addParam, cake);
          //cake = TestConcurrence.this.srvOrm.retrieveEntity(addParam, cake); // refresh
          TestConcurrence.this.srvDatabase.commitTransaction();
          assertNotSame(oldVersion, cake.getItsVersion());
          logger.info(null, this.getClass(), "Th2: cake updated");
          synchronized (TestConcurrence.this.isCakeUpdatedByTh2) {
            TestConcurrence.this.isCakeUpdatedByTh2 = true;
          }
          TestConcurrence.this.connThread2 = TestConcurrence.this.srvDatabase.getCurrentConnection();
          synchronized (TestConcurrence.this.isThread2End) {
            TestConcurrence.this.isThread2End = true;
          }
          logger.info(null, this.getClass(), "Th2: exit");
          TestConcurrence.this.isThread2Ok = true;
        } catch (Exception ex) {
          ex.printStackTrace();
          try {
            if (!TestConcurrence.this.srvDatabase.getIsAutocommit()) {
              TestConcurrence.this.srvDatabase.rollBackTransaction();
            }
          } catch (Exception ex2) {
            ex2.printStackTrace();
          }
          assertTrue(false);
        } finally {
          try {
            TestConcurrence.this.srvDatabase.releaseResources();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    };
    thread2.start();
  }

  public void doThread3() throws Exception {
    Thread thread3 = new Thread() {
      
      public void run() {
        try {
          boolean lIsThread1End = false;
          int attempt = 0;
          Map<String, Object> addParam = new HashMap<String, Object>();
          while (!lIsThread1End && attempt < 20) {
            attempt++;
            logger.info(null, this.getClass(), "Th3: waiting for Th1");
            Thread.sleep(450);
            synchronized (TestConcurrence.this.isThread1End) {
              lIsThread1End = TestConcurrence.this.isThread1End;
            }
          }
          boolean lIsThread2End = false;
          synchronized (TestConcurrence.this.isThread2End) {
            lIsThread2End = TestConcurrence.this.isThread2End;
          }
          attempt = 0;
          while (!lIsThread2End && attempt < 20) {
            attempt++;
            logger.info(null, this.getClass(), "Th3: waiting for Th2");
            Thread.sleep(450);
            synchronized (TestConcurrence.this.isThread2End) {
              lIsThread2End = TestConcurrence.this.isThread2End;
            }
          }
          TestConcurrence.this.srvDatabase.setIsAutocommit(true);
          if (cakeId != null) {
            GoodVersionTime cake = new GoodVersionTime();
            cake.setItsId(cakeId);
            TestConcurrence.this.srvOrm.deleteEntity(addParam, cake);
            logger.info(null, this.getClass(), "Th3: cake deleted");
          }
          if (sugarId != null) {
            GoodVersionTime sugar = new GoodVersionTime();
            sugar.setItsId(sugarId);
            TestConcurrence.this.srvOrm.deleteEntity(addParam, sugar);
            logger.info(null, this.getClass(), "Th3: sugar deleted");
          }
          synchronized (TestConcurrence.this.isThread3End) {
            TestConcurrence.this.isThread3End = true;
          }
          logger.info(null, this.getClass(), "Th3: exit");
          TestConcurrence.this.isThread3Ok = true;
        } catch (Exception ex) {
          ex.printStackTrace();
          synchronized (TestConcurrence.this.isThread3End) {
            TestConcurrence.this.isThread3End = true;
          }
          assertTrue(false);
        } finally {
           try {
            TestConcurrence.this.srvDatabase.releaseResources();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    };
    thread3.start();
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
