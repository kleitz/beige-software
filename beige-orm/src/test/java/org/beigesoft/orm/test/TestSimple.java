package org.beigesoft.orm.test;

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
import java.util.List;
import java.util.HashMap;
import java.io.File;
import java.text.SimpleDateFormat;
import java.lang.Thread;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.beigesoft.persistable.UserJetty;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.persistable.IdUserRoleJetty;
import org.beigesoft.persistable.UserRoleJetty;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.persistable.IdUserRoleTomcat;
import org.beigesoft.test.persistable.Department;
import org.beigesoft.test.persistable.GoodVersionTime;
import org.beigesoft.test.persistable.GoodsRating;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.PersistableLine;
import org.beigesoft.orm.model.PropertiesBase;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.service.ASrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.test.model.EStatus;
import org.beigesoft.log.ILogger;

/**
 * <p>Test of ORM service.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class TestSimple<RS> {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  private ASrvOrm<RS> srvOrm;

  private ISrvDatabase<RS> srvDatabase;

  public void doTest1() throws Exception {
    try {
      getLogger().info(null, TestSimple.class, "Start test simple");
      srvDatabase.setIsAutocommit(true);
      Map<String, Object> addParam = new HashMap<String, Object>();
      srvOrm.initializeDatabase(addParam);
      UserJetty userJetty = srvOrm.retrieveEntityWithConditions(addParam, UserJetty.class, "where ITSNAME='admin'");
      if (userJetty == null) {
        userJetty = new UserJetty();
        userJetty.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        userJetty.setItsName("admin");
        userJetty.setItsPassword("admin");
        srvOrm.insertEntity(addParam, userJetty);
        getLogger().info(null, TestSimple.class, "jetty user admin has inserted");
      }
      assertNotNull(userJetty.getItsId());
      getLogger().info(null, TestSimple.class, "jetty user admin ID=" + userJetty.getItsId());
      RoleJetty roleJettyAdmin = srvOrm.retrieveEntityWithConditions(addParam, RoleJetty.class, "where ITSNAME='admin'");
      if (roleJettyAdmin == null) {
        roleJettyAdmin = new RoleJetty();
        roleJettyAdmin.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        roleJettyAdmin.setItsName("admin");
        srvOrm.insertEntity(addParam, roleJettyAdmin);
        getLogger().info(null, TestSimple.class, "jetty role admin has inserted");
      }
      assertNotNull(roleJettyAdmin.getItsId());
      getLogger().info(null, TestSimple.class, "jetty role admin ID=" + roleJettyAdmin.getItsId());
      RoleJetty roleJettyUser = srvOrm.retrieveEntityWithConditions(addParam, RoleJetty.class, "where ITSNAME='user'");
      if (roleJettyUser == null) {
        roleJettyUser = new RoleJetty();
        roleJettyUser.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        roleJettyUser.setItsName("user");
        srvOrm.insertEntity(addParam, roleJettyUser);
        getLogger().info(null, TestSimple.class, "jetty role user has inserted");
      }
      assertNotNull(roleJettyUser.getItsId());
      getLogger().info(null, TestSimple.class, "jetty role user ID=" + roleJettyUser.getItsId());
      List<RoleJetty> lstroleJet = srvOrm.retrieveList(addParam, RoleJetty.class);
      assertEquals(2, lstroleJet.size());
      getLogger().info(null, TestSimple.class, "jetty roles list:");
      for (RoleJetty rj : lstroleJet) {
        getLogger().info(null, TestSimple.class, "rj id: " + rj.getItsId());
        getLogger().info(null, TestSimple.class, "rj name: " + rj.getItsName());
      }
      roleJettyAdmin = srvOrm.retrieveEntity(addParam, roleJettyAdmin);
      assertEquals("admin", roleJettyAdmin.getItsName());
      UserRoleJetty urj = new UserRoleJetty();
      urj.setItsUser(userJetty);
      urj.setItsRole(roleJettyAdmin);
      UserRoleJetty userRoleJetty = srvOrm.retrieveEntity(addParam, urj);
      if (userRoleJetty == null) {
        userRoleJetty = new UserRoleJetty();
        userRoleJetty.setItsUser(userJetty);
        userRoleJetty.setItsRole(roleJettyAdmin);
        srvOrm.insertEntity(addParam, userRoleJetty);
        getLogger().info(null, TestSimple.class, "jetty user role admin-admin has inserted");
      }
      userRoleJetty = srvOrm.retrieveEntity(addParam, urj);
      assertEquals(roleJettyAdmin.getItsId(), userRoleJetty.getItsRole().getItsId());
      assertEquals(userJetty.getItsId(), userRoleJetty.getItsUser().getItsId());
      UserRoleJetty userRoleJetty2 = srvOrm.retrieveEntityById(addParam, UserRoleJetty.class, urj.getItsId());
      assertEquals(roleJettyAdmin.getItsId(), userRoleJetty2.getItsRole().getItsId());
      assertEquals(userJetty.getItsId(), userRoleJetty2.getItsUser().getItsId());
      UserTomcat userTomcat = new UserTomcat();
      userTomcat.setItsId("admin");
      userTomcat = srvOrm.retrieveEntity(addParam, userTomcat);
      if (userTomcat == null) {
        userTomcat = new UserTomcat();
        userTomcat.setItsUser("admin");
        userTomcat.setItsPassword("admin");
        srvOrm.insertEntity(addParam, userTomcat);
        getLogger().info(null, TestSimple.class, "tomcat user admin has inserted");
      }
      assertNotNull(userTomcat.getItsId());
      getLogger().info(null, TestSimple.class, "tomcat user admin ID=" + userTomcat.getItsUser());
      UserRoleTomcat urt = new UserRoleTomcat();
      urt.setItsUser(userTomcat);
      urt.setItsRole("admin");
      UserRoleTomcat userRoleTomcat = srvOrm.retrieveEntity(addParam, urt);
      if (userRoleTomcat == null) {
        userRoleTomcat = new UserRoleTomcat();
        userRoleTomcat.setItsUser(userTomcat);
        userRoleTomcat.setItsRole("admin");
        srvOrm.insertEntity(addParam, userRoleTomcat);
        getLogger().info(null, TestSimple.class, "tomcat user role admin-admin has inserted");
      }
      userRoleTomcat = srvOrm.retrieveEntity(addParam, urt);
      assertEquals(userTomcat.getItsUser(), userRoleTomcat.getItsUser().getItsUser());
      assertEquals("admin", userRoleTomcat.getItsRole());
      UserRoleTomcat userRoleTomcat2 = srvOrm.retrieveEntityById(addParam, UserRoleTomcat.class, urt.getItsId());
      assertEquals(userTomcat.getItsUser(), userRoleTomcat2.getItsUser().getItsUser());
      assertEquals("admin", userRoleTomcat2.getItsRole());
      
      srvDatabase.setIsAutocommit(false);
      srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      srvDatabase.beginTransaction();
      Department department1 = srvOrm.retrieveEntityWithConditions(addParam, Department.class, "where ITSNAME='sellers'");
      if (department1 == null) {
        department1 = new Department();
        department1.setItsName("sellers");
        department1.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        srvOrm.insertEntity(addParam, department1);
        getLogger().info(null, TestSimple.class, "department sellers inserted");
      }
      Department department2 = srvOrm.retrieveEntityWithConditions(addParam, Department.class, "where ITSNAME='accountants'");
      if (department2 == null) {
        department2 = new Department();
        department2.setItsName("accountants");
        department2.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        srvOrm.insertEntity(addParam, department2);
        getLogger().info(null, TestSimple.class, "department accountants inserted");
      }
      department2 = srvOrm.retrieveEntityWithConditions(addParam, Department.class, "where ITSNAME='accountants'");
      assertNotNull(department2);
      List<Department> deptms = srvOrm.retrieveList(addParam, Department.class);
      Integer rowCountDp = srvOrm.evalRowCountWhere(addParam, Department.class, "ITSNAME='sellers'");
      assertEquals(new Integer(1), rowCountDp);
      GoodVersionTime itsProduct1 = srvOrm.retrieveEntityWithConditions(addParam, GoodVersionTime.class, "where ITSNAME='itsProduct1'");
      if (itsProduct1 == null) {
        itsProduct1 = new GoodVersionTime();
        itsProduct1.setItsName("itsProduct1");
        itsProduct1.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        assertNull(itsProduct1.getItsVersion());
        srvOrm.insertEntity(addParam, itsProduct1);
        getLogger().info(null, TestSimple.class, "itsProduct1 inserted");
      }
      GoodsRating gr = new GoodsRating();
      gr.setAverageRating(7);
      gr.setGoods(itsProduct1);
      GoodsRating prod1Rat = srvOrm.retrieveEntity(addParam, gr);
      if (prod1Rat == null) {
        prod1Rat = new GoodsRating();
        prod1Rat.setGoods(itsProduct1);
        prod1Rat.setAverageRating(7);
        srvOrm.insertEntity(addParam, prod1Rat);
        getLogger().info(null, TestSimple.class, "prod1Rat inserted");
      }
      getLogger().info(null, TestSimple.class, "try to get GoogsRating by ID - " + itsProduct1 + " ID " + itsProduct1.getItsId());
      prod1Rat = srvOrm.retrieveEntityById(addParam, GoodsRating.class, itsProduct1);
      assertEquals(prod1Rat.getAverageRating(), gr.getAverageRating());
      GoodVersionTime itsProduct2 = srvOrm.retrieveEntityWithConditions(addParam, GoodVersionTime.class, "where ITSNAME='itsProduct2'");
      if (itsProduct2 == null) {
        itsProduct2 = new GoodVersionTime();
        itsProduct2.setItsName("itsProduct2");
        itsProduct2.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        assertNull(itsProduct2.getItsVersion());
        srvOrm.insertEntity(addParam, itsProduct2);
        getLogger().info(null, TestSimple.class, "itsProduct2 inserted");
      }
      PersistableHead persistableHead1 = srvOrm.retrieveEntityWithConditions(addParam, PersistableHead.class, "where ITSDEPARTMENT=" + department1.getItsId());
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date itsDate = dateFormat.parse("01/01/2016");
      getLogger().info(null, TestSimple.class, "persHead date=" + itsDate);
      Double itsDouble = 245.43445;
      Float itsFloat = 245.434f;
      Integer itsInteger = 241;
      Long itsLong = 244444l;
      BigDecimal itsTotal = new BigDecimal("123.67");
      if (persistableHead1 == null) {
        persistableHead1 = new PersistableHead();
        persistableHead1.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        persistableHead1.setItsDepartment(department1);
        persistableHead1.setWaitingTime(22);
        persistableHead1.setItsStatus(EStatus.STATUS_B);
        persistableHead1.setItsDate(itsDate);
        persistableHead1.setItsTotal(itsTotal);
        persistableHead1.setItsDouble(itsDouble);
        persistableHead1.setItsFloat(itsFloat);
        persistableHead1.setItsInteger(itsInteger);
        persistableHead1.setItsLong(itsLong);
        assertNull(persistableHead1.getItsId());
        srvOrm.insertEntity(addParam, persistableHead1);
        getLogger().info(null, TestSimple.class, "persistableHead1 has inserted");
        persistableHead1 = srvOrm.retrieveEntityWithConditions(addParam, PersistableHead.class, "where ITSDEPARTMENT=" + department1.getItsId());
      }
      assertNotNull(persistableHead1.getItsId());
      Long itsId = persistableHead1.getItsId();
      persistableHead1 = new PersistableHead();
      persistableHead1.setItsId(itsId);
      persistableHead1 = srvOrm.retrieveEntity(addParam, persistableHead1);
      assertFalse(persistableHead1.getIsClosed());
      assertEquals(EStatus.STATUS_B, persistableHead1.getItsStatus());
      assertEquals(itsDouble, persistableHead1.getItsDouble());
      assertEquals(itsFloat, persistableHead1.getItsFloat());
      assertEquals(itsLong, persistableHead1.getItsLong());
      assertEquals(itsInteger, persistableHead1.getItsInteger());
      assertEquals(itsDate, persistableHead1.getItsDate());
      assertEquals(itsTotal, persistableHead1.getItsTotal());
      assertEquals(department1.getItsId(), persistableHead1.getItsDepartment().getItsId());
      assertEquals(department1.getItsName(), persistableHead1.getItsDepartment().getItsName());
      Date itsDate1 = dateFormat.parse("01/02/2016");;
      Double itsDouble1 = 25.43445;//245.434449999999998 postgresql?
      Float itsFloat1 = 24.434f;//245.434006 postgresql?
      Integer itsInteger1 = 2241;
      Long itsLong1 = 2444544l;
      BigDecimal itsTotal1 = new BigDecimal("1233.67");
      persistableHead1.setItsDate(itsDate1);
      getLogger().info(null, TestSimple.class, "persHead date updated=" + itsDate1);
      persistableHead1.setIsClosed(true);
      persistableHead1.setItsTotal(itsTotal1);
      persistableHead1.setItsDouble(itsDouble1);
      persistableHead1.setItsFloat(itsFloat1);
      persistableHead1.setItsInteger(itsInteger1);
      persistableHead1.setItsLong(itsLong1);
      srvOrm.updateEntity(addParam, persistableHead1);
      persistableHead1 = new PersistableHead();
      persistableHead1.setItsId(itsId);
      persistableHead1 = srvOrm.retrieveEntity(addParam, persistableHead1);
      assertEquals(itsDouble1, persistableHead1.getItsDouble());
      assertEquals(itsFloat1, persistableHead1.getItsFloat());
      assertEquals(itsLong1, persistableHead1.getItsLong());
      assertEquals(itsInteger1, persistableHead1.getItsInteger());
      assertEquals(itsDate1, persistableHead1.getItsDate());
      assertEquals(itsTotal1, persistableHead1.getItsTotal());
      PersistableLine persistableLine = new PersistableLine();
      persistableLine.setPersistableHead(persistableHead1);
      persistableHead1.setPersistableLines(srvOrm.retrieveListForField(addParam, persistableLine, "persistableHead"));
      BigDecimal itsPrice1 = new BigDecimal("2.33");
      BigDecimal itsQuantity1 = new BigDecimal("2");
      BigDecimal itsQuantity2 = new BigDecimal("4");
      BigDecimal itsPrice2 = new BigDecimal("4.31");
      if (persistableHead1.getPersistableLines().size() == 0) {
        PersistableLine pl1 = new PersistableLine();
        pl1.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        pl1.setPersistableHead(persistableHead1);
        pl1.setItsProduct(itsProduct1);
        pl1.setItsPrice(itsPrice1);
        pl1.setItsQuantity(itsQuantity1);
        pl1.setItsTotal(itsPrice1.multiply(itsQuantity1));
        srvOrm.insertEntity(addParam, pl1);
        getLogger().info(null, TestSimple.class, "persLine1 has inserted");
        persistableHead1.getPersistableLines().add(pl1);
        PersistableLine pl2 = new PersistableLine();
        pl2.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        pl2.setPersistableHead(persistableHead1);
        pl2.setItsProduct(itsProduct2);
        pl2.setItsPrice(itsPrice2);
        pl2.setItsQuantity(itsQuantity2);
        pl2.setItsTotal(itsPrice2.multiply(itsQuantity2));
        srvOrm.insertEntity(addParam, pl2);
        persistableHead1.getPersistableLines().add(pl2);
        getLogger().info(null, TestSimple.class, "persLine2 has inserted");
      } else {
        assertEquals(itsProduct2.getItsId(),
          persistableHead1.getPersistableLines().get(1).getItsProduct().getItsId());
        assertEquals(department1.getItsId(),
          persistableHead1.getPersistableLines().get(1).getPersistableHead().getItsDepartment().getItsId());
        assertNull(
          persistableHead1.getPersistableLines().get(1).getPersistableHead().getItsDepartment().getItsName());
      }
      Integer rowCountPl = srvOrm.evalRowCount(addParam, PersistableLine.class);
      assertEquals(new Integer(2), rowCountPl);
      assertEquals(2,
        persistableHead1.getPersistableLines().size());
      assertEquals(itsPrice1,
        persistableHead1.getPersistableLines().get(0).getItsPrice());
      getLogger().info(null, TestSimple.class, "persLine1 price=" + persistableHead1.getPersistableLines().get(0).getItsPrice());
      assertTrue(itsQuantity2.compareTo(persistableHead1.getPersistableLines().get(1).getItsQuantity()) == 0);
      assertEquals(itsProduct2.getItsName(),
        persistableHead1.getPersistableLines().get(1).getItsProduct().getItsName());
      assertEquals(persistableHead1.getItsId(),
        persistableHead1.getPersistableLines().get(1).getPersistableHead().getItsId());
      assertTrue(persistableHead1.getIsClosed());
      assertTrue(persistableHead1.getPersistableLines().get(1).getPersistableHead().getIsClosed());
      //set persistableHead1 back
      persistableHead1.setIsClosed(false);
      persistableHead1.setItsDate(itsDate);
      persistableHead1.setItsTotal(itsTotal);
      persistableHead1.setItsDouble(itsDouble);
      persistableHead1.setItsFloat(itsFloat);
      persistableHead1.setItsInteger(itsInteger);
      persistableHead1.setItsLong(itsLong);
      srvOrm.updateEntity(addParam, persistableHead1);
      //new pershead:
      PersistableHead persistableHead2 = srvOrm.retrieveEntityWithConditions(addParam, PersistableHead.class, "where ITSDEPARTMENT=" + department2.getItsId());
      if (persistableHead2 == null) {
        persistableHead2 = new PersistableHead();
        persistableHead2.setIdDatabaseBirth(srvDatabase.getIdDatabase());
        persistableHead2.setItsDepartment(department2);
        persistableHead2.setWaitingTime(27);
        persistableHead2.setItsDate(dateFormat.parse("02/01/2016"));
        persistableHead2.setItsTotal(new BigDecimal("12389.67"));
        persistableHead2.setItsDouble(299.43445);
        persistableHead2.setItsFloat(211.434f);
        persistableHead2.setItsInteger(2441);
        persistableHead2.setItsLong(2544l);
        srvOrm.insertEntity(addParam, persistableHead2);
        getLogger().info(null, TestSimple.class, "persistableHead2 has inserted");
      }
      assertEquals(null, persistableHead2.getItsStatus());
      assertFalse(persistableHead2.getIsClosed());
      //savepoint rollback test:
      String goodVersionTimeSp = "GoodVersionTimeSp";
      srvDatabase.createSavepoint(goodVersionTimeSp);
      GoodVersionTime caces = new GoodVersionTime();
      caces.setItsName("caces");
      caces.setIdDatabaseBirth(srvDatabase.getIdDatabase());
      assertNull(caces.getItsVersion());
      getLogger().info(null, TestSimple.class, "caces ID was " + caces.getItsId());
      getLogger().info(null, TestSimple.class, "caces version was " + caces.getItsVersion());
      srvOrm.insertEntity(addParam, caces);
      getLogger().info(null, TestSimple.class, "caces inserted");
      //caces = srvOrm.retrieveEntity(null, caces); //refresh
      Long cacesId = caces.getItsId();
      Long cacesVer = caces.getItsVersion();
      assertNotNull(cacesId);
      assertNotNull(cacesVer);
      getLogger().info(null, TestSimple.class, "caces ID is " + caces.getItsId());
      getLogger().info(null, TestSimple.class, "caces version after inserting " + caces.getItsVersion());
      caces.setItsName("caces upd");
      try {
        Thread.sleep(10);
      } catch (Exception ex) {
      }
      srvOrm.updateEntity(addParam, caces);
      //caces = srvOrm.retrieveEntity(null, caces); //refresh
      Long cacesVerUp = caces.getItsVersion();
      getLogger().info(null, TestSimple.class, "caces version after update " + caces.getItsVersion());
      assertNotSame(cacesVer, cacesVerUp);
      caces = new GoodVersionTime();
      caces.setItsId(cacesId);
      caces = srvOrm.retrieveEntity(addParam, caces);
      assertEquals(cacesVerUp, caces.getItsVersion());
      srvDatabase.rollBackTransaction(goodVersionTimeSp);
      getLogger().info(null, TestSimple.class, "It has rollback of inserting caces");
      caces = new GoodVersionTime();
      caces.setItsId(cacesId);
      caces = srvOrm.retrieveEntity(addParam, caces);
      assertNull(caces);
      srvDatabase.commitTransaction();
      getLogger().info(null, TestSimple.class, "Transaction commit successfully, exit test simple");
    } catch (Exception ex) {
      ex.printStackTrace();
      if (!srvDatabase.getIsAutocommit()) {
        srvDatabase.rollBackTransaction();
      }
      throw new Exception(ex);
    } finally {
      srvDatabase.releaseResources();
    }
  }

  //Simple getters and setters:
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

  /**
   * <p>Geter for srvOrm.</p>
   * @return ASrvOrm<RS>
   **/
  public final ASrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ASrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }
}
