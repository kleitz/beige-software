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
import java.util.LinkedHashMap;
import java.io.File;
import java.math.BigDecimal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.model.TableSql;
import org.beigesoft.orm.model.FieldSql;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.GoodVersionTime;
import org.beigesoft.log.LoggerSimple;

/**
 * <p>Test of ORM service working without database.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestPostgresql {
  
  LoggerSimple logger = new LoggerSimple();

  @Test
  public void testDdlPsql() throws Exception {
    logger.setIsShowDebugMessages(true);
    logger.debug(TestPostgresql.class, "Start Postgresql ORM tests");
    SrvOrmPostgresql<String> srvOrm = new SrvOrmPostgresql<String>();
    srvOrm.setSrvSqlEscape(new SrvSqlEscape());
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvOrm.setLogger(logger);
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(logger);
    srvOrm.setMngSettings(mngSettings);
    srvOrm.loadConfiguration("beige-orm", "persistence-postgresql.xml");
    assertEquals("jdbc:postgresql:beigeaccountingtest", srvOrm.getPropertiesBase().getDatabaseUrl());
    assertEquals("org.postgresql.Driver", srvOrm.getPropertiesBase().getJdbcDriverClass());
    assertEquals("beigeaccounting", srvOrm.getPropertiesBase().getUserName());
    assertEquals("beigeaccounting", srvOrm.getPropertiesBase().getUserPassword());

    Class<?> clazzUserJet = org.beigesoft.persistable.UserJetty.class;
    String usJetNm = clazzUserJet.getSimpleName();
    TableSql tblSqlUserJet = srvOrm.getTablesMap().get(usJetNm);
    logger.debug(TestPostgresql.class, "table name - " + usJetNm);
    assertEquals("bigserial not null primary key", tblSqlUserJet.getFieldsMap().get("itsId").getDefinition());
    assertEquals("text not null unique", tblSqlUserJet.getFieldsMap().get("itsName").getDefinition());
    assertEquals("text not null", tblSqlUserJet.getFieldsMap().get("itsPassword").getDefinition());

    Class<?> clazzRoleJet = org.beigesoft.persistable.RoleJetty.class;
    String roleJetNm = clazzRoleJet.getSimpleName();
    TableSql tblSqlRoleJet = srvOrm.getTablesMap().get(roleJetNm);
    logger.debug(TestPostgresql.class, "table name - " + roleJetNm);
    assertEquals("bigserial not null primary key", tblSqlRoleJet.getFieldsMap().get("itsId").getDefinition());
    assertEquals("text not null unique", tblSqlRoleJet.getFieldsMap().get("itsName").getDefinition());

    Class<?> clazzUserTomcat = org.beigesoft.persistable.UserTomcat.class;
    String usTomcatNm = clazzUserTomcat.getSimpleName();
    TableSql tblSqlUserTomcat = srvOrm.getTablesMap().get(usTomcatNm);
    logger.debug(TestPostgresql.class, "table name - " + usTomcatNm);
    assertEquals(2, tblSqlUserTomcat.getFieldsMap().size());
    assertEquals("text not null primary key", tblSqlUserTomcat.getFieldsMap().get("itsUser").getDefinition());
    assertEquals("text not null", tblSqlUserTomcat.getFieldsMap().get("itsPassword").getDefinition());

    Class<?> clazzUserRoleTomcat = org.beigesoft.persistable.UserRoleTomcat.class;
    String userRoleTomcatNm = clazzUserRoleTomcat.getSimpleName();
    TableSql tblSqlUserRoleTomcat = srvOrm.getTablesMap().get(userRoleTomcatNm);
    logger.debug(TestPostgresql.class, "table name - " + userRoleTomcatNm);
    for (Map.Entry<String, FieldSql> entry : tblSqlUserRoleTomcat.getFieldsMap().entrySet()) {
      logger.debug(TestPostgresql.class, "field: " + entry.getKey() + " - " + entry.getValue().getDefinition());
    }
    assertEquals(2, tblSqlUserRoleTomcat.getFieldsMap().size());
    assertEquals("text not null", tblSqlUserRoleTomcat.getFieldsMap().get("itsUser").getDefinition());
    assertEquals("text not null", tblSqlUserRoleTomcat.getFieldsMap().get("itsRole").getDefinition());

    Class<?> clazzUserRolJet = org.beigesoft.persistable.UserRoleJetty.class;
    String usRolJetNm = clazzUserRolJet.getSimpleName();
    TableSql tblSqlUserRolJet = srvOrm.getTablesMap().get(usRolJetNm);
    logger.debug(TestPostgresql.class, "table name - " + usRolJetNm);
    assertEquals(2, tblSqlUserRolJet.getFieldsMap().size());
    assertEquals("bigint not null", tblSqlUserRolJet.getFieldsMap().get("itsUser").getDefinition());
    assertEquals("bigint not null", tblSqlUserRolJet.getFieldsMap().get("itsRole").getDefinition());

    Class<?> clazzPersTest = org.beigesoft.test.persistable.PersistableHead.class;
    String namePersTest = clazzPersTest.getSimpleName();
    TableSql tblSqlPersTest = srvOrm.getTablesMap().get(namePersTest);
    logger.debug(TestPostgresql.class, "table name - " + namePersTest);
    assertEquals("bigint not null", tblSqlPersTest.getFieldsMap().get("itsDepartment").getDefinition());
    assertEquals("integer not null", tblSqlPersTest.getFieldsMap().get("waitingTime").getDefinition());
    
    SrvOrmTestInd srvOrmTestInd = new SrvOrmTestInd();
    srvOrmTestInd.doRdbmsIndependentAssertions(srvOrm);
    logger.debug(TestPostgresql.class, "Exit Postgresql ORM tests");
  }
}
