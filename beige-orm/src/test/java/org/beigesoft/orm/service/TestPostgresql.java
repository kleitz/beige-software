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
import org.beigesoft.orm.model.ETypeField;
import org.beigesoft.test.persistable.PersistableHead;
import org.beigesoft.test.persistable.GoodVersionTime;
import org.beigesoft.test.persistable.GoodsRating;
import org.beigesoft.factory.FctConvertersToFromString;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.persistable.IdUserRoleTomcat;
import org.beigesoft.test.persistable.UserRoleTomcatPriority;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.orm.model.ColumnsValues;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.orm.service.SrvOrmSqlite;
import org.beigesoft.orm.service.SrvSqlEscape;
import org.beigesoft.orm.service.HlpInsertUpdate;
import org.beigesoft.orm.converter.CnvObjectToColumnsValues;
import org.beigesoft.converter.CnvTfsHasId;
import org.beigesoft.converter.CnvTfsString;
import org.beigesoft.converter.IConverter;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.log.LoggerSimple;

/**
 * <p>Test of ORM service working without database.
 * </p>
 *
 * @author Yury Demidenko
 */
public class TestPostgresql<RS> {
  
  LoggerSimple logger = new LoggerSimple();

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection = new UtlReflection();

  @Test
  public void testDdlPsql() throws Exception {
    //logger.setIsShowDebugMessages(true);
    logger.debug(TestPostgresql.class, "Start Postgresql ORM tests");
    SrvOrmPostgresql<RS> srvOrm = new SrvOrmPostgresql<RS>();
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvOrm.setLogger(logger);
    srvOrm.setUtlReflection(getUtlReflection());
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(logger);
    srvOrm.setMngSettings(mngSettings);
    srvOrm.loadConfiguration("beige-orm", "persistence-postgresql.xml");
    FctBnCnvIbnToColumnValues facConvFields = new FctBnCnvIbnToColumnValues();
    facConvFields.setUtlReflection(getUtlReflection());
    facConvFields.setTablesMap(srvOrm.getTablesMap());
    HolderRapiGetters hrg = new HolderRapiGetters();
    hrg.setUtlReflection(getUtlReflection());
    facConvFields.setGettersRapiHolder(hrg);
    HolderRapiFields hrf = new HolderRapiFields();
    hrf.setUtlReflection(getUtlReflection());
    facConvFields.setFieldsRapiHolder(hrf);
    facConvFields.setSrvSqlEscape(new SrvSqlEscape());
    FctBcCnvEntityToColumnsValues fcetcv = new FctBcCnvEntityToColumnsValues();
    HldCnvToColumnsValuesNames hldConvFld = new HldCnvToColumnsValuesNames();
    hldConvFld.setFieldsRapiHolder(hrf);
    fcetcv.setLogger(logger);
    fcetcv.setTablesMap(srvOrm.getTablesMap());
    fcetcv.setFieldsConvertersNamesHolder(hldConvFld);
    fcetcv.setGettersRapiHolder(hrg);
    fcetcv.setFieldsRapiHolder(hrf);
    fcetcv.setFieldsConvertersFatory(facConvFields);
    srvOrm.setFactoryCnvEntityToColumnsValues(fcetcv);
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
    assertEquals(2, tblSqlUserRoleTomcat.getFieldsMap().size());
    assertEquals("text not null", tblSqlUserRoleTomcat.getFieldsMap().get("itsUser").getDefinition());
    assertEquals("text not null", tblSqlUserRoleTomcat.getFieldsMap().get("itsRole").getDefinition());

    Class<?> clazzUserRoleTomcatPr = org.beigesoft.test.persistable.UserRoleTomcatPriority.class;
    String userRoleTomcatPrNm = clazzUserRoleTomcatPr.getSimpleName();
    TableSql tblSqlUserRoleTomcatPr = srvOrm.getTablesMap().get(userRoleTomcatPrNm);
    System.out.println("table name - " + userRoleTomcatPrNm);
    System.out.println("table - " + tblSqlUserRoleTomcatPr);
    assertEquals(4, tblSqlUserRoleTomcatPr.getFieldsMap().size());
    assertNull(tblSqlUserRoleTomcatPr.getFieldsMap().get("userRoleTomcat").getDefinition());
    assertTrue(tblSqlUserRoleTomcatPr.getFieldsMap().get("userRoleTomcat").getTypeField().equals(ETypeField.COMPOSITE_FK_PK));
    assertEquals("text not null", tblSqlUserRoleTomcatPr.getFieldsMap().get("itsUser").getDefinition());
    assertEquals("text not null", tblSqlUserRoleTomcatPr.getFieldsMap().get("itsRole").getDefinition());

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

  //Simple getters and setters:
  /**
   * <p>Getter for utlReflection.</p>
   * @return IUtlReflection
   **/
  public final IUtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final IUtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }
}
