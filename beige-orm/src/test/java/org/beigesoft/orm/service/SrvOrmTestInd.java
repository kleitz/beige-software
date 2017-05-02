package org.beigesoft.orm.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.math.BigDecimal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

import org.beigesoft.service.UtlReflection;
import org.beigesoft.orm.service.ASrvOrm;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.PersistableLine;
import org.beigesoft.test.persistable.UserRoleTomcatPriority;
import org.beigesoft.test.persistable.GoodVersionTime;
import org.beigesoft.test.persistable.GoodsRating;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;

/**
 * <p>RDBMS independent test of ORM service working without database.
 * </p>
 *
 * @author Yury Demidenko
 */
public class SrvOrmTestInd {

  /**
   * <pre>
   * Test RDBMS independent of ORM service.
   * Exactly:
   * a) loading properties
   * b) generating queries
   * So:
   * PropertiesBase:
   * .Test persistable classes
   * .Test excluded fields
   * .Test not-null fields
   * .Test tables with version=last time changed
   * TableSql
   * .Test column definitions
   * Generating queries:
   * .Test optimistic locking version changed in update query
   * </pre>
   **/
  public void doRdbmsIndependentAssertions(ASrvOrm<?> srvOrm) throws Exception {
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.persistable.UserJetty.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.persistable.RoleJetty.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.persistable.UserRoleJetty.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.test.persistable.Department.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.test.persistable.GoodVersionTime.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.test.persistable.PersistableHead.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.test.persistable.PersistableLine.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.persistable.UserTomcat.class));
    assertTrue(srvOrm.getMngSettings().getClasses().contains(org.beigesoft.persistable.UserRoleTomcat.class));

    String usTomcatNm = UserTomcat.class.getSimpleName();
    System.out.println("Test for class - " + usTomcatNm);
    TableSql tblSqlUserTomcat = srvOrm.getTablesMap().get(usTomcatNm);
    assertTrue(tblSqlUserTomcat != null);
    System.out.println("table name - " + usTomcatNm);
    System.out.println(tblSqlUserTomcat.toString());
    for (String lKey : tblSqlUserTomcat.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlUserTomcat.getFieldsMap().get(lKey));
    }
    assertEquals("itsUser", tblSqlUserTomcat.getIdColumnsNames()[0]);
    assertEquals(2, tblSqlUserTomcat.getFieldsMap().size());

    String usRolTomcatNm = UserRoleTomcat.class.getSimpleName();
    System.out.println("Test for class - " + usRolTomcatNm);
    TableSql tblSqlUserRolTomcat = srvOrm.getTablesMap().get(usRolTomcatNm);
    assertTrue(tblSqlUserRolTomcat != null);
    System.out.println("table name - " + usRolTomcatNm);
    System.out.println(tblSqlUserRolTomcat.toString());
    for (String lKey : tblSqlUserRolTomcat.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlUserRolTomcat.getFieldsMap().get(lKey));
    }
    assertEquals(2, tblSqlUserRolTomcat.getFieldsMap().size());

    UserTomcat userTomcat = new UserTomcat();
    userTomcat.setItsUser("ut1");
    userTomcat.setItsPassword("pw1");
    UserRoleTomcat userRoleTomcat = new UserRoleTomcat();
    userRoleTomcat.setItsUser(userTomcat);
    userRoleTomcat.setItsRole("rl1");
    Map<String, Object> addParam = new HashMap<String, Object>();
    String sqlSelectUrt = srvOrm.evalSqlSelect(addParam, UserRoleTomcat.class);
    System.out.println(sqlSelectUrt);
    assertTrue(sqlSelectUrt.contains("left join USERTOMCAT as ITSUSER on USERROLETOMCAT.ITSUSER=ITSUSER.ITSUSER"));
    ColumnsValues colValUrt = srvOrm.evalColumnsValues(addParam, userRoleTomcat);
    String sqlInsertUrt = srvOrm.getHlpInsertUpdate().evalSqlInsert(UserRoleTomcat.class.getSimpleName()
      .toUpperCase(), colValUrt);
    System.out.println(sqlInsertUrt);
    assertTrue(sqlInsertUrt.contains("insert into USERROLETOMCAT (ITSROLE, ITSUSER)"));
    assertTrue(sqlInsertUrt.contains("values ('rl1', 'ut1')"));

    Class<?> clazzUserJet = org.beigesoft.persistable.UserJetty.class;
    String usJetNm = clazzUserJet.getSimpleName();
    System.out.println("Test for class - " + usJetNm);
    TableSql tblSqlUserJet = srvOrm.getTablesMap().get(usJetNm);
    assertTrue(tblSqlUserJet != null);
    System.out.println("table name - " + usJetNm);
    System.out.println(tblSqlUserJet.toString());
    for (String lKey : tblSqlUserJet.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlUserJet.getFieldsMap().get(lKey));
    }
    assertEquals(5, tblSqlUserJet.getFieldsMap().size());

    Class<?> clazzUserRolJet = org.beigesoft.persistable.UserRoleJetty.class;
    String usRolJetNm = clazzUserRolJet.getSimpleName();
    System.out.println("Test for class - " + usRolJetNm);
    TableSql tblSqlUserRolJet = srvOrm.getTablesMap().get(usRolJetNm);
    assertTrue(tblSqlUserRolJet != null);
    System.out.println("table name - " + usRolJetNm);
    System.out.println(tblSqlUserRolJet.toString());
    for (String lKey : tblSqlUserRolJet.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlUserRolJet.getFieldsMap().get(lKey));
    }
    assertEquals(2, tblSqlUserRolJet.getFieldsMap().size());
    assertTrue(tblSqlUserRolJet.getConstraint().contains("primary key (ITSUSER,ITSROLE)"));

    Class<?> clazzPersTest = org.beigesoft.test.persistable.PersistableHead.class;
    String namePersTest = clazzPersTest.getSimpleName();
    Object nameCast = (Object) namePersTest;
    assertEquals(String.class, nameCast.getClass());
    System.out.println("Test for class - " + namePersTest);
    TableSql tblSqlPersTest = srvOrm.getTablesMap().get(namePersTest);
    assertTrue(tblSqlPersTest != null);
    System.out.println("table name - " + namePersTest);
    System.out.println(tblSqlPersTest.toString());
    for (String lKey : tblSqlPersTest.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlPersTest.getFieldsMap().get(lKey));
    }
    assertTrue(tblSqlPersTest.getConstraint().contains("foreign key (ITSDEPARTMENT) references DEPARTMENT (ITSID)"));
    assertTrue(tblSqlPersTest.getConstraint().contains("constraint agef1to200 check (WAITINGTIME > 0 and WAITINGTIME < 201)"));

    Class<?> clazzPersLineTest = org.beigesoft.test.persistable.PersistableLine.class;
    String namePersLineTest = clazzPersLineTest.getSimpleName();
    System.out.println("Test for class - " + namePersLineTest);
    TableSql tblSqlPersLineTest = srvOrm.getTablesMap().get(namePersLineTest);
    assertTrue(tblSqlPersLineTest != null);
    System.out.println("table name - " + namePersLineTest);
    System.out.println(tblSqlPersLineTest.toString());
    for (String lKey : tblSqlPersLineTest.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlPersLineTest.getFieldsMap().get(lKey));
    }
    assertEquals(tblSqlPersLineTest.getConstraint().indexOf("perlnpriquangrt0"),
      tblSqlPersLineTest.getConstraint().lastIndexOf("perlnpriquangrt0"));
    assertTrue(tblSqlPersLineTest.getFieldsMap().get("idDatabaseBirth")
      .getDefinition().contains("not null"));
    PersistableHead ph = new PersistableHead();
    ph.setItsId(1L);
    PersistableLine pl = new PersistableLine();
    pl.setPersistableHead(ph);
    String selectPhLines = srvOrm.evalSqlSelect(addParam, PersistableLine.class)
     + srvOrm.evalWhereForField(addParam, pl, "persistableHead");
    System.out.println(selectPhLines);
    assertTrue(selectPhLines.contains("PERSISTABLEHEAD=1"));

    TableSql tblGoodVersionTime = srvOrm.getTablesMap().get(GoodVersionTime.class.getSimpleName());
    assertTrue(tblGoodVersionTime.getVersionAlgorithm() == 1);
    assertTrue(tblGoodVersionTime.getFieldsMap().containsKey(ISrvOrm.VERSION_NAME));
    assertTrue(tblGoodVersionTime.getFieldsMap().get("itsName")
      .getDefinition().contains("not null"));
    GoodVersionTime cake = new GoodVersionTime();
    cake.setItsName("cake");
    cake.setIdDatabaseBirth(999);
    assertNull(cake.getItsVersion());
    ColumnsValues colVal = srvOrm.evalColumnsValues(addParam, cake);
    String insStr = srvOrm.getHlpInsertUpdate().evalSqlInsert(cake.getClass().getSimpleName()
      .toUpperCase(), colVal);
    System.out.println(insStr);
    assertTrue(!insStr.contains("ITSID"));
    assertTrue(insStr.contains(ISrvOrm.VERSION_NAME.toUpperCase()));
    Long versionOld = colVal.getLong(ISrvOrm.VERSION_NAME);
    cake.setItsVersion(versionOld);
    cake.setItsId(1L); // pretend that is assigned by DB
    cake.setItsName("cake updated");
    colVal = srvOrm.evalColumnsValues(addParam, cake);
    String whereUpd = srvOrm.evalWhereForUpdate(cake, colVal);
    String insUpd = srvOrm.getHlpInsertUpdate().evalSqlUpdate(cake.getClass().getSimpleName()
      .toUpperCase(), colVal, whereUpd);
    assertNotSame(versionOld, colVal.getLong(ISrvOrm.VERSION_NAME));
    System.out.println(insUpd);
    assertTrue(insUpd.contains(" and " + ISrvOrm.VERSION_NAME.toUpperCase() + "=" + versionOld));
    //foreign key is primary key
    Class<?> clazzGr = org.beigesoft.test.persistable.GoodsRating.class;
    String nameGr = clazzGr.getSimpleName();
    TableSql tblSqlGr = srvOrm.getTablesMap().get(nameGr);
    System.out.println("table name - " + nameGr);
    System.out.println(tblSqlGr.toString());
    for (String lKey : tblSqlGr.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlGr.getFieldsMap().get(lKey));
    }
    GoodsRating gr = new GoodsRating();
    gr.setGoods(cake);
    gr.setAverageRating(7);
    ColumnsValues colValGr = srvOrm.evalColumnsValues(addParam, gr);
    String insStrGr = srvOrm.getHlpInsertUpdate().evalSqlInsert(gr.getClass().getSimpleName()
      .toUpperCase(), colValGr);
    System.out.println(insStrGr);
    String whereUpdGr = srvOrm.evalWhereForUpdate(gr, colValGr);
    System.out.println(whereUpdGr);
    assertTrue(whereUpdGr.contains("GOODS=1"));
    String updStrGr = srvOrm.getHlpInsertUpdate().evalSqlUpdate(gr.getClass().getSimpleName()
      .toUpperCase(), colValGr, whereUpdGr);
    System.out.println(updStrGr);
    //Composite primary and foreign ID:
    String nameUrtp = UserRoleTomcatPriority.class.getSimpleName();
    TableSql tblSqlUrtp = srvOrm.getTablesMap().get(nameUrtp);
    System.out.println("table name - " + nameUrtp);
    System.out.println(tblSqlUrtp.toString());
    for (String lKey : tblSqlUrtp.getFieldsMap().keySet()) {
      System.out.println(lKey + " - " + tblSqlUrtp.getFieldsMap().get(lKey));
    }
    UserRoleTomcatPriority urtp = new UserRoleTomcatPriority();
    urtp.setUserRoleTomcat(userRoleTomcat);
    urtp.setPriority(4);
    ColumnsValues colValUrtp = srvOrm.evalColumnsValues(addParam, urtp);
    String selectUrtp = srvOrm.evalSqlSelect(addParam, UserRoleTomcatPriority.class)
      + " where " + srvOrm.evalWhereId(urtp, colValUrtp) + ";";
    System.out.println(selectUrtp);
    assertTrue(selectUrtp.contains("where USERROLETOMCATPRIORITY.ITSUSER='ut1' and USERROLETOMCATPRIORITY.ITSROLE='rl1'"));
    assertTrue(colValUrtp.ifContains("itsUser"));
    assertTrue(colValUrtp.ifContains("itsRole"));
    String insStrUrtp = srvOrm.getHlpInsertUpdate().evalSqlInsert(gr.getClass().getSimpleName()
      .toUpperCase(), colValUrtp);
    System.out.println(insStrUrtp);
    String whereUpdUrtp = srvOrm.evalWhereForUpdate(urtp, colValUrtp);
    System.out.println(whereUpdUrtp);
    assertTrue(whereUpdUrtp.contains("USERROLETOMCATPRIORITY.ITSUSER='ut1' and USERROLETOMCATPRIORITY.ITSROLE='rl1'"));
    srvOrm.prepareColumnValuesForUpdate(colValUrtp, urtp);
    assertTrue(!colValUrtp.ifContains("itsUser"));
    assertTrue(!colValUrtp.ifContains("itsRole"));
    String updStrUrtp = srvOrm.getHlpInsertUpdate().evalSqlUpdate(urtp.getClass().getSimpleName()
      .toUpperCase(), colValUrtp, whereUpdUrtp);
    System.out.println(updStrUrtp);
  }
}
