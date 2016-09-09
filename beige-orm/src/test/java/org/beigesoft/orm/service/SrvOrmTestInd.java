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
import org.beigesoft.test.persistable.GoodVersionTime;
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
    assertEquals("itsUser", tblSqlUserTomcat.getIdName());
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
    assertNull(tblSqlUserRolTomcat.getIdName());
    assertNull(tblSqlUserRolTomcat.getIdDefinitionForeign());
    assertEquals(2, tblSqlUserRolTomcat.getFieldsMap().size());

    UserTomcat userTomcat = new UserTomcat();
    userTomcat.setItsUser("ut1");
    userTomcat.setItsPassword("pw1");
    UserRoleTomcat userRoleTomcat = new UserRoleTomcat();
    userRoleTomcat.setItsUser(userTomcat);
    userRoleTomcat.setItsRole("rl1");
    String sqlSelectUrt = srvOrm.evalSqlSelect(UserRoleTomcat.class);
    System.out.println(sqlSelectUrt);
    assertTrue(sqlSelectUrt.contains("left join USERTOMCAT as ITSUSER on USERROLETOMCAT.ITSUSER=ITSUSER.ITSUSER"));
    ColumnsValues colValUrt = srvOrm.evalColumnsValuesAndFillNewVersion(userRoleTomcat);
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
    assertNull(tblSqlUserRolJet.getIdName());
    assertNull(tblSqlUserRolJet.getIdDefinitionForeign());
    assertEquals(2, tblSqlUserRolJet.getFieldsMap().size());
    assertTrue(tblSqlUserRolJet.getConstraint().contains("primary key (ITSUSER, ITSROLE)"));

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

    TableSql tblGoodVersionTime = srvOrm.getTablesMap().get(GoodVersionTime.class.getSimpleName());
    assertTrue(tblGoodVersionTime.getVersionAlgorithm() == 1);
    assertTrue(tblGoodVersionTime.getFieldsMap().get("itsName")
      .getDefinition().contains("not null"));
    GoodVersionTime cake = new GoodVersionTime();
    cake.setItsName("cake");
    cake.setIdDatabaseBirth(999);
    assertNull(cake.getItsVersion());
    ColumnsValues colVal = srvOrm.evalColumnsValuesAndFillNewVersion(cake);
    String insStr = srvOrm.getHlpInsertUpdate().evalSqlInsert(cake.getClass().getSimpleName()
      .toUpperCase(), colVal);
    System.out.println(insStr);
    assertTrue(!insStr.contains("ITSID"));
    assertTrue(insStr.contains(ISrvOrm.VERSION_NAME.toUpperCase()));
    assertNotNull(cake.getItsVersion());
    Long versionOld = cake.getItsVersion(); 
    colVal = srvOrm.evalColumnsValuesAndFillNewVersion(cake);
    String whereUpd = srvOrm.evalWhereForUpdate(cake, colVal);
    String insUpd = srvOrm.getHlpInsertUpdate().evalSqlUpdate(cake.getClass().getSimpleName()
      .toUpperCase(), colVal, whereUpd);
    assertNotSame(versionOld, cake.getItsVersion());
    System.out.println(insUpd);
    assertTrue(insUpd.contains(" and " + ISrvOrm.VERSION_NAME.toUpperCase() + "=" + versionOld));
    //Create:
    PersistableHead persistableHead = srvOrm.createEntity(PersistableHead.class);
    //assertEquals(Integer.valueOf(999), persistableHead.getIdDatabaseBirth()); srvDatabase is null it's need a stub to test it
    persistableHead.setItsId(1L);
    PersistableLine persistableLine = srvOrm.createEntityWithOwner(PersistableLine.class, 
      PersistableHead.class, persistableHead.getItsId().toString());
    //assertEquals(Integer.valueOf(999), persistableLine.getIdDatabaseBirth());
    assertEquals(Long.valueOf(1), persistableLine.getPersistableHead().getItsId());
  }
}
